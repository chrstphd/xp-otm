package otm.samples;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

public class SicilyRectangularSample implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildRectangular(
                "Sicily", zoom, policy,
                CoordinatesHelper.toCoordinates("N38°29.76' E11°30.18'"),
                CoordinatesHelper.toCoordinates("N36°30.67' E15°59.12'")
        ).generate();
    }
}
