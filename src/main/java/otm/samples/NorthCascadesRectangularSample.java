package otm.samples;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

public class NorthCascadesRectangularSample implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildRectangular(
                "North Cascades National Park", zoom, policy,
                CoordinatesHelper.toCoordinates("N48°59.77' W122°59.62'"),
                CoordinatesHelper.toCoordinates("N48°1.33' W120°31.85'")
        ).generate();
    }
}
