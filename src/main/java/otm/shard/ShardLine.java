package otm.shard;

import otm.util.Coordinates;

public class ShardLine {
    private static final int OFFSET = 1000;
    private final Shard shard;
    private final Coordinates a;
    private final Coordinates b;

    public ShardLine(Shard shard, Coordinates a, Coordinates b) {
        this.shard = shard;
        this.a = a;
        this.b = b;
    }

    public void draw() {
        // check if the route is related to this shard

        // minimum requirements:
        //    - a must be west of shard east
        //    - b must be east of shard west
        //    - a must be west of b
//        if (
//                a.getOffsetLon() < shard.get
//                        && b.getLon() + OFFSET > shard.getWest() + OFFSET
//                        && a.getLon() + OFFSET < b.getLon() + OFFSET
//        ) {
//            final double alpha = Math.atan((b.getLat() - a.getLat()) / (b.getLon() - a.getLon()));
//
//            // compute the pixels coordinates of coords a
//
//
//            // compute the pixels coordinates of coords b
//        }


    }


//    private ShardPixelCoordinates reduceWithinShard(Coordinates c) {
//        // get the longitude/x
//
//        if (inBetween(c.getLon(), shard.getWest(), shard.getEast())) {
//            return new c.getLon();
//        } else if (c.getLon() < shard.getWest()) {
//            double angle = Math.atan((b.getLat() - a.getLat()) / (b.getLon() - a.getLon()));
//
//        }
//    }

//    private int reduceLongitudeWithinShard(double lon) {
//
//
//        if (inBetween(lon, shard.getWest(), shard.getEast())) {
//
//
//            return new c.getLon();
//        } else if (c.getLon() < shard.getWest()) {
//            double angle = Math.atan((b.getLat() - a.getLat()) / (b.getLon() - a.getLon()));
//
//        }
//    }


    private boolean inBetween(double d, double left, double right) {
        return (((left + OFFSET) <= (d + OFFSET)) && ((d + OFFSET) <= (right + OFFSET)));
    }
//
//    private int convertDoubleToPixel(double lon, double min, double max) {
//        if (!inBetween(lon, min, max)) {
//            return -1;
//        }
//
//        double diff = (min + OFFSET) - (max + OFFSET);
//        double d = (Shard.SHARD_PIXEL_SIZE / diff) * (lon - min);
//
//        return Double.valueOf(d).intValue();
//    }
}
