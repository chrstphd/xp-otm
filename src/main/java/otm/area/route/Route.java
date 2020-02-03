package otm.area.route;

import otm.util.Coordinates;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Route {
    private final Color color;
    private final List<Coordinates> waypoints = new ArrayList<>();

    public Route(Color color) {
        this.color = color;
    }

    public void addWaypoint(Coordinates coordinates) {
        waypoints.add(coordinates);
    }

    public List<Coordinates> getWaypoints() {
        return waypoints;
    }

    public Color getColor() {
        return color;
    }
}
