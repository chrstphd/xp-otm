package otm.common;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class MapDescription {
    private final String bitmapName;
    private final int width;
    private final int height;
    private final double westernLongitude;
    private final double eastearnLongitude;
    private final double northernLatitude;
    private final double southernLatitude;

    public MapDescription(String bitmapName, int width, int height, double westernLongitude, double eastearnLongitude, double northernLatitude, double southernLatitude) {
        this.bitmapName = bitmapName;
        this.width = width;
        this.height = height;
        this.westernLongitude = westernLongitude;
        this.eastearnLongitude = eastearnLongitude;
        this.northernLatitude = northernLatitude;
        this.southernLatitude = southernLatitude;
    }

    public void writeMapFile(Path outputDirPath) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("BITMAP_NAME\t").append(bitmapName + ".tga").append("\n");
        builder.append("WIDTH\t").append(width).append("\n");
        builder.append("HEIGHT\t").append(height).append("\n");
        builder.append("LON_WEST\t").append(westernLongitude).append("\n");
        builder.append("LON_EAST\t").append(eastearnLongitude).append("\n");
        builder.append("LAT_SOUTH\t").append(southernLatitude).append("\n");
        builder.append("LAT_NORTH\t").append(northernLatitude);

        try (FileWriter writer = new FileWriter(outputDirPath.resolve(bitmapName + ".map").toFile())) {
            writer.write(builder.toString());
        }
    }
}
