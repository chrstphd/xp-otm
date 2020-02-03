package otm.samples;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

public class IndonesiaRectangularSample implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildRectangular(
                "Indonesia", zoom, policy,
                CoordinatesHelper.toCoordinates("S5°59.88' E105°31.72"),
                CoordinatesHelper.toCoordinates("S7°57.97' E108°28.05'")
        ).generate();
    }



}
