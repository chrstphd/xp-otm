package otm.samples;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

public class FaroeIslandsRectangularSample implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildRectangular(
                "Faroe Islands", zoom, policy,
                CoordinatesHelper.toCoordinates("N62°30.15' W7°59.95'"),
                CoordinatesHelper.toCoordinates("N61°1.28' W6°3.27'")
        ).generate();
    }
}
