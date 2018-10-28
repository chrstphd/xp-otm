package otm.tile;

import otm.util.Coordinates;

import java.text.MessageFormat;

public class TileMatrixFactory {

    public static TileMatrix build(Coordinates nw, Coordinates se, int zoom, String name) throws TileException {
        if (nw.getLat() < se.getLat()) {
            throw new TileException(name + "> NW latitude [" + nw.getLat() + "] must be higher than SW latitude [" + se.getLat() + "]");
        }

        if (nw.getLon() > se.getLon()) {
            throw new TileException(name + "> NW longitude [" + nw.getLon() + "] must be smaller than SW longitude [" + se.getLon() + "]");
        }

        return new TileMatrix(nw, se, zoom, name);
    }

    /**
     * Comute the tile coordinates from the point
     *
     * @param point
     * @param zoom
     * @return
     */
    public static TileMatrix build(Coordinates point, int zoom) throws TileException {
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

        final String name = MessageFormat.format("{0}{1,number,00}{2}{3,number,000}", (lat >= 0 ? "N" : "S"), Math.abs(lat), (lon < 0 ? "W" : "E"), Math.abs(lon));

        return new TileMatrix(nw, se, zoom, name);
    }

//    protected TileMatrix(Coordinates coords, int zoom) throws TileException {
//        this(
//                (int) coords.getLat(),
//                (int) coords.getLon(),
//                zoom
//        );
//    }
//
//    protected TileMatrix(int latDegrees, int lonDegrees, int zoom) throws TileException {
//        this(
////                new Coordinates(CoordinatesHelper.toDecimal(latDegrees + 1, 0, 0), CoordinatesHelper.toDecimal(lonDegrees, 0, 0)),
//                new Coordinates(CoordinatesHelper.toDecimal(latDegrees, 59, 59.99999), CoordinatesHelper.toDecimal(lonDegrees, 0, 0)),
////                new Coordinates(CoordinatesHelper.toDecimal(latDegrees, 0, 0), CoordinatesHelper.toDecimal(lonDegrees + 1, 0, 0)),
//                new Coordinates(CoordinatesHelper.toDecimal(latDegrees, 0, 0), CoordinatesHelper.toDecimal(lonDegrees, 59, 59.99999)),
//                zoom,
//                MessageFormat.format("{0}{1,number,00}{2}{3,number,000}", (latDegrees >= 0 ? "N" : "S"), Math.abs(latDegrees), (lonDegrees < 0 ? "W" : "E"), Math.abs(lonDegrees))
//        );
//    }
}
