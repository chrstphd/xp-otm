package otm.samples;

import otm.area.AreaFactory;
import otm.area.route.Route;
import otm.area.route.RouteArea;
import otm.tile.SubTilingPolicy;
import otm.util.Coordinates;
import otm.util.CoordinatesHelper;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class USNWMilIRVRRouteSample implements Sample {

    @Override
    public void generate(int zoom, SubTilingPolicy policy) throws Exception {
        Coordinates nw = CoordinatesHelper.toCoordinates("N48°59.99' W122°59.65'");
        Coordinates se = CoordinatesHelper.toCoordinates("N45°1.27' W116°32.15'");

        AreaFactory.buildRectangular(
                "US-NW-Mil-IRVR", zoom, policy,
                nw, se
        ).generate();


//        Set<Route> routes = new HashSet<>();
//
//        Route ir = new Route(Color.RED);
//        ir.addWaypoint(nw);
//        ir.addWaypoint(se);
//
//        routes.add(ir);
//
//        new RouteArea(nw, se, routes)
//                .setName("route")
//                .setZoom(zoom)
//                .setPolicy(policy)
//                .generate();
    }
}


