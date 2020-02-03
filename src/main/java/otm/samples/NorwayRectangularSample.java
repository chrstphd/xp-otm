package otm.samples;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

public class NorwayRectangularSample implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildRectangular(
                "Norway", zoom, policy,
                CoordinatesHelper.toCoordinates("N62°54.85' E4°7.98'"),
                CoordinatesHelper.toCoordinates("N57°46.40' E10°56.67'")
        ).generate();
    }
}
