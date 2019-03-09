package otm.samples;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

public class CorsicaRectangularSample implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildRectangular(
                "Corsica", zoom, policy,
                CoordinatesHelper.toCoordinates("N43°8.85' E8°10.34'"),
                CoordinatesHelper.toCoordinates("N41°18.39' E9°54.93'")
        ).generate();
    }
}
