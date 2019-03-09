package otm.samples;

import otm.airport.Airports;
import otm.area.AreaFactory;
import otm.tile.SubTilingPolicy;

public class MachLoopRectangularSample implements Sample {
    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        AreaFactory.buildRectangular(
                "MachLoop", zoom, policy,
                Airports.getAirportCoordinates("EGOV"),
                Airports.getAirportCoordinates("EGCW")
        ).generate();
    }
}
