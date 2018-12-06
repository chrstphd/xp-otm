package otm.area;

import otm.tile.SubTilingPolicy;
import otm.tile.Tile;
import otm.util.Coordinates;
import otm.util.NameTool;
import otm.util.ProgressBar;

import java.text.MessageFormat;

/**
 * An Area is a surface defined by two Coordinates (NW, SE).
 * This Area is composed by a serie of Tile (1°x1°).
 */
public class Area {

    private static final long MAX_SHARDS_TO_PROCESS = 50000L;

    private final String areaName;

    private final Coordinates northWest;
    private final Coordinates southEast;
    private final int zoom;
    private final SubTilingPolicy subTilingPolicy;

    private Tile[][] tiles;
    private long nbOfShardsToProcess;

    protected Area(String areaName, Coordinates nw, Coordinates se, int zoom, SubTilingPolicy subTilingPolicy) {
        this.areaName = areaName;

        this.northWest = nw;
        this.southEast = se;
        this.zoom = zoom;
        this.subTilingPolicy = subTilingPolicy;

        System.out.println(nw);
        System.out.println(se);

        prepare();
    }

    /**
     * Prepare the processing.
     */
    private void prepare() {
        int nbOfVerticalTiles = Math.abs(Math.abs((int) northWest.getLat()) - Math.abs((int) southEast.getLat())) + 1;
        int nbOfHorizontalTiles = Math.abs(Math.abs((int) northWest.getLon()) - Math.abs((int) southEast.getLon())) + 1;

        System.out.println("area tiles: " + nbOfVerticalTiles + "x" + nbOfHorizontalTiles);

        tiles = new Tile[nbOfVerticalTiles][nbOfHorizontalTiles];

        for (int v = 0; v < nbOfVerticalTiles; v++) {
            for (int h = 0; h < nbOfHorizontalTiles; h++) {
                tiles[v][h] = new Tile(new Coordinates((northWest.getLat()) - v, (northWest.getLon()) + h), zoom, subTilingPolicy);
                System.out.print(tiles[v][h] + " - ");
            }
            System.out.println();
        }

        nbOfShardsToProcess = 0;
        for (int v = 0; v < nbOfVerticalTiles; v++) {
            for (int h = 0; h < nbOfHorizontalTiles; h++) {
                nbOfShardsToProcess += tiles[v][h].prepare();
            }
        }

        System.out.println("nb of shards to process: " + nbOfShardsToProcess);
    }

    public void generate() throws Exception {
        // security to avoid Earth mapping's OCD
        if (nbOfShardsToProcess >= MAX_SHARDS_TO_PROCESS) {
            throw new Exception("nb of shards to process (" + nbOfShardsToProcess + ") exceeds the limit (" + MAX_SHARDS_TO_PROCESS + "): please, split the coordinates coverage");
        }

        try (ProgressBar progress = new ProgressBar("Generating tiles")) {
            final int nbOfTiles = tiles.length * tiles[0].length;
            ProgressBar.ProgressItem tileProgression = progress.createProgressItem("Tiles", nbOfTiles);
            int currentTileIndex = 0;
            for (int v = 0; v < tiles.length; v++) {
                for (int h = 0; h < tiles[0].length; h++) {
                    currentTileIndex++;
                    tileProgression.increment(MessageFormat.format("Tile #{0}/{1}: {2}", currentTileIndex, nbOfTiles, tiles[v][h].getTileName()));
                    tiles[v][h].generate(areaName, progress);
                }
            }
        }
    }
}
