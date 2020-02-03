package otm.samples.test;

import otm.area.AreaFactory;
import otm.samples.Sample;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

public class FaroeIslandsNorthRectangularTest implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildRectangular(
                "Faroe Islands North Test", zoom, SubTilingPolicy.NO_SUB_TILING,
                CoordinatesHelper.toCoordinates("N62°2.04' W7°5.07'"),
                CoordinatesHelper.toCoordinates("N61°56.83' W6°50.68'")
        ).generate();
    }
}
