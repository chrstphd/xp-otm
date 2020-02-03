package otm.area.route;

import otm.area.Area;
import otm.tile.Tile;
import otm.util.Coordinates;

import java.awt.Color;
import java.nio.file.Path;
import java.util.Set;

public class RouteArea extends Area {
    private final Coordinates nw;
    private final Coordinates se;
    private final Set<Route> routes;

    private Tile[][] tiles;

    public RouteArea(Coordinates nw, Coordinates se, Set<Route> routes) {
        this.nw = nw;
        this.se = se;
        this.routes = routes;
    }

    @Override
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
            }
        }

        // draw each route into the image
        for (Route route : routes) {
            if (route.getWaypoints().size() > 1) {
                for (int i = 0; i < tiles.length; i++) {
                    for (int j = 0; j < tiles[i].length; j++) {
                        tiles[i][j].drawLine(route.getColor(), route);
                    }
                }
            }
        }
    }

    @Override
    public void describe() {
        System.out.println("no description");
    }

    @Override
    protected void doColourLayer(String layerName, Color color, Path outputFolderPath) throws RuntimeException {

    }

    // TODO duplicated code -> factorize
    private int difference(double smaller, double higher) {
        return (int) Math.abs((smaller + 1000) - (higher + 1000));
    }

    private double tile2lon(int x, int z) {
        return x / Math.pow(2.0, z) * 360.0 - 180;
    }

    private double tile2lat(int y, int z) {
        double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
        return Math.toDegrees(Math.atan(Math.sinh(n)));
    }
}
