package com.bulka.java.games.jmine.engine.graphics.material;

import org.lwjgl.opengl.GL11;

import java.util.logging.Logger;

public class Material {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private int textureID;
    private int width, height;

    public Material() {

    }

    public void load(String texturePath){
        Texture texture = new Texture();
        texture.load(texturePath);
        textureID = texture.getTextureID();
        width = texture.getWidth();
        height = texture.getHeight();
    }

    public void destroy(){
        GL11.glDeleteTextures(textureID);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getTextureID() {
        return textureID;
    }
}
