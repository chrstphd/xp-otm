package otm.area;

import otm.airport.AirportNotFoundException;
import otm.airport.Airports;
import otm.area.multi.MultiArea;
import otm.area.rectangular.RectangularArea;
import otm.tile.SubTilingPolicy;
import otm.tile.TileException;
import otm.util.Coordinates;
import otm.util.CoordinatesHelper;
import otm.util.NameTool;

import java.util.Arrays;

public class AreaFactory {

    /**
     * Build the matrix based on the NW and SE corners, the zoom level and a name.
     *
     * @param name the name of the matrix
     * @param zoom the zoom level
     * @param nw   the NorthWest coordinates
     * @param se   the SouthEast coordinates
     * @return
     * @throws TileException
     */
    public static Area buildRectangular(String name, int zoom, SubTilingPolicy policy, Coordinates nw, Coordinates se) throws TileException {
        if (nw.getLat() < se.getLat()) {
            throw new TileException(name + "> NW latitude [" + nw.getLat() + "] must be higher than SW latitude [" + se.getLat() + "]");
        }

        if (nw.getLon() > se.getLon()) {
            throw new TileException(name + "> NW longitude [" + nw.getLon() + "] must be smaller than SW longitude [" + se.getLon() + "]");
        }

        return new RectangularArea(
                CoordinatesHelper.toTileCoordinateUpperNW(nw),
                CoordinatesHelper.toTileCoordinateLowerSE(se))
                .setName(name)
                .setZoom(zoom)
                .setPolicy(policy);
    }

    /**
     * Compute the tile coordinates covering a dedicated point
     *
     * @param point the coordinate of a point
     * @param zoom
     * @return
     */
    public static Area buildSingle(Coordinates point, int zoom, SubTilingPolicy policy) throws TileException {
        final int lat = (int) point.getLat();
        final int lon = (int) point.getLon();

        final Coordinates nw = CoordinatesHelper.toTileCoordinateUpperNW(point);
        final Coordinates se = CoordinatesHelper.toTileCoordinateLowerSE(point);

        System.out.println("nw: " + nw);
        System.out.println("se: " + se);

        final String name = NameTool.createNameFromCoordinates(point, zoom, policy);

        return buildRectangular(name, zoom, policy, nw, se);
    }

    public static Area buildRectangular(String name, int zoom, SubTilingPolicy policy, String icaoNW, String icaoSE) throws TileException, AirportNotFoundException {
        Coordinates nw = Airports.getAirportCoordinates(icaoNW);
        Coordinates se = Airports.getAirportCoordinates(icaoSE);

        return buildRectangular(name, zoom, policy, nw, se);
    }

    public static Area buildMulti(String name, int zoom, SubTilingPolicy policy, Area... areas) {
        return new MultiArea(Arrays.asList(areas))
                .setName(name)
                .setZoom(zoom)
                .setPolicy(policy);
    }
}
