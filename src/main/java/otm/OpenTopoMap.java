package otm;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

public class OpenTopoMap {

    /**
     * to be able to download maps through https
     */
    private static void allowSecuredHttp() {
        // Create a new trust manager that trust all certificates
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Activate the new trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        allowSecuredHttp();

        // where the files need to be stored (.map and .png)
        final Path outputDir = Paths.get("E:\\XPlane11\\Aircraft\\X-Trident.beta\\AV8B Harrier II.20181006\\plugins\\harrier\\maps");

        // the zoom of the map (11->18)
        final int zoom = 13;

        // the latitude/longitude in degrees
        // see e.g. for the conversion: http://www.onlineconversion.com/map_decimaldegrees.htm
        final double lat = 45.88750000d;
        final double lon = 10.83916667d;

        // the width of the matrix -> (size x size) tiles = size * size * 2 files
        final int size = 10;

        final int maxTiles = size * size;
        System.out.println("will generate " + (maxTiles) + " tile(s)");

        final Tile[][] tiles = new Tile[size][size];
        // create the first line
        for (int j = 0; j < size; j++) {
            if (j == 0) {
                tiles[0][j] = new Tile(new Coordinates(lat, lon), zoom);
            } else {
                tiles[0][j] = tiles[0][j - 1].getRightNeighbor();
            }

            System.out.println(MessageFormat.format("working on [{0}/{1}]: {2}", (j + 1), maxTiles, tiles[0][j].getName()));
            tiles[0][j].writeImageOnDisk(outputDir);
        }

        // fill the whole matrix
        for (int i = 1; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (j == 0) {
                    // create a new tile based on the previous row
                    tiles[i][j] = tiles[i - 1][j].getLowerNeighbor();
                } else {
                    // create a new tile base on the previous tile
                    tiles[i][j] = tiles[i][j - 1].getRightNeighbor();
                }

                System.out.println(MessageFormat.format("working on [{0}/{1}]: {2}", (i * size) + j + 1, maxTiles, tiles[i][j].getName()));
                tiles[i][j].writeImageOnDisk(outputDir);
            }
        }

        // all the images are fetched, now we merge them in one
        final int combinedSize = size * 256;
        BufferedImage combined = new BufferedImage(combinedSize, combinedSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();
        try {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    g.drawImage(tiles[i][j].getBufferedImage(outputDir), j * 256, i * 256, null);
                }
            }

            ImageIO.write(combined, "PNG", outputDir.resolve("combined.png").toFile());
        } catch (IOException e) {
            // TODO rework the Exception handling
            System.out.println(MessageFormat.format("error when merging images: {0}", e.toString()));
        }

        // generate the combined .map
        StringBuilder builder = new StringBuilder();
        builder.append("BITMAP_NAME\tcombined.tga").append("\n");
        builder.append("WIDTH\t").append(combinedSize).append("\n");
        builder.append("HEIGHT\t").append(combinedSize).append("\n");
        builder.append("LON_WEST\t").append(tiles[0][0].getWest()).append("\n");
        builder.append("LON_EAST\t").append(tiles[0][size - 1].getEast()).append("\n");
        builder.append("LAT_SOUTH\t").append(tiles[0][size - 1].getSouth()).append("\n");
        builder.append("LAT_NORTH\t").append(tiles[0][0].getNorth());
        try(FileWriter writer = new FileWriter(outputDir.resolve("combined.map").toFile())){
            writer.write(builder.toString());
        } catch (IOException e) {
            // TODO rework the Exception handling
            System.out.println(MessageFormat.format("error when creating combined.map: {0}", e.toString()));
        }

    }
}
