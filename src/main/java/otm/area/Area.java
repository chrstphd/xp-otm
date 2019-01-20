package otm.area;

import otm.OpenTopoMap;
import otm.tile.Tile;
import otm.util.Coordinates;
import otm.util.ProgressBar;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

/**
 * An Area is a surface defined by two Coordinates (NW, SE).
 * This Area is composed by a serie of Tile (1°x1°).
 */
public class Area {

    private static final long MAX_SHARDS_TO_PROCESS = 50000L;

    private final AreaDescriptor descriptor;
    private Path areaWorkFolderPath;

    private Tile[][] tiles;
    private long nbOfShardsToProcess;

    private boolean alreadyGenerated = false;

    public Area(AreaDescriptor descriptor) {
        this.descriptor = descriptor;
        this.areaWorkFolderPath = Paths.get(OpenTopoMap.OTM_WORK_DIR.toFile().getAbsolutePath(), descriptor.getAreaFolderName());

        alreadyGenerated = areaWorkFolderPath.toFile().exists();
    }

    public void generate() throws Exception {
        if (alreadyGenerated) {
            System.out.println("abort area generation of " + descriptor.getName() + ": already generated for zoom/policy: " + descriptor.getZoom() + "/" + descriptor.getPolicy());
            return;
        }

        System.out.println("------------------");
        System.out.println("preparing area " + descriptor.getName());

        int nbOfVerticalTiles = difference(descriptor.getNW().getLat(), descriptor.getSE().getLat()) + 1;
        int nbOfHorizontalTiles = difference(descriptor.getNW().getLon(), descriptor.getSE().getLon()) + 1;

        System.out.println("area tiles: " + nbOfVerticalTiles + "x" + nbOfHorizontalTiles);

        tiles = new Tile[nbOfVerticalTiles][nbOfHorizontalTiles];

        for (int v = 0; v < nbOfVerticalTiles; v++) {
            for (int h = 0; h < nbOfHorizontalTiles; h++) {
                tiles[v][h] = new Tile(new Coordinates((descriptor.getNW().getLat()) - v, (descriptor.getNW().getLon()) + h), descriptor.getZoom(), descriptor.getPolicy());
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

        // security to avoid Earth mapping's OCD
        if (nbOfShardsToProcess >= MAX_SHARDS_TO_PROCESS) {
            throw new Exception("nb of shards to process (" + nbOfShardsToProcess + ") exceeds the limit (" + MAX_SHARDS_TO_PROCESS + "): please, split the coordinates coverage");
        }

        // create output folder if needed
        areaWorkFolderPath.toFile().mkdirs();

        try (ProgressBar progress = new ProgressBar("Generating " + descriptor.getName())) {
            final int nbOfTiles = tiles.length * tiles[0].length;
            ProgressBar.ProgressItem tileProgression = progress.createProgressItem("Tiles", nbOfTiles);
            int currentTileIndex = 0;
            for (int v = 0; v < tiles.length; v++) {
                for (int h = 0; h < tiles[0].length; h++) {
                    currentTileIndex++;
                    tileProgression.increment(MessageFormat.format("Tile #{0}/{1}: {2}", currentTileIndex, nbOfTiles, tiles[v][h].getTileName()));
                    tiles[v][h].generate(descriptor.getName(), progress, areaWorkFolderPath);
                }
            }
        }
    }

    private int difference(double smaller, double higher) {
        return (int) Math.abs((smaller + 1000) - (higher + 1000));
    }
}
