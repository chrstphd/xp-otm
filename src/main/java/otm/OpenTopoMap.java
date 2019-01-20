package otm;

import otm.airport.Airports;
import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.CoordinatesHelper;
import otm.util.ErrorManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class OpenTopoMap {

    private static final Path OTM_HOME_DIR = Paths.get(System.getProperty("otm.dir", "h:/XPlaneComplements/OTM"));
    public static final Path OTM_CACHE_DIR = OTM_HOME_DIR.resolve("cache");
    public static final Path OTM_WORK_DIR = OTM_HOME_DIR.resolve("work");

    public static void main(String[] args) throws Exception {

        if (!OpenTopoMap.OTM_HOME_DIR.toFile().exists()) {
            if (!OpenTopoMap.OTM_HOME_DIR.toFile().mkdirs()) {
                throw new Exception("unable to init home folder: " + OTM_HOME_DIR);
            }
        }

        if (!OpenTopoMap.OTM_CACHE_DIR.toFile().exists()) {
            if (!OpenTopoMap.OTM_CACHE_DIR.toFile().mkdirs()) {
                throw new Exception("unable to init cache folder: " + OTM_CACHE_DIR);
            }
        }

        if (!OpenTopoMap.OTM_WORK_DIR.toFile().exists()) {
            if (!OpenTopoMap.OTM_WORK_DIR.toFile().mkdirs()) {
                throw new Exception("unable to init work folder: " + OTM_WORK_DIR);
            }
        }


        final int zoom = 11;
        final SubTilingPolicy policy = SubTilingPolicy.EIGHT_BY_EIGHT_SHARDS;

        try {

//            AreaFactory.build(
//                    Airports.getAirportCoordinates("EGLL"),
//                    zoom, SubTilingPolicy.NO_SUB_TILING
//            ).generate();
//
//            AreaFactory.build(
//                    "EGTR-EGKB",
//                    Airports.getAirportCoordinates("EGTR"),
//                    Airports.getAirportCoordinates("EGKB"),
//                    zoom, SubTilingPolicy.NO_SUB_TILING
//            ).generate();
//
            AreaFactory.build(
                    "UK-Shetland",
                    CoordinatesHelper.toCoordinates("N60°58.87' W2°27.56'"),
                    CoordinatesHelper.toCoordinates("N59°31.72' W0°32.86'"),
                    zoom, policy
            ).generate();

            AreaFactory.build(
                    "UK-Orkneys",
                    CoordinatesHelper.toCoordinates("N60°59.11' W3°27.25'"),
                    CoordinatesHelper.toCoordinates("N59°1.42' W2°1.85'"),
                    zoom, policy
            ).generate();


            AreaFactory.build(
                    "UK-North",
                    CoordinatesHelper.toCoordinates("N58°32.24' W7°55.73'"),
                    CoordinatesHelper.toCoordinates("N55°5.68' W1°9.02'"),
                    zoom, policy
            ).generate();

            AreaFactory.build(
                    "UK-South",
                    CoordinatesHelper.toCoordinates("N54°59.22' W5°28.67'"),
                    CoordinatesHelper.toCoordinates("N51°1.28' E1°57.16'"),
                    zoom, policy
            ).generate();

            AreaFactory.build(
                    "UK-South-2",
                    CoordinatesHelper.toCoordinates("N50°57.34' W5°22.82'"),
                    CoordinatesHelper.toCoordinates("N50°33.71' E1°6.53'"),
                    zoom, policy
            ).generate();

            AreaFactory.build(
                    "UK-Channel",
                    CoordinatesHelper.toCoordinates("N50°28.40' W6°28.74'"),
                    CoordinatesHelper.toCoordinates("N49°4.98' W2°3.31'"),
                    zoom, policy
            ).generate();
//
//            AreaFactory.build(
//                    "MachLoop",
//                    Airports.getAirportCoordinates("EGOV"),
//                    Airports.getAirportCoordinates("EGCW"),
//                    zoom, policy
//            ).generate();
//
//            AreaFactory.build(
//                    "Sicily",
//                    CoordinatesHelper.toCoordinates("N38°29.76' E11°30.18'"),
//                    CoordinatesHelper.toCoordinates("N36°30.67' E15°59.12'"),
//                    zoom, policy
//            ).generate();
//
//            AreaFactory.build(
//                    "North Cascades National Park",
//                    CoordinatesHelper.toCoordinates("N48°59.77' W122°59.62'"),
//                    CoordinatesHelper.toCoordinates("N48°1.33' W120°31.85'"),
//                    zoom, policy
//            ).generate();
//
//            AreaFactory.build(
//                    "Corsica",
//                    CoordinatesHelper.toCoordinates("N43°8.85' E8°10.34'"),
//                    CoordinatesHelper.toCoordinates("N41°18.39' E9°54.93'"),
//                    zoom, policy
//            ).generate();
//
//            AreaFactory.build(
//                    "Sardinia",
//                    CoordinatesHelper.toCoordinates("N41°27.95' E8°3.31'"),
//                    CoordinatesHelper.toCoordinates("N38°35.36' E9°56.47'"),
//                    zoom, policy
//            ).generate();
//
//            AreaFactory.build(
//                    "Pyrenees",
//                    CoordinatesHelper.toCoordinates("N43°29.12' W1°57.20'"),
//                    CoordinatesHelper.toCoordinates("N41°33.84' E3°26.24'"),
//                    zoom, policy
//            ).generate();
//
//            AreaFactory.build(
//                    "Alps",
//                    CoordinatesHelper.toCoordinates("N47°3.94' E5°54.33'"),
//                    CoordinatesHelper.toCoordinates("N45°37.18' E15°56.82'"),
//                    zoom, policy
//            ).generate();


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
