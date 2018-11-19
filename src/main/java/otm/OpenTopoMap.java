package otm;

import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;
import otm.util.ErrorManager;

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

    public static void main(String[] args) {

        try {
            AreaFactory.build("EGOV", "EGCW", 13, SubTilingPolicy.EIGHT_BY_EIGHT_SHARDS)
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
