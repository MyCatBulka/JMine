package com.bulka.java.games.jmine.engine.graphics.textures;

import com.bulka.java.games.jmine.engine.graphics.render.TextRendererGL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FontTexture {
    private static Logger logger = Logger.getLogger(FontTexture.class.getName());
    private int textureID = 0;
    private int imageWidth = 0, imageHeight = 0;
    private int charHeight = 0;
    private FontMetrics fontMetrics;
    private int[] charWidths;
    private float[] charFloatBounds;

    public FontTexture() {
    }


    public void load(Font font) {
        try {
            logger.config("Loading font: " + font.getFontName() + " " + font.getSize());
            BufferedImage image = getBitMapImage(TextRendererGL.charset, font);
            int[] pixels = new int[imageWidth * imageHeight];
            image.getRGB(0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);
            ByteBuffer buffer = MemoryUtil.memAlloc(imageWidth * imageHeight);
            byte c;
            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < imageWidth; x++) {
                    c = (byte) ((pixels[y * imageWidth + x]) & 0xFF);
                    buffer.put(c);
                }
            }
            buffer.flip();
            textureID = GL11.glGenTextures();

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RED, imageWidth, imageHeight, 0, GL11.GL_RED, GL11.GL_UNSIGNED_BYTE, buffer);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

            logger.config("Loaded font: " + font.getFontName() + " " + font.getSize());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Can`t load font: " + font.getFontName() + " " + font.getSize(), e);
        }
    }

    public BufferedImage getBitMapImage(String text, Font font) {
        fontMetrics = getFontMetrics(font);
        BufferedImage[] images = new BufferedImage[text.length()];
        char[] str = text.toCharArray();
        char c;
        int width;
        charHeight = fontMetrics.getHeight();
        for (int i = 0; i < str.length; i++) {
            c = str[i];
            width = fontMetrics.charWidth(c);
            images[i] = new BufferedImage(width, charHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = images[i].createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2d.setFont(font);
            g2d.setColor(Color.WHITE);
            g2d.drawString(String.valueOf(c), 0, fontMetrics.getAscent());
            g2d.dispose();
        }

        int totalWidth = 0;

        for (BufferedImage img : images) {
            totalWidth += img.getWidth();
        }

//        imageWidth = (int) Math.ceil(Math.sqrt(totalWidth * charHeight));
//        imageHeight = (int) Math.ceil((double) totalWidth / imageWidth) * charHeight;
        imageWidth = (int) Math.pow(2, Math.ceil(Math.log(Math.sqrt(totalWidth * charHeight)) / Math.log(2)));
        imageHeight = (int) Math.pow(2, Math.ceil(Math.log((double) totalWidth / imageWidth * charHeight) / Math.log(2)));

        BufferedImage result = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();

        charWidths = new int[str.length];
        charFloatBounds = new float[str.length * 4];
        int x = 0, y = 0, i = 0;
        int nowWidth = 0;
        for (BufferedImage img : images) {
            nowWidth = img.getWidth();
            if (x + nowWidth > imageWidth) {
                y += charHeight;
                x = 0;
            }

            g2d.drawImage(img, x, y, null);

            charWidths[i] = nowWidth;

            charFloatBounds[i * 4] = (float) x / imageWidth;
            charFloatBounds[i * 4 + 1] = (float) y / imageHeight;
            charFloatBounds[i * 4 + 2] = (float) (x + nowWidth) / imageWidth;
            charFloatBounds[i * 4 + 3] = (float) (y + charHeight) / imageHeight;

            i++;

            x += img.getWidth();
        }
        g2d.dispose();

        return result;
    }

    public static FontMetrics getFontMetrics(Font font) {
        BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tempImage.createGraphics();
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics();
        g2d.dispose();
        return metrics;
    }

    public int getTextureID() {
        return textureID;
    }

    public void destroy() {
        GL11.glDeleteTextures(textureID);
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getCharHeight() {
        return charHeight;
    }

    public int[] getCharWidths() {
        return charWidths;
    }

    public float[] getCharFloatBounds() {
        return charFloatBounds;
    }

    public FontMetrics getFontMetrics() {
        return fontMetrics;
    }
}
