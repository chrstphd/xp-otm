package otm.samples;

import otm.tile.SubTilingPolicy;

public interface Sample {

   void generate(int zoom, SubTilingPolicy policy) throws Exception;
}
