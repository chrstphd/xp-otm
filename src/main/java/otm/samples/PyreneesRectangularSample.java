package otm.samples;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

public class PyreneesRectangularSample implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildRectangular(
                "Pyrenees", zoom, policy,
                CoordinatesHelper.toCoordinates("N43°29.12' W1°57.20'"),
                CoordinatesHelper.toCoordinates("N41°33.84' E3°26.24'")
        ).generate();
    }
}
