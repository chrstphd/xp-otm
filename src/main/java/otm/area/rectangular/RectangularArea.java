package otm.area.rectangular;

import otm.area.Area;
import otm.tile.Tile;
import otm.util.Coordinates;
import otm.util.ProgressBar;

import java.nio.file.Path;
import java.text.MessageFormat;

/**
 * An RectangularArea is a surface defined by two Coordinates (NW, SE).
 * This RectangularArea is composed by a serie of Tile (1°x1°).
 */
public final class RectangularArea extends Area {
    private final Coordinates nw;
    private final Coordinates se;

    private Tile[][] tiles;

    public RectangularArea(Coordinates nw, Coordinates se) {
        this.nw = nw;
        this.se = se;
    }

    protected void doGenerate(Path outputFolderPath) throws Exception {
        final int nbOfVerticalTiles = difference(nw.getLat(), se.getLat()) + 1;
        final int nbOfHorizontalTiles = difference(nw.getLon(), se.getLon()) + 1;

        tiles = new Tile[nbOfVerticalTiles][nbOfHorizontalTiles];

        // init and prepare the tiles
        for (int v = 0; v < nbOfVerticalTiles; v++) {
            for (int h = 0; h < nbOfHorizontalTiles; h++) {
                Tile tile = new Tile(new Coordinates((nw.getLat()) - v, (nw.getLon()) + h), getZoom(), getPolicy());
                tiles[v][h] = tile;
                tile.prepare();
                System.out.print(tile + " - ");
            }
            System.out.println();
        }

        try (ProgressBar progress = new ProgressBar("Generating " + getName())) {
            final int nbOfTiles = tiles.length * tiles[0].length;
            ProgressBar.ProgressItem tileProgression = progress.createProgressItem("Tiles", nbOfTiles);
            int currentTileIndex = 0;
            for (int v = 0; v < tiles.length; v++) {
                for (int h = 0; h < tiles[0].length; h++) {
                    currentTileIndex++;
                    tileProgression.increment(MessageFormat.format("Tile #{0}/{1}: {2}", currentTileIndex, nbOfTiles, tiles[v][h].getTileName()));
                    tiles[v][h].generate(getName(), progress, outputFolderPath);
                }
            }
        }
    }

    private int difference(double smaller, double higher) {
        return (int) Math.abs((smaller + 1000) - (higher + 1000));
    }
}
