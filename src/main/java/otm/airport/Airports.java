package otm.airport;

import otm.util.Coordinates;

import java.io.FileReader;
import java.util.Properties;

public class Airports {
    private static Properties airports = new Properties();

    static {
        try (FileReader fr = new FileReader(ClassLoader.getSystemResource("apt.txt").getFile())) {
            airports.load(fr);
        } catch (Exception e) {
            throw new RuntimeException("unable to load apt.txt: " + e.getMessage(), e);
        }
    }

    public static Coordinates getAirportCoordinates(String apt) throws AirportNotFoundException {
        String coords = airports.getProperty(apt);

        if (coords != null) {
            String[] split = coords.split(" ");
            return new Coordinates(
                    Double.parseDouble(split[0]),
                    Double.parseDouble(split[1])
            );
        }

        throw new AirportNotFoundException("airport [" + apt + "] not found");
    }
}
