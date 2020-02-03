package otm.util.image;

import otm.shard.Shard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ColorImages {
    private static final String DEFAULT_WATER = "default_water";
    private static final Map<String, BufferedImage> images = new HashMap<>();

    private static class ColorImagesHolder {
        static final ColorImages INSTANCE = new ColorImages();
    }

    private ColorImages() {
    }

    public static ColorImages getInstance() {
        return ColorImagesHolder.INSTANCE;
    }

    public BufferedImage getDefaultWaterImage() {

        BufferedImage image = images.get(DEFAULT_WATER);

        if (image == null) {
            image = getImage(DEFAULT_WATER, new Color(163, 221, 232), Shard.SHARD_PIXEL_SIZE, Shard.SHARD_PIXEL_SIZE);
            images.put(DEFAULT_WATER, image);
        }

        return image;
    }

    public BufferedImage getImage(String name, Color color, int width, int height) {
        BufferedImage image = images.get(name);

        // TODO replace by computeIfAbsent()
        if (image == null) {
            image = buildImage(color, width, height);
            images.put(name, image);
        }

        return image;
    }

    private BufferedImage buildImage(Color color, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(color);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        return image;
    }
}
