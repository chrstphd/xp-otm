package otm.util;

import otm.shard.Shard;
import otm.tile.SubTilingPolicy;

import java.text.MessageFormat;

public class NameTool {
    public static String createDefaultName(String name, int zoom, SubTilingPolicy subTilingPolicy) {
        return MessageFormat.format(
                "{0}_{1,number,00}_{2,number,0000}",
                name,
                zoom,
                subTilingPolicy.nbOfShards * Shard.SHARD_PIXEL_SIZE
        );
    }

    public static String createNameFromCoordinates(Coordinates coords, int zoom, SubTilingPolicy subTilingPolicy) {
        return createDefaultName(
                CoordinatesHelper.toText(coords),
                zoom,
                subTilingPolicy
        );
    }
}
