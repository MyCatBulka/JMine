package com.bulka.java.games.jmine.engine.graphics.render;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.graphics.textures.FontTexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TextRendererGL {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public static final String charset = "()[]{}<>/\\|.,!@#$%^&*-+`:;№'\"_=?~0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯЇІЄабвгдеёжзийклмнопрстуфхцчшщъыьэюяїіє—«»Ґґ";
    private String fontName = "Arial";
    private Map<Integer, FontTexture> fontSizes;

    public void load() {
        fontSizes = new HashMap<>();
        FontTexture standard16 = new FontTexture();
        standard16.load(new Font(fontName, Font.PLAIN, 16));
        fontSizes.put(16, standard16);
    }

    public void render(String text, float x, float y, int size, Color color) {
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            renderLine(lines[i], x, y+size*i, size, color);
        }
    }
    public void renderLine(String text, float x, float y, int size, Color color) {
        FontTexture fontTexture = fontSizes.get(size);
        if(fontTexture == null){
            fontTexture = new FontTexture();
            fontTexture.load(new Font(fontName, Font.PLAIN, size));
            fontSizes.put(size, fontTexture);
        }
        char[] str = text.toCharArray();
        for (int i = 0; i < str.length; i++) {
            if(charset.indexOf(str[i]) == -1)
                str[i] = '?';
        }


        GL11.glBegin(GL11.GL_QUADS);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTexture.getTextureID());
        GL11.glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 255);
        int windowWidth = Engine.getEngine().getWindow().getWidth();
        int windowHeight = Engine.getEngine().getWindow().getHeight();
        for (char c : str) {
            int chNumber = charset.indexOf(c);
            float x1 = fontTexture.getCharFloatBounds()[chNumber * 4];
            float y1 = fontTexture.getCharFloatBounds()[chNumber * 4 + 1];
            float x2 = fontTexture.getCharFloatBounds()[chNumber * 4 + 2];
            float y2 = fontTexture.getCharFloatBounds()[chNumber * 4 + 3];

            float charWidth = fontTexture.getCharBounds()[chNumber * 4 + 2] - fontTexture.getCharBounds()[chNumber * 4];
            float charHeight = fontTexture.getCharBounds()[chNumber * 4 + 3] - fontTexture.getCharBounds()[chNumber * 4 + 1];

            float xPos = x;


            float xPosNorm = 2.0f * xPos / windowWidth - 1.0f;
            float yPosNorm = 1.0f - 2.0f * y / windowHeight;
            float xPosEndNorm = 2.0f * (xPos + charWidth) / windowWidth - 1.0f;
            float yPosEndNorm = 1.0f - 2.0f * (y + charHeight) / windowHeight;


            GL11.glTexCoord2f(x1, y2);
            GL11.glVertex2f(xPosNorm, yPosEndNorm);

            GL11.glTexCoord2f(x2, y2);
            GL11.glVertex2f(xPosEndNorm, yPosEndNorm);

            GL11.glTexCoord2f(x2, y1);
            GL11.glVertex2f(xPosEndNorm, yPosNorm);

            GL11.glTexCoord2f(x1, y1);
            GL11.glVertex2f(xPosNorm, yPosNorm);

//            GL11.glTexCoord2f(x1, y1);
//            GL11.glVertex2f(xPos, yPos);
//
//            GL11.glTexCoord2f(x2, y1);
//            GL11.glVertex2f(xPos + charWidth, yPos);
//
//            GL11.glTexCoord2f(x2, y2);
//            GL11.glVertex2f(xPos + charWidth, yPos + charHeight);
//
//            GL11.glTexCoord2f(x1, y2);
//            GL11.glVertex2f(xPos, yPos + charHeight);

            x += charWidth;
        }
        GL11.glEnd();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public Map<Integer, FontTexture> getFontSizes() {
        return fontSizes;
    }
    public FontTexture getFontTexture(int size){
        return fontSizes.get(size);
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public void destroy() {
    }
}
