package otm.common;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;

public class MapDescription {
    private final String mapName;
    private final String bitmapName;
    private final int width;
    private final int height;
    private final double westernLongitude;
    private final double easternLongitude;
    private final double northernLatitude;
    private final double southernLatitude;

    public MapDescription(String mapName, String bitmapName, int width, int height, double westernLongitude, double easternLongitude, double northernLatitude, double southernLatitude) {
        this.mapName = mapName;
        this.bitmapName = bitmapName;
        this.width = width;
        this.height = height;
        this.westernLongitude = westernLongitude;
        this.easternLongitude = easternLongitude;
        this.northernLatitude = northernLatitude;
        this.southernLatitude = southernLatitude;
    }

    public void writeMapFile(Path outputDirPath) throws IOException {
        StringBuilder builder = new StringBuilder();

        builder.append(MessageFormat.format("BITMAP_NAME\t{0}.png\n", bitmapName));
        builder.append(MessageFormat.format("WIDTH\t{0,number,#}\n", width));
        builder.append(MessageFormat.format("HEIGHT\t{0,number,#}\n", height));
        builder.append(MessageFormat.format("LON_WEST\t{0,number,#.############}\n", westernLongitude));
        builder.append(MessageFormat.format("LON_EAST\t{0,number,#.############}\n", easternLongitude));
        builder.append(MessageFormat.format("LAT_SOUTH\t{0,number,#.############}\n", southernLatitude));
        builder.append(MessageFormat.format("LAT_NORTH\t{0,number,#.############}\n", northernLatitude));

        try (FileWriter writer = new FileWriter(outputDirPath.resolve(mapName + ".map").toFile())) {
            writer.write(builder.toString());
        }
    }
}
