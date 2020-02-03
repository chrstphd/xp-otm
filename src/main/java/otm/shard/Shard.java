package otm.shard;

import otm.Context;
import otm.OpenTopoMap;
import otm.area.route.Route;
import otm.tile.TileException;
import otm.util.Coordinates;
import otm.util.ErrorManager;
import otm.util.ProgressBar;
import otm.util.image.ColorImages;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class Shard {
    public static final int SHARD_PIXEL_SIZE = 256;
    private static final int MAX_RETRIES = 5;

    private final Coordinates coordinates;
    private final int zoom;

    private int xtile;
    private int ytile;

    private final double north;
    private final double south;
    private final double west;
    private final double east;

    private final Path tilePath;

    private static final Map<String, Double> latCache = new HashMap<>();
    private static final Map<String, Double> lonCache = new HashMap<>();

    public Shard(Coordinates coordinates, int zoom) {
        this.coordinates = coordinates;
        this.zoom = zoom;

        getTileNumber();

        this.north = tile2lat(ytile, zoom);
        this.south = tile2lat(ytile + 1, zoom);
        this.west = tile2lon(xtile, zoom);
        this.east = tile2lon(xtile + 1, zoom);

        this.tilePath = Context.getInstance().getCachePath().resolve(getName() + ".png");
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
        return lonCache.computeIfAbsent(x + "/" + z, v -> x / Math.pow(2.0, z) * 360.0 - 180);
    }

    private double tile2lat(int y, int z) {
        return latCache.computeIfAbsent(y + "/" + z, v ->
                Math.toDegrees(
                        Math.atan(
                                Math.sinh(
                                        Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z)
                                )
                        )
                )
        );
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
    public Shard getRightNeighbor() {
        double middleLon = west + Math.abs(west - east) / 2;
        double nextLon = middleLon + Math.abs(west - east);

        return new Shard(new Coordinates(coordinates.getLat(), nextLon), zoom);
    }

    /**
     * Create the tile just lower of the current tile.
     * See the tiles like a simple table
     *
     * @return
     */
    public Shard getLowerNeighbor() {
        double middleLat = south + Math.abs(north - south) / 2;
        double nextLat = middleLat - Math.abs(north - south);

        return new Shard(new Coordinates(nextLat, coordinates.getLon()), zoom);
    }

    public void generate(ProgressBar.ProgressItem shardProgress) {
        try {
            downloadImage(shardProgress);
        } catch (TileException e) {
            ErrorManager.getInstance().addError("shard", "error raised when generating " + getName(), e);
        }
    }

    private void downloadImage(ProgressBar.ProgressItem shardProgress) throws TileException {
        if (!checkIfPreviouslyDownloaded()) {
            // create the folders "zoom/x/"
            tilePath.getParent().toFile().mkdirs();

            for (int retry = 0; retry <= MAX_RETRIES; retry++) {
                // we compute, again, the URL to change the server on the fly
                final URL url;
                try {
                    url = new URL("https://" + ("abc".charAt((int) (Math.random() * 100) % 3)) + ".tile.opentopomap.org/" + getName() + ".png");
                } catch (MalformedURLException e) {
                    throw new TileException(e.toString(), e);
                }

                if (retry > 0) {
                    shardProgress.setTemporaryMessage("retrying #" + retry + "/" + MAX_RETRIES + ": " + url);

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        shardProgress.setTemporaryMessage(e.getMessage());
                    }
                } else {
                    shardProgress.setTemporaryMessage("retrieving: " + url);
                }

                try (
                        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
                        FileOutputStream fileOutputStream = new FileOutputStream(tilePath.toFile())
                ) {
                    fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

                    // break the loop
                    break;
                } catch (IOException e) {
                    if (e instanceof FileNotFoundException) {
                        try {
                            //System.out.println("resource " + getName() + " not found on the server, probably plain water: flagging as such");
                            // we already know the image will be not found on the server, so we create a "water" flag instead if we have to re-process the same image
                            Files.createFile(Paths.get(tilePath.toFile().getAbsolutePath() + ".water"));
                            break;
                        } catch (IOException e1) {
                            shardProgress.setTemporaryMessage(e.toString());
                        }
                    } else {
                        if (!e.getMessage().contains("503 for URL")) {
                            throw new TileException(MessageFormat.format("Shard {0}: {1}", tilePath, e.toString()), e);
                        }
                    }
                }
            }

            // try to avoid to flood the server to avoid HTTP 503
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                shardProgress.setTemporaryMessage(e.getMessage());
            }
        }
    }

    private boolean checkIfPreviouslyDownloaded() {
        boolean alreadyDownloaded = tilePath.toFile().exists();

        // check if we create a "water only flag" instead
        if (!alreadyDownloaded) {
            String water = tilePath.toFile().getAbsolutePath() + ".water";
            alreadyDownloaded = new File(water).exists();
        }

        return alreadyDownloaded;
    }

    public boolean isWater() {
        return new File(tilePath.toFile().getAbsolutePath() + ".water").exists();
    }

    public BufferedImage getBufferedImage() throws IOException {
        if (tilePath.toFile().exists()) {
            // if the file exists, read it
            try {
                return ImageIO.read(tilePath.toFile());
            } catch (Exception e) {
                ErrorManager.getInstance().addError("shard", "unable to read image " + tilePath, e);
                throw e;
            }
        } else if (isWater()) {
            return ColorImages.getInstance().getDefaultWaterImage();
        } else {
            // real error...
            throw new IOException("the file " + tilePath + " is really missing !?!");
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

    @Override
    public String toString() {
        return "Shard{" + getName() + "}";
    }

    public void drawLine(Color color, Route route) {
        for (int i = 1; i < route.getWaypoints().size(); i++) {

        }
    }
}
