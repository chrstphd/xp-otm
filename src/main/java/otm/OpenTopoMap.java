package otm;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
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
        final int size = 4;

        final int maxTiles = size*size;
        System.out.println("will generate " + (maxTiles) + " tile(s)");

        final Tile[][] tiles = new Tile[size][size];
        // create the first line
        for (int j = 0; j < size; j++) {
            if (j == 0) {
                tiles[0][j] = new Tile(new Coordinates(lat, lon), zoom);
            } else {
                tiles[0][j] = tiles[0][j - 1].getRightNeighbor();
            }

            System.out.println(MessageFormat.format("working on [{0}/{1}]: {2}", (j+1), maxTiles, tiles[0][j].getName()));
            tiles[0][j].writeOnDisk(outputDir);
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

                System.out.println(MessageFormat.format("working on [{0}/{1}]: {2}", (i*size)+j+1, maxTiles, tiles[i][j].getName()));
                tiles[i][j].writeOnDisk(outputDir);
            }
        }
    }
}
