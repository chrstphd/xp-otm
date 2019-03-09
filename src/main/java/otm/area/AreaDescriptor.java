package otm.area;

import otm.shard.Shard;
import otm.tile.SubTilingPolicy;

import java.text.MessageFormat;

public abstract class AreaDescriptor {
    private final String name;
    private final int zoom;
    private final SubTilingPolicy policy;
    private final String areaFolderName;

    public AreaDescriptor(String name, int zoom, SubTilingPolicy policy) {
        this.name = name;
        this.zoom = zoom;
        this.policy = policy;
        this.areaFolderName = MessageFormat.format("{0}_{1}_{2}", name, zoom, (policy.nbOfShards * Shard.SHARD_PIXEL_SIZE));
    }

    public final String getName() {
        return name;
    }

    public final int getZoom() {
        return zoom;
    }

    public final SubTilingPolicy getPolicy() {
        return policy;
    }

    public final String getAreaFolderName() {
        return areaFolderName;
    }
}
