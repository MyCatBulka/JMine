package com.bulka.java.games.jmine.engine.graphics.render;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.graphics.textures.FontTexture;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TextRendererGL {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public static final String charset = " ()[]{}<>/\\|.,!@#$%^&*-+`:;№'\"_=?~0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯЇІЄабвгдеёжзийклмнопрстуфхцчшщъыьэюяїіє—«»Ґґ";
//    private String fontName = "Courier new";
    private String fontName = "Arial";
    private int fontType = Font.PLAIN;
    private Map<Integer, FontTexture> fontSizes;

    public void load() {
        fontSizes = new HashMap<>();
//        int size = 14;
//        FontTexture standard = new FontTexture();
//        standard.load(new Font(fontName, fontType, size));
//        fontSizes.put(size, standard);
    }

    public void render(String text, int x, int y, int size, Color color) {
        FontTexture fontTexture = getFontTexture(size);
        String[] lines = text.split("\n");
        for (String line : lines) {
            renderLine(line, x, y, fontTexture, color);
            y += fontTexture.getCharHeight();
        }
    }

    public void renderLine(String text, int x, int y, FontTexture fontTexture, Color color) {
        char[] str = text.toCharArray();
        for (int i = 0; i < str.length; i++) {
            if (charset.indexOf(str[i]) == -1)
                str[i] = '?';
        }

        Engine.getEngine().getShaderManager().getTextShader().bind();
        Engine.getEngine().getShaderManager().getTextShader().setUniform("textColor", new Vector4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 255));
//        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTexture.getTextureID());
        List<Float> vertices = new ArrayList<>();
        for (char c : str) {
            int chNumber = charset.indexOf(c);
            float x1 = fontTexture.getCharFloatBounds()[chNumber * 4];
            float y1 = fontTexture.getCharFloatBounds()[chNumber * 4 + 1];
            float x2 = fontTexture.getCharFloatBounds()[chNumber * 4 + 2];
            float y2 = fontTexture.getCharFloatBounds()[chNumber * 4 + 3];

            int charWidth = fontTexture.getCharWidths()[chNumber];
            int charHeight = fontTexture.getCharHeight();

            float xPos = x;
            float yPos = y;

            vertices.add(xPos);
            vertices.add(yPos + charHeight);
            vertices.add(x1);
            vertices.add(y2); // Верхний левый
            vertices.add(xPos + charWidth);
            vertices.add(yPos + charHeight);
            vertices.add(x2);
            vertices.add(y2); // Верхний правый
            vertices.add(xPos + charWidth);
            vertices.add(yPos);
            vertices.add(x2);
            vertices.add(y1); // Нижний правый

            vertices.add(xPos);
            vertices.add(yPos + charHeight);
            vertices.add(x1);
            vertices.add(y2); // Верхний левый
            vertices.add(xPos + charWidth);
            vertices.add(yPos);
            vertices.add(x2);
            vertices.add(y1); // Нижний правый
            vertices.add(xPos);
            vertices.add(yPos);
            vertices.add(x1);
            vertices.add(y1); // Нижний левый

            x += charWidth;
        }
        int vao = GL30.glGenVertexArrays();
        int vbo = GL15.glGenBuffers();
        GL30.glBindVertexArray(vao);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.size());
        for (float v : vertices) vertexBuffer.put(v);
        vertexBuffer.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.size() / 4);

        GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL15.glDeleteBuffers(vbo);
        GL30.glDeleteVertexArrays(vao);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        Engine.getEngine().getShaderManager().getTextShader().unBind();
    }

    public Map<Integer, FontTexture> getFontSizes() {
        return fontSizes;
    }

    public FontTexture getFontTexture(int fontSize) {
        FontTexture fontTexture = fontSizes.get(fontSize);
        if (fontTexture == null) {
            fontTexture = new FontTexture();
            fontTexture.load(new Font(fontName, fontType, fontSize));
            fontSizes.put(fontSize, fontTexture);
        }
        return fontTexture;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getFontType() {
        return fontType;
    }

    public void setFontType(int fontType) {
        this.fontType = fontType;
    }

    public void destroy() {
    }
}
