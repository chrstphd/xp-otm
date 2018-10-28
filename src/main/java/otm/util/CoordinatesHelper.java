package otm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordinatesHelper {
    private static final Pattern pattern = Pattern.compile("([+-]?[0-9]{1,3})[° ]([0-9]{1,3})[\\. ']([0-9]{1,3})\\\"?");

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
}
