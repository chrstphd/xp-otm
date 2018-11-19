package otm.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

class CoordinatesHelperTest {

    @Test
    void fromStringToDecimal() {
        final Map<String, Double> inputs = new LinkedHashMap<>();
        inputs.put("N52°30.12", 52.50333333d);
        inputs.put("S52°30.12", -52.50333333d);
        inputs.put("E4°10.02", 4.16722222d);
        inputs.put("W4°10.02", -4.16722222d);

        for (String coords : inputs.keySet()) {
            try {
                double result = CoordinatesHelper.toDecimal(coords);
                Assertions.assertTrue(isAround(inputs.get(coords).doubleValue(), result), "computing " + coords);
            } catch (Exception e) {
                e.printStackTrace();
                assert false;
            }
        }
    }

    @Test
    void toTileCoordinateUpperNW() {
        // Q2 | Q1
        // ---+---
        // Q3 | Q4

        // Q1
        Coordinates q1 = CoordinatesHelper.toTileCoordinateUpperNW(new Coordinates(59.234, 5.123));
        Assertions.assertEquals(59.99999, q1.getLat(), "Q1 latitude");
        Assertions.assertEquals(5.0, q1.getLon(), "Q1 longitude");

        // Q2
        Coordinates q2 = CoordinatesHelper.toTileCoordinateUpperNW(new Coordinates(59.234, -5.123));
        Assertions.assertEquals(59.99999, q2.getLat(), "Q2 latitude");
        Assertions.assertEquals(-6.0, q2.getLon(), "Q2 longitude");

        // Q3
        Coordinates q3 = CoordinatesHelper.toTileCoordinateUpperNW(new Coordinates(-59.234, -5.123));
        Assertions.assertEquals(-59.0, q3.getLat(), "Q3 latitude");
        Assertions.assertEquals(-6.0, q3.getLon(), "Q3 longitude");

        // Q4
        Coordinates q4 = CoordinatesHelper.toTileCoordinateUpperNW(new Coordinates(-59.234, 5.123));
        Assertions.assertEquals(-59.0, q4.getLat(), "Q4 latitude");
        Assertions.assertEquals(5.0, q4.getLon(), "Q4 longitude");
    }

    @Test
    void toTileCoordinateLowerSE() {
        // Q2 | Q1
        // ---+---
        // Q3 | Q4

        // Q1
        Coordinates q1 = CoordinatesHelper.toTileCoordinateLowerSE(new Coordinates(59.234, 5.123));
        Assertions.assertEquals(59.0, q1.getLat(), "Q1 latitude");
        Assertions.assertEquals(5.99999, q1.getLon(), "Q1 longitude");

        // Q2
        Coordinates q2 = CoordinatesHelper.toTileCoordinateLowerSE(new Coordinates(59.234, -5.123));
        Assertions.assertEquals(59.0, q2.getLat(), "Q2 latitude");
        Assertions.assertEquals(-5.0, q2.getLon(), "Q2 longitude");

        // Q3
        Coordinates q3 = CoordinatesHelper.toTileCoordinateLowerSE(new Coordinates(-59.234, -5.123));
        Assertions.assertEquals(-60.0, q3.getLat(), "Q3 latitude");
        Assertions.assertEquals(-5.0, q3.getLon(), "Q3 longitude");

        // Q4
        Coordinates q4 = CoordinatesHelper.toTileCoordinateLowerSE(new Coordinates(-59.234, 5.123));
        Assertions.assertEquals(-60.0, q4.getLat(), "Q4 latitude");
        Assertions.assertEquals(5.99999, q4.getLon(), "Q4 longitude");
    }

    private boolean isAround(double a, double b) {
        return Math.abs(a - b) <= 0.0001;
    }
}