package otm.samples;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

public class Sardinia implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildRectangular(
                "Sardinia", zoom, policy,
                CoordinatesHelper.toCoordinates("N41°27.95' E8°3.31'"),
                CoordinatesHelper.toCoordinates("N38°35.36' E9°56.47'")
        ).generate();
    }
}
