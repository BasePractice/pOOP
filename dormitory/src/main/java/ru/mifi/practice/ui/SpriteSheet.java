package ru.mifi.practice.ui;

import java.awt.image.BufferedImage;

public final class SpriteSheet {
    final int width;
    final int height;
    final int[] pixels;

    public SpriteSheet(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();
        pixels = image.getRGB(0, 0, width, height, null, 0, width);
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = (pixels[i] & 0xff) / 64;
        }
    }
}
