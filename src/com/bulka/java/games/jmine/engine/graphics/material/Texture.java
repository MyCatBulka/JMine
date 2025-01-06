package com.bulka.java.games.jmine.engine.graphics.material;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Texture {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private int textureID;
    private int width;
    private int height;

    public static BufferedImage emptyTexture;
    public static void init(){
        int width = 16;
        int height = 16;

        emptyTexture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if ((x + y) % 2 == 0) {
                    emptyTexture.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    emptyTexture.setRGB(x, y, new Color(128, 0, 128).getRGB()); // Фиолетовый
                }
            }
        }
    }


    public Texture(){

    }

    public void load(String path){
        try {
            logger.config("Loading texture: " + path);
            BufferedImage image;
            try {
                image = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream(path)));
                logger.config("Successful loaded image, creating texture: " + path);
                load(image);
                logger.config("Successful created texture: " + path);
            } catch (Exception e) {
                logger.warning("Can`t load image: " + path);
                image = emptyTexture;
                load(image);
                logger.config("Successful created standard texture: " + path);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE,"!!Can`t load texture: " + path, e);
        }
    }

    public void load(BufferedImage image){
        width = image.getWidth();
        height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        ByteBuffer buffer = MemoryUtil.memAlloc(width*height*4);
        int pixel;
        byte red;
        byte green;
        byte blue;
        byte alpha;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixel = pixels[y * width + x];

                red = (byte) ((pixel >> 16) & 0xFF);
                green = (byte) ((pixel >> 8) & 0xFF);
                blue = (byte) (pixel & 0xFF);
                alpha = (byte) ((pixel >> 24) & 0xFF);

                buffer.put(red);
                buffer.put(green);
                buffer.put(blue);
                buffer.put(alpha);
            }
        }
        buffer.flip();
        textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTextureID() {
        return textureID;
    }

    public void destroy(){
        GL11.glDeleteTextures(textureID);
    }
}
