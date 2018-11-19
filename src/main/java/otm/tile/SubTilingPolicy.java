package otm.tile;

public enum SubTilingPolicy {
    NO_SUB_TILING(99999),
    SHARD_BY_SHARD(1),
    FOUR_BY_FOUR_SHARDS(4),
    EIGHT_BY_EIGHT_SHARDS(8);

    public final int nbOfShards;

    SubTilingPolicy(int size) {
        this.nbOfShards = size;
    }
}
