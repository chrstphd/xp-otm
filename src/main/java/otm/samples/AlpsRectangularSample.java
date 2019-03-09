package otm.samples;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

public class AlpsRectangularSample implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildRectangular(
                "Alps", zoom, policy,
                CoordinatesHelper.toCoordinates("N47°3.94' E5°54.33'"),
                CoordinatesHelper.toCoordinates("N45°37.18' E15°56.82'")
        ).generate();
    }
}
