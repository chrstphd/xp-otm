package otm;

import otm.airport.AirportNotFoundException;
import otm.tile.TileException;
import otm.tile.TileMatrix;
import otm.tile.TileMatrixFactory;
import otm.util.Coordinates;
import otm.util.CoordinatesHelper;

import java.nio.file.Path;
import java.nio.file.Paths;

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

    public static void main(String[] args){

//        // list of "one degree wide" tiles you want produce
//        final int[][] coords = new int[][]{
//                {53, -5}, {53, -4}, {53, -3},
//                {52, -5}, {52, -4}, {52, -3},
//                {47, 7}, {47, 8}, {47, 9},
//                {46, 7}, {46, 8}, {46, 9},
//                {45, 7}, {45, 8}, {45, 9}, {45, 10}
//        };
//
//        // start the generation
//        for (int i = 0; i < coords.length; i++) {
//            try {
//                TileMatrixFactory.build(new Coordinates(coords[i][0], coords[i][1]), 13).generate();
//            } catch (TileException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        for (int i = 47; i >= 45; i--) {
//            for (int j = 5; j <= 13; j++) {
//                try {
//                    TileMatrixFactory.build(new Coordinates(i, j), 13).generate();
//                } catch (TileException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        try {
            TileMatrixFactory.build("LFGJ", "LIPV", 13).generate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Now, you have to:");
        System.out.println("  - transform each PNG in TGA (no compressed, bottom-up),");
        System.out.println("  - copy each .map/.tga couple in the plugin's map directory");
    }

}
