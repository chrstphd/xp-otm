package otm.tile;

import otm.common.MapDescription;
import otm.shard.Shard;
import otm.util.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;

public class Tile {
    private final Coordinates coords;
    private final Coordinates nw;
    private final Coordinates se;
    private final int zoom;
    private final SubTilingPolicy subTilingPolicy;
    private final String tileName;

    private Shard[][] shards;
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

    public int prepare() {
        Shard nwShard = new Shard(nw, zoom);
        Shard seShard = new Shard(se, zoom);

        int width = Math.abs(nwShard.getXtile() - seShard.getXtile()) + 1;
        int height = Math.abs(nwShard.getYtile() - seShard.getYtile()) + 1;

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

        System.out.println("tileName: " + tileName);
        System.out.println("zoom: " + zoom);
        System.out.println("shard(s): " + height + "x" + width + " shard(s)");
        System.out.println("  -> " + (width * height) + " shards(s)");
        System.out.println("  => " + (height * Shard.SHARD_PIXEL_SIZE) + "x" + (width * Shard.SHARD_PIXEL_SIZE) + " combined pixels");
        System.out.println("sub-tiling policy: " + subTilingPolicy.name());
        System.out.println("  -> " + verticalSubdivisions + "x" + horizontalSubdivisions + " sub-tiles");
        System.out.println("  -> " + (subTilingPolicy.nbOfShards * Shard.SHARD_PIXEL_SIZE) + "x" + (subTilingPolicy.nbOfShards * Shard.SHARD_PIXEL_SIZE) + " pixels per sub-tile");
        System.out.println("------------------");

        return (height * width);
    }

    public void generate(String areaName, ProgressBar progress, Path areaFolderPath) {
        final int nbOfShards = shards.length * shards[0].length;

        // download the image of each shard
        try (ProgressBar.ProgressItem shardsProgress = progress.createProgressItem("[# ] Downloading shards", nbOfShards)) {
            long currentShardIndex = 0;
            for (int v = 0; v < shards.length; v++) {
                for (int h = 0; h < shards[0].length; h++) {
                    shardsProgress.increment(MessageFormat.format("processing shard #{0}/{1}", currentShardIndex++, nbOfShards));
                    shards[v][h].generate();
                }
            }
        }

        // group shards as described by the policy
        try (ProgressBar.ProgressItem shardsProgress = progress.createProgressItem("[##] Merging shards", verticalSubdivisions * horizontalSubdivisions)) {
            for (int v = 0; v < verticalSubdivisions; v++) {
                for (int h = 0; h < horizontalSubdivisions; h++) {
                    shardsProgress.increment();
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

        final int mergedWidthInPixels = width * Shard.SHARD_PIXEL_SIZE;
        final int mergedHeightInPixels = height * Shard.SHARD_PIXEL_SIZE;

        BufferedImage merged = new BufferedImage(mergedWidthInPixels, mergedHeightInPixels, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = merged.createGraphics();
        try {
            for (int v = 0; v < height; v++) {
                for (int h = 0; h < width; h++) {
                    try {
                        g.drawImage(shards[vIndex + v][hIndex + h].getBufferedImage(), h * Shard.SHARD_PIXEL_SIZE, v * Shard.SHARD_PIXEL_SIZE, null);
                    } catch (IOException e) {
                        throw e;
                    }
                }
            }

            ImageIO.write(merged, "PNG", outFolderPath.resolve(subTileName + ".png").toFile());

            // create the .map file only if the combined image is OK
            MapDescription map = new MapDescription(
                    subTileName,
                    mergedWidthInPixels,
                    mergedHeightInPixels,
                    shards[vIndex][hIndex].getWest(),
                    shards[vIndex][hIndex + width - 1].getEast(),
                    shards[vIndex][hIndex].getNorth(),
                    shards[vIndex + height - 1][hIndex + width - 1].getSouth()
            );

            // write the .map descriptor
            map.writeMapFile(outFolderPath);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorManager.getInstance().addError("tile", "unable to merge " + subTileName, e);
        }

        ErrorManager.getInstance().dump(System.out);
    }

    public String getTileName() {
        return tileName;
    }
}
