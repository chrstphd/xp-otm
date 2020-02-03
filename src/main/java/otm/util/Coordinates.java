package otm.util;

public class Coordinates {
    // move all the coordinates into the first quadrant to avoid sign/quadrant calculation and eas the computation
    private static final int OFFSET = 1000;

    private final double lat;
    private final double lon;

    public Coordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getOffsetLat() {
        return lat + OFFSET;
    }

    public double getOffsetLon() {
        return lon + OFFSET;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
