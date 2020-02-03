package otm.tile;

import otm.shard.Shard;

import java.text.MessageFormat;

public class TilesDescriptor {

    private final TileDescriptor[][] descriptors;

    public TilesDescriptor(Tile[][] tiles) {
        this.descriptors = new TileDescriptor[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                descriptors[i][j] = new TileDescriptor(tiles[i][j]);
            }
        }
    }

    public void describe() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < descriptors.length; i++) {
            addHorizontalSeparator(builder);
            describeNorthLine(i, builder);
            describeEastLine(i, builder);
            describeWestLine(i, builder);
            describeSouthLine(i, builder);
        }
        addHorizontalSeparator(builder);

        System.out.println(builder.toString());
    }

    private void addHorizontalSeparator(StringBuilder builder) {
        for (int i = 0; i < descriptors[0].length; i++) {
            builder.append("+----------------------------");
        }
        builder.append("+\n");
    }

    private void describeNorthLine(int line, StringBuilder builder) {
        for (int j = 0; j < descriptors[line].length; j++) {
            builder.append(MessageFormat.format("|     ^N: {0,number,#00.000000000}       ", descriptors[line][j].getNorth()));
        }
        builder.append("|\n");
    }

    private void describeEastLine(int line, StringBuilder builder) {
        for (int j = 0; j < descriptors[line].length; j++) {
            builder.append(MessageFormat.format("|           {0,number,#00.000000000} :E>", descriptors[line][j].getEast()));
        }
        builder.append("|\n");
    }

    private void describeSouthLine(int line, StringBuilder builder) {
        for (int j = 0; j < descriptors[line].length; j++) {
            builder.append(MessageFormat.format("|     vS: {0,number,#00.000000000}       ", descriptors[line][j].getSouth()));
        }
        builder.append("|\n");
    }

    private void describeWestLine(int line, StringBuilder builder) {
        for (int j = 0; j < descriptors[line].length; j++) {
            builder.append(MessageFormat.format("|<W: {0,number,#00.000000000}           ", descriptors[line][j].getWest()));
        }
        builder.append("|\n");
    }

    private final class TileDescriptor {
        private double north;
        private double east;
        private double south;
        private double west;

        public TileDescriptor(Tile tile) {
            Shard nw = tile.getShards()[0][0];
            this.north = nw.getNorth();
            this.west = nw.getWest();

            Shard se = tile.getShards()[tile.getShards().length - 1][tile.getShards()[0].length - 1];
            this.south = se.getSouth();
            this.east = se.getEast();
        }

        public double getNorth() {
            return north;
        }

        public double getEast() {
            return east;
        }

        public double getSouth() {
            return south;
        }

        public double getWest() {
            return west;
        }
    }
}
