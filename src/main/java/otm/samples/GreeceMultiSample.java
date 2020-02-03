package otm.samples;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

public class GreeceMultiSample implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildMulti("Greece", zoom, policy,
                AreaFactory.buildRectangular("Crete", zoom, policy,
                        CoordinatesHelper.toCoordinates("N35°59.87' E23°30.49'"),
                        CoordinatesHelper.toCoordinates("N34°31.06' E26°28.03'")
                ),
                AreaFactory.buildRectangular("Heraklion", zoom, policy,
                        CoordinatesHelper.toCoordinates("N35°59.73' E26°30.38'"),
                        CoordinatesHelper.toCoordinates("N35°0.79' E27°29.16'")
                )
        ).generate();
    }
}
