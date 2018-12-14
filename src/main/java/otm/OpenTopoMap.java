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
        final SubTilingPolicy policy = SubTilingPolicy.FOUR_BY_FOUR_SHARDS;

        try {
            AreaFactory.build(
                    "MachLoop",
                    Airports.getAirportCoordinates("EGOV"),
                    Airports.getAirportCoordinates("EGCW"),
                    zoom, policy
            ).generate();

            AreaFactory.build(
                    "Sicily",
                    CoordinatesHelper.toCoordinates("N38°29.76' E11°30.18'"),
                    CoordinatesHelper.toCoordinates("N36°30.67' E15°59.12'"),
                    zoom, policy
            ).generate();

            AreaFactory.build(
                    "North Cascades National Park",
                    CoordinatesHelper.toCoordinates("N48°59.77' W122°59.62'"),
                    CoordinatesHelper.toCoordinates("N48°1.33' W120°31.85'"),
                    zoom, policy
            ).generate();

            AreaFactory.build(
                    "Corsica",
                    CoordinatesHelper.toCoordinates("N43°8.85' E8°10.34'"),
                    CoordinatesHelper.toCoordinates("N41°18.39' E9°54.93'"),
                    zoom, policy
            ).generate();

            AreaFactory.build(
                    "Sardinia",
                    CoordinatesHelper.toCoordinates("N41°27.95' E8°3.31'"),
                    CoordinatesHelper.toCoordinates("N38°35.36' E9°56.47'"),
                    zoom, policy
            ).generate();

            AreaFactory.build(
                    "Pyrenees",
                    CoordinatesHelper.toCoordinates("N43°29.12' W1°57.20'"),
                    CoordinatesHelper.toCoordinates("N41°33.84' E3°26.24'"),
                    zoom, policy
            ).generate();

            AreaFactory.build(
                    "Alps",
                    CoordinatesHelper.toCoordinates("N47°3.94' E5°54.33'"),
                    CoordinatesHelper.toCoordinates("N45°37.18' E15°56.82'"),
                    zoom, policy
            ).generate();


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
