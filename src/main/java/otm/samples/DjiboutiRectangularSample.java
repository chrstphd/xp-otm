package otm.samples;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

public class DjiboutiRectangularSample implements Sample {

    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildRectangular("Djibouti", zoom, policy,
                CoordinatesHelper.toCoordinates("N12°59.28' E41°31.09'"),
                CoordinatesHelper.toCoordinates("N10°31.07' E43°58.97'")
        ).generate();
    }
}
