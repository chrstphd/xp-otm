package otm.samples;

import otm.airport.Airports;
import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;

public class EGLLSingleSample implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildSingle(
                Airports.getAirportCoordinates("EGLL"),
                zoom, policy
        ).generate();
    }
}
