package otm.area;

import otm.shard.Shard;
import otm.tile.SubTilingPolicy;
import otm.util.Coordinates;

public class AreaDescriptor {
    private final String name;
    private final Coordinates nw;
    private final Coordinates se;
    private final int zoom;
    private final SubTilingPolicy policy;
    private final String areaFolderName;

    protected AreaDescriptor(String name, Coordinates nw, Coordinates se, int zoom, SubTilingPolicy policy) {
        this.name = name;
        this.nw = nw;
        this.se = se;
        this.zoom = zoom;
        this.policy = policy;
        this.areaFolderName = name + "_" + zoom + "_" + (policy.nbOfShards * Shard.SHARD_PIXEL_SIZE);
    }

    public String getName() {
        return name;
    }

    public Coordinates getNW() {
        return nw;
    }

    public Coordinates getSE() {
        return se;
    }

    public int getZoom() {
        return zoom;
    }

    public SubTilingPolicy getPolicy() {
        return policy;
    }

    public String getAreaFolderName() {
        return areaFolderName;
    }
}
