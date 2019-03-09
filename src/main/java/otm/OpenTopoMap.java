package otm;

import otm.samples.AlpsRectangularSample;
import otm.samples.CorsicaRectangularSample;
import otm.samples.EGLLSingleSample;
import otm.samples.MachLoopRectangularSample;
import otm.samples.NorthCascadesRectangularSample;
import otm.samples.PyreneesRectangularSample;
import otm.samples.Sample;
import otm.samples.Sardinia;
import otm.samples.SicilyRectangularSample;
import otm.samples.UKMultiSample;
import otm.tile.SubTilingPolicy;
import otm.util.ErrorManager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

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

        List<Sample> samples = Arrays.asList(
                new EGLLSingleSample(),
                new UKMultiSample(),
                new MachLoopRectangularSample(),
                new SicilyRectangularSample(),
                new NorthCascadesRectangularSample(),
                new CorsicaRectangularSample(),
                new Sardinia(),
                new PyreneesRectangularSample(),
                new AlpsRectangularSample()
        );

        try {
            for (Sample sample : samples) {
                sample.generate(zoom, policy);
            }
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
