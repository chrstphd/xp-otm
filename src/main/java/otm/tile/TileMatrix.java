package otm.tile;

import otm.OpenTopoMap;
import otm.util.Coordinates;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;

public class TileMatrix {

    private final Coordinates northWest;
    private final Coordinates southEast;
    private final int zoom;
    private final String tileMatrixName;
    private Tile[][] matrices;

    protected TileMatrix(Coordinates nw, Coordinates se, int zoom, String tileMatrixName) throws TileException {
        this.northWest = nw;
        this.southEast = se;
        this.zoom = zoom;
        this.tileMatrixName = tileMatrixName;
    }

    public void generate() throws TileException {
        final Path pngPath = OpenTopoMap.OTM_WORK_DIR.resolve(tileMatrixName + ".png");

        //if the file already exists, don't do anything.
        if (pngPath.toFile().exists()) {
            System.out.println("### " + tileMatrixName + " generation bypassed: files already exist");
            return;
        }

        System.out.println("###");
        System.out.println("# working on " + tileMatrixName);
        System.out.println("###");

        // create the opposite corners to compute in-between tiles
        Tile nw = new Tile(northWest, zoom);
        Tile se = new Tile(southEast, zoom);

        int width = Math.abs(nw.getXtile() - se.getXtile()) + 1;
        int height = Math.abs(nw.getYtile() - se.getYtile()) + 1;

        System.out.println("zoom: " + zoom);
        System.out.println("width: " + width + " image(s)");
        System.out.println("height: " + height + " image(s)");
        System.out.println("  -> " + (width * height) + " tile(s) to manage");
        System.out.println("combined pixels: " + (width * 256) + "x" + (height * 256));

        // generate the whole matrices
        matrices = new Tile[height][width];

        // fill the whole matrices
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 && j == 0) {
                    matrices[0][j] = nw;
                } else {
                    if (j == 0) {
                        // create a new tile based on the previous row
                        matrices[i][j] = matrices[i - 1][j].getLowerNeighbor();
                    } else {
                        // create a new tile base on the previous tile
                        matrices[i][j] = matrices[i][j - 1].getRightNeighbor();
                    }
                }
            }
        }

        // check if the lower right corner corresponds to se's tile
        System.out.println("last tile:  " + matrices[height - 1][width - 1].getName());
        System.out.println("south east: " + se.getName());

        downloadTiles();
        writeFiles();
    }

    private void downloadTiles() throws TileException {
        System.out.println("downloading tile images...");
        for (int i = 0; i < matrices.length; i++) {
            for (int j = 0; j < matrices[0].length; j++) {
                matrices[i][j].downloadImage();
            }
        }
    }

    private void writeFiles() throws TileException {
        System.out.println("merging tiles in one...");
        final Path pngPath = OpenTopoMap.OTM_WORK_DIR.resolve(tileMatrixName + ".png");

        final int width = matrices[0].length;
        final int height = matrices.length;

        // all the images are fetched, now we merge them in one
        final int combinedWidthInPixels = width * 256;
        final int combinedHeightInPixels = height * 256;

        BufferedImage combined = new BufferedImage(combinedWidthInPixels, combinedHeightInPixels, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();
        try {
            for (int i = 0; i < matrices.length; i++) {
                for (int j = 0; j < matrices[0].length; j++) {
                    try {
                        g.drawImage(matrices[i][j].getBufferedImage(), j * 256, i * 256, null);
                    } catch (IOException e) {
                        System.out.println("error> " + matrices[i][j].getName() + " -> " + e.toString());
                        throw e;
                    }
                }
            }

            ImageIO.write(combined, "PNG", pngPath.toFile());

            // create the .map file only if the combined image is OK
            writeMapFile(width, height, combinedWidthInPixels, combinedHeightInPixels);
        } catch (IOException e) {
            throw new TileException(MessageFormat.format("error when merging images: {0}", e.toString()), e);
        }
    }

    private void writeMapFile(int width, int height, int combinedWidthInPixels, int combinedHeightInPixels) throws TileException {
        System.out.println("creating the description of the map...");
        final Path mapPath = OpenTopoMap.OTM_WORK_DIR.resolve(tileMatrixName + ".map");
        StringBuilder builder = new StringBuilder();
        builder.append("BITMAP_NAME\t").append(tileMatrixName + ".tga").append("\n");
        builder.append("WIDTH\t").append(combinedWidthInPixels).append("\n");
        builder.append("HEIGHT\t").append(combinedHeightInPixels).append("\n");
        builder.append("LON_WEST\t").append(matrices[0][0].getWest()).append("\n");
        builder.append("LON_EAST\t").append(matrices[0][width - 1].getEast()).append("\n");
        builder.append("LAT_SOUTH\t").append(matrices[height - 1][0].getSouth()).append("\n");
        builder.append("LAT_NORTH\t").append(matrices[0][0].getNorth());
        try (FileWriter writer = new FileWriter(mapPath.toFile())) {
            writer.write(builder.toString());
        } catch (IOException e) {
            throw new TileException(MessageFormat.format("error when creating {}.map: {}", tileMatrixName, e.toString()), e);
        }
    }
}
