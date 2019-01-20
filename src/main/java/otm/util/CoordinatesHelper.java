package otm.util;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordinatesHelper {
    private static final Pattern pattern = Pattern.compile("([+-]?[0-9]{1,3})[° ]([0-9]{1,3})[\\. ']([0-9]{1,3})'?\\\"?");
    private static final double NEARLY_ZERO = .00001d;
    private static final double NEARLY_MAX = .99999d;

    public static Coordinates toCoordinates(String text) throws Exception {
        String[] split = text.split(" ");
        if (split.length != 2) {
            throw new Exception("unreadable coordinates: " + text);
        }

        return new Coordinates(
                toDecimal(split[0]),
                toDecimal(split[1])
        );
    }

    public static double toDecimal(String text) throws Exception {
        switch (text.charAt(0)) {
            case 'N':
            case 'E':
                text = text.substring(1);
                break;
            case 'S':
            case 'W':
                text = "-" + text.substring(1);
                break;
        }

        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()) {
            return toDecimal(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3))
            );
        } else {
            throw new Exception("unreadable coordinates: " + text);
        }
    }

    public static double toDecimal(int degrees, int minutes, double seconds) {
        return (degrees >= 0 ? 1 : -1) * (Math.abs(degrees) + (minutes / 60d) + (seconds / 3600d));
    }

    public static Coordinates toTileCoordinateUpperNW(Coordinates coordinates) {
        final int lat = (int) coordinates.getLat();
        final int lon = (int) coordinates.getLon();
        return new Coordinates(
                coordinates.getLat() >= 0 ? (lat + NEARLY_MAX) : (lat - NEARLY_ZERO),
                coordinates.getLon() >= 0 ? (lon + NEARLY_ZERO) : (lon - 1 + NEARLY_ZERO)
        );
    }

    public static Coordinates toTileCoordinateLowerSE(Coordinates coordinates) {
        final int lat = (int) coordinates.getLat();
        final int lon = (int) coordinates.getLon();
        return new Coordinates(
                coordinates.getLat() >= 0 ? (lat + NEARLY_ZERO) : (lat - 1 + NEARLY_ZERO),
                coordinates.getLon() >= 0 ? (lon + NEARLY_MAX) : (lon - NEARLY_ZERO)
        );
    }

    public static String toText(Coordinates coords) {
        return MessageFormat.format(
                "{0}{1,number,00}{2}{3,number,000}",
                (coords.getLat() >= 0 ? "N" : "S"),
                Math.abs((int) coords.getLat()),
                (coords.getLon() < 0 ? "W" : "E"),
                Math.abs((int) coords.getLon())
        );
    }
}
