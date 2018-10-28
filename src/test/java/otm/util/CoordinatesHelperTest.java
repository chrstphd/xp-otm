package otm.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

class CoordinatesHelperTest {

    private static final Map<String, Double> inputs = new LinkedHashMap<>();

    @BeforeAll
    private static void setup() {
        inputs.put("N52°30.12", 52.50333333d);
        inputs.put("S52°30.12", -52.50333333d);
        inputs.put("E4°10.02", 4.16722222d);
        inputs.put("W4°10.02", -4.16722222d);
    }

    @Test
    void fromStringToDecimal() {
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

    private boolean isAround(double a, double b) {
        return Math.abs(a - b) <= 0.0001;
    }
}