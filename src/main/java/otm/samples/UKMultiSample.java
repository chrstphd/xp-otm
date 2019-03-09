package otm.samples;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

public class UKMultiSample implements  Sample{

    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildMulti("UK", zoom, policy,
                AreaFactory.buildRectangular(
                        "UK-Shetland", zoom, policy,
                        CoordinatesHelper.toCoordinates("N60°58.87' W2°27.56'"),
                        CoordinatesHelper.toCoordinates("N59°31.72' W0°32.86'")
                ),
                AreaFactory.buildRectangular(
                        "UK-Orkneys", zoom, policy,
                        CoordinatesHelper.toCoordinates("N60°59.11' W3°27.25'"),
                        CoordinatesHelper.toCoordinates("N59°1.42' W2°1.85'")
                ),
                AreaFactory.buildRectangular(
                        "UK-North", zoom, policy,
                        CoordinatesHelper.toCoordinates("N58°32.24' W7°55.73'"),
                        CoordinatesHelper.toCoordinates("N55°5.68' W1°9.02'")
                ),
                AreaFactory.buildRectangular(
                        "UK-South", zoom, policy,
                        CoordinatesHelper.toCoordinates("N54°59.22' W5°28.67'"),
                        CoordinatesHelper.toCoordinates("N51°1.28' E1°57.16'")
                ),
                AreaFactory.buildRectangular(
                        "UK-South-2", zoom, policy,
                        CoordinatesHelper.toCoordinates("N50°57.34' W5°22.82'"),
                        CoordinatesHelper.toCoordinates("N50°33.71' E1°6.53'")
                ),
                AreaFactory.buildRectangular(
                        "UK-Channel", zoom, policy,
                        CoordinatesHelper.toCoordinates("N50°28.40' W6°28.74'"),
                        CoordinatesHelper.toCoordinates("N49°4.98' W2°3.31'")
                )
        ).generate();
    }

}
