package otm.util;

import otm.shard.Shard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class WaterImage  {
    private static final BufferedImage image;

    static {
        image = new BufferedImage(Shard.SHARD_PIXEL_SIZE, Shard.SHARD_PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint ( new Color( 163, 221, 232) );
        graphics.fillRect ( 0, 0, image.getWidth(), image.getHeight() );
    }

    public static BufferedImage getBufferedImage() {
        return image;
    }
}
