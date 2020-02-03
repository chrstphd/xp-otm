package otm.tile;

import otm.area.route.Route;
import otm.common.MapDescription;
import otm.shard.Shard;
import otm.util.Coordinates;
import otm.util.CoordinatesHelper;
import otm.util.ErrorManager;
import otm.util.NameTool;
import otm.util.ProgressBar;
import otm.util.image.ColorImages;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

public class Tile {
    private final Coordinates coords;
    private final Coordinates nw;
    private final Coordinates se;
    private final int zoom;
    private final SubTilingPolicy subTilingPolicy;
    private final String tileName;

    private Shard[][] shards;
    private int nbOfShards;
    private int verticalSubdivisions;
    private int horizontalSubdivisions;

    public Tile(Coordinates coords, int zoom, SubTilingPolicy subTilingPolicy) {
        // get only the SW coordinates
        this.coords = new Coordinates((int) coords.getLat(), (int) coords.getLon());
        this.nw = CoordinatesHelper.toTileCoordinateUpperNW(coords);
        this.se = CoordinatesHelper.toTileCoordinateLowerSE(coords);
        this.zoom = zoom;
        this.subTilingPolicy = subTilingPolicy;

        this.tileName = NameTool.createNameFromCoordinates(coords, zoom, subTilingPolicy);
    }

    @Override
    public String toString() {
        return "Tile{" + tileName + '}';
    }

    public Shard[][] getShards() {
        return shards;
    }

    public int prepare() {
        final Shard nwShard = new Shard(nw, zoom);
        final Shard seShard = new Shard(se, zoom);

        final int width = Math.abs(nwShard.getXtile() - seShard.getXtile()) + 1;
        final int height = Math.abs(nwShard.getYtile() - seShard.getYtile()) + 1;
        nbOfShards = width * height;

        // generate the shard matrix
        shards = new Shard[height][width];

        // fill the shard matrix
        for (int v = 0; v < height; v++) {
            for (int h = 0; h < width; h++) {
                if (v == 0 && h == 0) {
                    shards[0][h] = nwShard;
                } else {
                    if (h == 0) {
                        // create a new tile based on the previous row
                        shards[v][h] = shards[v - 1][h].getLowerNeighbor();
                    } else {
                        // create a new tile base on the previous tile
                        shards[v][h] = shards[v][h - 1].getRightNeighbor();
                    }
                }
            }
        }

        // compute the sub-tiling
        this.verticalSubdivisions = (shards.length / subTilingPolicy.nbOfShards) + (shards.length % subTilingPolicy.nbOfShards == 0 ? 0 : 1);
        this.horizontalSubdivisions = (shards[0].length / subTilingPolicy.nbOfShards) + (shards[0].length % subTilingPolicy.nbOfShards == 0 ? 0 : 1);

        System.out.println("tile name: " + tileName);
        System.out.println("zoom: " + zoom);
        System.out.println("coords:");
        System.out.println("  NW: " + nw);
        System.out.println("  SE: " + se);
        System.out.println("shards:");
        System.out.println("  -> " + height + "x" + width + "=" + (width * height) + " shards");
        System.out.println("  => " + (height * Shard.SHARD_PIXEL_SIZE) + "x" + (width * Shard.SHARD_PIXEL_SIZE) + " combined pixels");
        System.out.println("sub-tiling policy: " + subTilingPolicy.name());
        System.out.println("  -> " + verticalSubdivisions + "x" + horizontalSubdivisions + " sub-tiles");
        System.out.println("  => " + (subTilingPolicy.nbOfShards * Shard.SHARD_PIXEL_SIZE) + "x" + (subTilingPolicy.nbOfShards * Shard.SHARD_PIXEL_SIZE) + " pixels per sub-tile");
        System.out.println("shards limits:");
        System.out.println("  - shard left (w): " + shards[0][0].getWest());
        System.out.println("  - shard right (e): " + shards[0][shards[0].length - 1].getEast());
        System.out.println("------------------");

        return (height * width);
    }

    public void drawLine(Color color, Route route) {
        // if the line is over this tile, draw it
        for (int i = 0; i < shards.length; i++) {
            for (int j = 0; j < shards[i].length; j++) {
                shards[i][j].drawLine(color, route);
            }
        }
    }

    /**
     * download all the shard images
     */
    public void download(ProgressBar progress) {
        try (ProgressBar.ProgressItem shardsProgress = progress.createProgressItem("Downloading shards", nbOfShards)) {
            long currentShardIndex = 0;
            for (int v = 0; v < shards.length; v++) {
                for (int h = 0; h < shards[0].length; h++) {
                    shardsProgress.increment(MessageFormat.format("processing shard #{0}/{1}", currentShardIndex++, nbOfShards));
                    shards[v][h].generate(shardsProgress);
                }
            }
        }
    }

    public void merge(ProgressBar progress, Path areaFolderPath) {
        // group shards as described by the policy
        try (ProgressBar.ProgressItem shardsProgress = progress.createProgressItem("Merging shards", verticalSubdivisions * horizontalSubdivisions)) {
            long currentShardIndex = 0;
            for (int v = 0; v < verticalSubdivisions; v++) {
                for (int h = 0; h < horizontalSubdivisions; h++) {
                    shardsProgress.increment(MessageFormat.format("merging shard #{0}/{1}", currentShardIndex++, nbOfShards));
                    String subTileName = MessageFormat.format("{0}-{1,number,000}-{2,number,000}", tileName, v, h);
                    mergeShards(v * subTilingPolicy.nbOfShards, h * subTilingPolicy.nbOfShards, areaFolderPath, subTileName);
                }
            }
        }
    }

    private void mergeShards(int vIndex, int hIndex, Path outFolderPath, String subTileName) {
        if (outFolderPath.resolve(subTileName + ".png").toFile().exists()) {
            return;
        }

        final int height = Math.min(shards.length - vIndex, subTilingPolicy.nbOfShards);
        final int width = Math.min(shards[0].length - hIndex, subTilingPolicy.nbOfShards);

        try {
            // first check if the tile is full of water tiles
            // if so, avoid to generate the map
            boolean plainWater = true;

            for (int v = 0; v < height; v++) {
                for (int h = 0; h < width; h++) {
                    if (!shards[vIndex + v][hIndex + h].isWater()) {
                        plainWater = false;
                    }
                }
            }

            if (!plainWater) {
                final int mergedWidthInPixels = width * Shard.SHARD_PIXEL_SIZE;
                final int mergedHeightInPixels = height * Shard.SHARD_PIXEL_SIZE;

                BufferedImage merged = new BufferedImage(mergedWidthInPixels, mergedHeightInPixels, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = merged.createGraphics();

                for (int v = 0; v < height; v++) {
                    for (int h = 0; h < width; h++) {
                        try {
                            g.drawImage(shards[vIndex + v][hIndex + h].getBufferedImage(), h * Shard.SHARD_PIXEL_SIZE, v * Shard.SHARD_PIXEL_SIZE, null);
                        } catch (IOException e) {
                            throw e;
                        }
                    }
                }

                // TODO investigate why, sometimes, filtering .water is not enough
                // detect used RGB colors
                Set<Integer> rgbs = new HashSet<>();
                for (int v = 0; v < merged.getHeight(); v++) {
                    for (int h = 0; h < merged.getWidth(); h++) {
                        rgbs.add(Integer.valueOf(merged.getRGB(h, v)));
                    }
                }

                // if only one color and water ->
                if (!(rgbs.size() == 1 && rgbs.contains(Integer.valueOf(-6038040)))) {
                    ImageIO.write(merged, "PNG", outFolderPath.resolve(subTileName + ".png").toFile());

                    // create the .map file only if the combined image is OK
                    MapDescription map = new MapDescription(
                            subTileName, // the name of the .map
                            subTileName, // the name of the image
                            mergedWidthInPixels,
                            mergedHeightInPixels,
                            shards[vIndex][hIndex].getWest(),
                            shards[vIndex][hIndex + width - 1].getEast(),
                            shards[vIndex][hIndex].getNorth(),
                            shards[vIndex + height - 1][hIndex + width - 1].getSouth()
                    );

                    // write the .map descriptor
                    map.writeMapFile(outFolderPath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorManager.getInstance().addError("tile", "unable to merge " + subTileName, e);
        }

        ErrorManager.getInstance().dump(System.out);
    }

    public String getTileName() {
        return tileName;
    }

    public void paint(String colorName, Color color, Path outputFolderPath) {
        final File imageFile = outputFolderPath.resolve(MessageFormat.format("{0}.png", colorName)).toFile();

        final int imageWidth = 4;
        final int imageHeight = 4;

        if (!imageFile.exists()) {
            try {
                ImageIO.write(
                        ColorImages.getInstance().getImage(colorName, color, imageWidth, imageHeight),
                        "PNG",
                        imageFile
                );
            } catch (IOException e) {
                e.printStackTrace();
                ErrorManager.getInstance().addError("color", "unable to paint a color layer", e);
            }
        }

        MapDescription map = new MapDescription(
                tileName,
                colorName,
                imageWidth,
                imageHeight,
                shards[0][0].getWest(),
                shards[shards.length - 1][shards[0].length - 1].getEast(),
                shards[0][0].getNorth(),
                shards[shards.length - 1][shards[0].length - 1].getSouth()
        );

        // write the .map descriptor
        try {
            map.writeMapFile(outputFolderPath);
        } catch (IOException e) {
            e.printStackTrace();
            ErrorManager.getInstance().addError("color", "unable to write the layer descriptor", e);
        }
    }

    public void describe(){
        Shard nws = shards[0][0];
        Shard ses = shards[shards.length-1][shards[0].length-1];

        System.out.println("+----------------------------+");
        System.out.println(MessageFormat.format("|     ^N: {0,number,#00.000000000}       |", nws.getNorth()));
        System.out.println(MessageFormat.format("|<W: {0,number,#00.000000000}           |", nws.getWest()));
        System.out.println("|                            |");
        System.out.println(MessageFormat.format("|           {0,number,#00.000000000} :E>|", ses.getEast()));
        System.out.println(MessageFormat.format("|     vS: {0,number,#00.000000000}       |", ses.getSouth()));
        System.out.println("+----------------------------+");
    }
}
