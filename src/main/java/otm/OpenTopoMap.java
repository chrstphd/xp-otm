package otm;

import otm.samples.Sample;
import otm.samples.test.FaroeIslandsNorthRectangularTest;
import otm.tile.SubTilingPolicy;
import otm.util.ErrorManager;

import java.util.Arrays;
import java.util.List;

public class OpenTopoMap {

    public static void main(String[] args) throws Exception {
        final int zoom = 12;
        final SubTilingPolicy policy = SubTilingPolicy.EIGHT_BY_EIGHT_SHARDS;

        List<Sample> samples = Arrays.asList(
//                new EGLLSingleSample(),
//                new UKMultiSample(),
//                new MachLoopRectangularSample(),
//                new SicilyRectangularSample(),
//                new NorthCascadesRectangularSample(),
//                new CorsicaRectangularSample(),
//                new Sardinia(),
//                new PyreneesRectangularSample(),
//                new AlpsRectangularSample(),
//                new USNWMilIRVRRouteSample(),
//                new NewZealandSample(),
//                new GreeceMultiSample(),
//                new DjiboutiRectangularSample(),
//                new FaroeIslandsRectangularSample(),
//                new IndonesiaRectangularSample()
//                new NorwayRectangularSample()

                new FaroeIslandsNorthRectangularTest()
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

        System.out.println("Job done.");
    }
}
