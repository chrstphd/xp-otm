package otm.tile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import otm.util.Coordinates;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TileMatrixTest {

    private final Coordinates nw = new Coordinates(50.0, -1);
    private final Coordinates se = new Coordinates(49.0, 0);

    @Test
    public void testInvertedCoords() {
        // TODO add tests to check both cases (lat, lon)
        Executable executable = () -> new TileMatrix(se, nw, 13, "test");
        assertThrows(TileException.class, executable);
    }

}