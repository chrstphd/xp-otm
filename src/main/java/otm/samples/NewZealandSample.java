package otm.samples;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

public class NewZealandSample implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildMulti("NZ_" + zoom, zoom, policy,
                AreaFactory.buildRectangular(
                        "North Island", zoom, policy,
                        CoordinatesHelper.toCoordinates("S34°0.54' E172°0.56'"),
                        CoordinatesHelper.toCoordinates("S41°55.05' E178°54.52'")
                ),
                AreaFactory.buildRectangular(
                        "South Island - A", zoom, policy,
                        CoordinatesHelper.toCoordinates("S40°24.68' E170°31.61'"),
                        CoordinatesHelper.toCoordinates("S43°57.23' E174°30.67'")
                ),
                AreaFactory.buildRectangular(
                        "South Island - B", zoom, policy,
                        CoordinatesHelper.toCoordinates("S43°55.33' E166°5.74'"),
                        CoordinatesHelper.toCoordinates("S47°32.99' E171°30.94'")
                ),
                AreaFactory.buildRectangular(
                        "South Islands - C", zoom, policy,
                        CoordinatesHelper.toCoordinates("S50°13.88' E165°12.92'"),
                        CoordinatesHelper.toCoordinates("S52°58.67' E169°54.61'")
                ),
                AreaFactory.buildRectangular(
                        "East Islands", zoom, policy,
                        CoordinatesHelper.toCoordinates("S43°16.34' W177°28.12'"),
                        CoordinatesHelper.toCoordinates("S44°27.63' W175°29.91'")
                )
        ).generate();
    }
}
