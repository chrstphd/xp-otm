package otm.tile;

import otm.OpenTopoMap;
import otm.util.Coordinates;
import otm.util.WaterImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

public class Tile {
    private static final int MAX_RETRIES = 5;

    private final Coordinates coordinates;
    private final int zoom;

    private int xtile;
    private int ytile;

    private double north;
    private double south;
    private double west;
    private double east;

    private Path imagePath;

    public Tile(Coordinates coordinates, int zoom) {
        this.coordinates = coordinates;
        this.zoom = zoom;

        getTileNumber();

        north = tile2lat(ytile, zoom);
        south = tile2lat(ytile + 1, zoom);
        west = tile2lon(xtile, zoom);
        east = tile2lon(xtile + 1, zoom);

        imagePath = OpenTopoMap.OTM_CACHE_DIR.resolve(getName() + ".png");
    }

    /**
     * Compute the tiling coordinates (x/y) from a lat,lon coordinates
     */
    private void getTileNumber() {
        int xtile = (int) Math.floor((coordinates.getLon() + 180) / 360 * (1 << zoom));
        int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(coordinates.getLat())) + 1 / Math.cos(Math.toRadians(coordinates.getLat()))) / Math.PI) / 2 * (1 << zoom));

        if (xtile < 0) xtile = 0;

        if (xtile >= (1 << zoom)) xtile = ((1 << zoom) - 1);

        if (ytile < 0) ytile = 0;

        if (ytile >= (1 << zoom)) ytile = ((1 << zoom) - 1);

        this.xtile = xtile;
        this.ytile = ytile;
    }

    private double tile2lon(int x, int z) {
        return x / Math.pow(2.0, z) * 360.0 - 180;
    }

    private double tile2lat(int y, int z) {
        double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
        return Math.toDegrees(Math.atan(Math.sinh(n)));
    }

    public String getName() {
        return zoom + "/" + xtile + "/" + ytile;
    }

    /**
     * Create the tile next to the right of the current tile
     * See the tiles like a simple table.
     *
     * @return
     */
    public Tile getRightNeighbor() {
        double middleLon = west + Math.abs(west - east) / 2;
        double nextLon = middleLon + Math.abs(west - east);

        return new Tile(new Coordinates(coordinates.getLat(), nextLon), zoom);
    }

    /**
     * Create the tile just lower of the current tile.
     * See the tiles like a simple table
     *
     * @return
     */
    public Tile getLowerNeighbor() {
        double middleLat = south + Math.abs(north - south) / 2;
        double nextLat = middleLat - Math.abs(north - south);

        return new Tile(new Coordinates(nextLat, coordinates.getLon()), zoom);
    }

    public void downloadImage() throws TileException {
        if (!checkIfPreviouslyDownloaded()) {
            // create the folders "zoom/x/"
            imagePath.getParent().toFile().mkdirs();

            for (int retry = 0; retry <= MAX_RETRIES; retry++) {
                // we compute, again, the URL to change the server on the fly
                final URL url;
                try {
                    url = new URL("https://" + ("abc".charAt((int) (Math.random() * 100) % 3)) + ".tile.opentopomap.org/" + getName() + ".png");
                } catch (MalformedURLException e) {
                    throw new TileException(e.toString(), e);
                }

                if (retry > 0) {
                    System.out.println("retrying #" + retry + "/" + MAX_RETRIES + ": " + url);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try (
                        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
                        FileOutputStream fileOutputStream = new FileOutputStream(imagePath.toFile())
                ) {
                    fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                    //System.out.println("downloaded: " + imagePath);

                    // break the loop
                    break;
                } catch (IOException e) {
                    if (e instanceof FileNotFoundException) {
                        try {
                            System.out.println("resource " + getName() + " not found on the server, probably plain water: flagging as such");
                            // we already know the image will be not found on the server, so we create a "water" flag instead if we have to re-process the same image
                            Files.createFile(Paths.get(imagePath.toFile().getAbsolutePath() + ".water"));
                            break;
                        } catch (IOException e1) {
                            System.out.println(MessageFormat.format("Tile {0}: {1}", imagePath, e.toString()));
                        }
                    } else {
                        if (!e.getMessage().contains("503 for URL")) {
                            throw new TileException(MessageFormat.format("Tile {0}: {1}", imagePath, e.toString()), e);
                        }
                    }
                }
            }

            // try to avoid to flood the server to avoid HTTP 503
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkIfPreviouslyDownloaded() {
        boolean alreadyDownloaded = imagePath.toFile().exists();

        // check if we create a "water only flag" instead
        if (!alreadyDownloaded) {
            String water = imagePath.toFile().getAbsolutePath() + ".water";
            alreadyDownloaded = new File(water).exists();
        }

        return alreadyDownloaded;
    }

    public BufferedImage getBufferedImage() throws IOException {
        if (imagePath.toFile().exists()) {
            // if the file exists, read it
            return ImageIO.read(imagePath.toFile());
        } else if (new File(imagePath.toFile().getAbsolutePath() + ".water").exists()) {
            // otherwise, it's certainly a water tile -> water image
            return WaterImage.getBufferedImage();
        } else {
            // real error...
            throw new IOException("the file " + imagePath + " is really missing !?!");
        }
    }

    public double getNorth() {
        return north;
    }

    public double getSouth() {
        return south;
    }

    public double getWest() {
        return west;
    }

    public double getEast() {
        return east;
    }

    public int getXtile() {
        return xtile;
    }

    public int getYtile() {
        return ytile;
    }
}
