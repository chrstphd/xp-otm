package otm;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.Coordinates;
import otm.util.CoordinatesHelper;
import otm.util.ErrorManager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class OpenTopoMap {

    private static final Path OTM_HOME_DIR = Paths.get(System.getProperty("otm.dir", "h:/XPlaneComplements/OTM"));
    public static final Path OTM_CACHE_DIR = OTM_HOME_DIR.resolve("cache");
    public static final Path OTM_WORK_DIR = OTM_HOME_DIR.resolve("work");

    static {
        if (!OpenTopoMap.OTM_HOME_DIR.toFile().exists()) {
            if (!OpenTopoMap.OTM_HOME_DIR.toFile().mkdirs()) {
                throw new RuntimeException("unable to init home folder: " + OTM_HOME_DIR);
            }
        }

        if (!OpenTopoMap.OTM_CACHE_DIR.toFile().exists()) {
            if (!OpenTopoMap.OTM_CACHE_DIR.toFile().mkdirs()) {
                throw new RuntimeException("unable to init cache folder: " + OTM_CACHE_DIR);
            }
        }

        if (!OpenTopoMap.OTM_WORK_DIR.toFile().exists()) {
            if (!OpenTopoMap.OTM_WORK_DIR.toFile().mkdirs()) {
                throw new RuntimeException("unable to init work folder: " + OTM_WORK_DIR);
            }
        }
    }

    public static void main(String[] args) {

        // example to cover an area between EGOV and EGCW, zoom 13, byt images of 8x8 shards so 2048x2048 pixels.
//        try {
//            AreaFactory.build("EGOV", "EGCW", 13, SubTilingPolicy.EIGHT_BY_EIGHT_SHARDS)
//                    .generate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            ErrorManager.getInstance().dump(System.out);
//        }

        // example to cover an area between upper left and lower right coordinates
//        Arrays.asList(11).forEach(z->{
//            Arrays.asList(SubTilingPolicy.FOUR_BY_FOUR_SHARDS, SubTilingPolicy.EIGHT_BY_EIGHT_SHARDS).forEach( p-> {
//
//
//
//        try {
//            AreaFactory.build(
//                    new Coordinates(
//                            CoordinatesHelper.toDecimal("N38°27.69"),
//                            CoordinatesHelper.toDecimal("E12°31.77")
//                    ),
//                    new Coordinates(
//                            CoordinatesHelper.toDecimal("N37°31.52"),
//                            CoordinatesHelper.toDecimal("E13°58.12")
//                    ),
//                    z,
//                    "AroundPalermo",
//                    p)
//                    .generate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            ErrorManager.getInstance().dump(System.out);
//        }
//
//
//            });
//        });


        try {
            AreaFactory.build(
                    CoordinatesHelper.toCoordinates("N38°28.53' E11°32.20'"),
                    CoordinatesHelper.toCoordinates("N36°32.46' E15°56.87'"),
                    11,
                    "Sicilia",
                    SubTilingPolicy.FOUR_BY_FOUR_SHARDS)
                    .generate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ErrorManager.getInstance().dump(System.out);
        }




        System.out.println("Now, you have to:");
        System.out.println("  - transform each PNG in TGA (no compressed, bottom-up),");
        System.out.println("  - copy each .map/.tga couple in the plugin's map directory");
    }

}
