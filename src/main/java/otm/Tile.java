package otm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.text.MessageFormat;

public class Tile {

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
     * Compute the URL of a tile based on the tile coordinates
     *
     * @return
     * @throws MalformedURLException
     */
    public URL getURL() throws MalformedURLException {
        return new URL("https://tile.openstreetmap.org/" + getName() + ".png");
    }

    /**
     * This content will describe the tile, used by the X-Trident plugin to load the tiles in the moving map.
     *
     * @return
     */
    public String getMap() {
        StringBuilder builder = new StringBuilder();

        builder.append("BITMAP_NAME\t").append(getName()).append(".tga").append("\n");
        builder.append("WIDTH\t256").append("\n");
        builder.append("HEIGHT\t256").append("\n");
        builder.append("LON_WEST\t").append(west).append("\n");
        builder.append("LON_EAST\t").append(east).append("\n");
        builder.append("LAT_SOUTH\t").append(south).append("\n");
        builder.append("LAT_NORTH\t").append(north);

        return builder.toString();
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

    public void writeImageOnDisk(Path output) {
        imagePath = output.resolve("cache/" + getName() + ".png");

        if (!imagePath.toFile().exists()) {
            try {
                // create the folders "zoom/x/"
                imagePath.getParent().toFile().mkdirs();

                ReadableByteChannel readableByteChannel = Channels.newChannel(getURL().openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(imagePath.toFile());
                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            } catch (IOException e) {
                // TODO rework the Exception handling
                System.out.println(MessageFormat.format("Tile {0}: {1}", getName(), e.toString()));
            }
        } else {
            System.out.println("image already fetched, no download");
        }
    }

    private void writeMapOnDisk(Path output) {
        try (FileWriter writer = new FileWriter(output.resolve(getName() + ".map").toFile())) {
            writer.write(getMap());
        } catch (IOException e) {
            // TODO rework the Exception handling
            System.out.println(MessageFormat.format("Tile {0}: {1}", getName(), e.toString()));
        }
    }

    public BufferedImage getBufferedImage(Path inputPath) throws IOException {
        return ImageIO.read(imagePath.toFile());
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
}
