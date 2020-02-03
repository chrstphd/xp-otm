package otm.samples;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;

import java.awt.Color;

public class Sardinia implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildRectangular(
                "Sardinia Magenta", zoom, policy,
                CoordinatesHelper.toCoordinates("N41°27.95' E8°3.31'"),
                CoordinatesHelper.toCoordinates("N38°35.36' E9°56.47'"))
//                .withColouredLayer("magenta", new Color(255, 0, 255, 50))
                .generate();

//        AreaFactory.buildRectangular(
//                "Sardinia TGA Compressed", zoom, policy,
//                CoordinatesHelper.toCoordinates("N41°27.95' E8°3.31'"),
//                CoordinatesHelper.toCoordinates("N38°35.36' E9°56.47'"))
////                .withColouredLayer("magenta", new Color(255, 0, 255, 50))
//                .generate();
//
//        AreaFactory.buildRectangular(
//                "Sardinia TGA Uncompressed", zoom, policy,
//                CoordinatesHelper.toCoordinates("N41°27.95' E8°3.31'"),
//                CoordinatesHelper.toCoordinates("N38°35.36' E9°56.47'"))
////                .withColouredLayer("magenta", new Color(255, 0, 255, 50))
//                .generate();
    }
}
