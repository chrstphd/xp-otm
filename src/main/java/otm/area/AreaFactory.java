package otm.area;

import otm.airport.AirportNotFoundException;
import otm.airport.Airports;
import otm.tile.SubTilingPolicy;
import otm.tile.TileException;
import otm.util.Coordinates;
import otm.util.CoordinatesHelper;
import otm.util.NameTool;

public class AreaFactory {

    /**
     * Build the matrix based on the NW and SE corners, the zoom level and a name.
     *
     * @param nw   the NorthWest coordinates
     * @param se   the SouthEast coordinates
     * @param zoom the zoom level
     * @param name the name of the matrix
     * @return
     * @throws TileException
     */
    public static Area build(String name, Coordinates nw, Coordinates se, int zoom, SubTilingPolicy policy) throws TileException {
        if (nw.getLat() < se.getLat()) {
            throw new TileException(name + "> NW latitude [" + nw.getLat() + "] must be higher than SW latitude [" + se.getLat() + "]");
        }

        if (nw.getLon() > se.getLon()) {
            throw new TileException(name + "> NW longitude [" + nw.getLon() + "] must be smaller than SW longitude [" + se.getLon() + "]");
        }


        return new Area(
                new AreaDescriptor(
                        name,
                        CoordinatesHelper.toTileCoordinateUpperNW(nw),
                        CoordinatesHelper.toTileCoordinateLowerSE(se),
                        zoom, policy
                )
        );
    }

    /**
     * Compute the tile coordinates covering a dedicated point
     *
     * @param point the coordinate of a point
     * @param zoom
     * @return
     */
    public static Area build(Coordinates point, int zoom, SubTilingPolicy policy) throws TileException {
        final int lat = (int) point.getLat();
        final int lon = (int) point.getLon();

        final Coordinates nw = new Coordinates(
                lat >= 0 ? (lat + 1) : lat,
                lon >= 0 ? lon : (lon - 1)
        );

        final Coordinates se = new Coordinates(
                lat >= 0 ? lat : (lat - 1),
                lon >= 0 ? (lon + 1) : lon
        );

        final String name = NameTool.createNameFromCoordinates(point, zoom, policy);

        return build(name, nw, se, zoom, policy);
    }

    public static Area build(String name, String icaoNW, String icaoSE, int zoom, SubTilingPolicy policy) throws TileException, AirportNotFoundException {
        Coordinates nw = Airports.getAirportCoordinates(icaoNW);
        Coordinates se = Airports.getAirportCoordinates(icaoSE);

        return build(name, nw, se, zoom, policy);
    }
}
