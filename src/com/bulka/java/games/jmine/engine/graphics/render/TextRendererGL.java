package com.bulka.java.games.jmine.engine.graphics.render;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.graphics.shaders.Shader;
import com.bulka.java.games.jmine.engine.graphics.shaders.ShaderManager;
import com.bulka.java.games.jmine.engine.graphics.textures.FontTexture;
import com.bulka.java.games.jmine.engine.io.Window;
import com.bulka.java.games.jmine.settings.SettingsManager;
import org.joml.Vector4f;
import org.lwjgl.opengl.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TextRendererGL {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Shader textShader;

    public static final String charset = " ()[]{}<>/\\|.,!@#$%^&*-+`:;№'\"_=?~0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯЇІЄабвгдеёжзийклмнопрстуфхцчшщъыьэюяїіє—«»Ґґ";
    private Map<Integer, FontTexture> fontSizes;

    public void load() {
        fontSizes = new HashMap<>();
        textShader = ShaderManager.getSelf().getTextShader();
        getFontTexture(14);
    }

    public void render(String text, float x, float y, boolean normalizedCoords, boolean alignLeft, boolean alignTop, int size, Color color, Color bg) {
        FontTexture fontTexture = getFontTexture(size);
        int charHeight = fontTexture.getCharHeight();
        int[] charWidthArray = fontTexture.getCharWidths();
        float[] bounds = fontTexture.getCharFloatBounds();

        textShader.bind();
        Vector4f textColor = new Vector4f(
                color.getRed() / 255.0f,
                color.getGreen() / 255.0f,
                color.getBlue() / 255.0f,
                color.getAlpha() / 255.0f
        );
        Vector4f bgColor = new Vector4f(
                bg.getRed() / 255.0f,
                bg.getGreen() / 255.0f,
                bg.getBlue() / 255.0f,
                bg.getAlpha() / 255.0f
        );
        textShader.setUniform("textColor", textColor);
        textShader.setUniform("bgColor", bgColor);
//        textShader.setUniform("uiScale", SettingsManager.getSelf().getFloat("game.graphics.ui.scale", 1f));
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTexture.getTextureID());

        int maxVertices = text.length() * 16;
        int maxIndices = text.length() * 6;
        float[] vertices = new float[maxVertices];
        int[] indices = new int[maxIndices];

        int vertexIndex = 0, indexIndex = 0, globalIndex = 0;

        int windowWidth = Window.getSelf().getWidth();
        int windowHeight = Window.getSelf().getHeight();

        if (normalizedCoords) {
            x = (x + 1) / 2 * windowWidth;
            y = (1 - y) / 2 * windowHeight;
        }

        int textWidth = 0;
        int numLines = 0;
        for (int i = 0, lineWidth = 0; i <= text.length(); i++) {
            if (i == text.length() || text.charAt(i) == '\n') {
                textWidth = Math.max(textWidth, lineWidth);
                lineWidth = 0;
                numLines++;
            } else {
                char c = text.charAt(i);
                int chNumber = charset.indexOf(c);
                if (chNumber == -1) chNumber = charset.indexOf('?');
                lineWidth += charWidthArray[chNumber];
            }
        }
        int textHeight = numLines * charHeight;

        if (!alignLeft) {
            x = windowWidth - x - textWidth;
        }
        if (!alignTop) {
            y = windowHeight - y - textHeight;
        }

        int startY = Math.round(y);

        for (int i = 0, lineStart = 0; i <= text.length(); i++) {
            if (i == text.length() || text.charAt(i) == '\n') {
                // Вычисляем ширину текущей строки
                int lineWidth = 0;
                for (int j = lineStart; j < i; j++) {
                    char c = text.charAt(j);
                    int chNumber = charset.indexOf(c);
                    if (chNumber == -1) chNumber = charset.indexOf('?');
                    lineWidth += charWidthArray[chNumber];
                }

                // Корректируем начальную позицию X для выравнивания по правому краю
                int startX = Math.round(alignLeft ? x : x + (textWidth - lineWidth));

                for (int j = lineStart; j < i; j++) {
                    char c = text.charAt(j);
                    int chNumber = charset.indexOf(c);
                    if (chNumber == -1) chNumber = charset.indexOf('?');

                    float x1 = bounds[chNumber * 4];
                    float y1 = bounds[chNumber * 4 + 1];
                    float x2 = bounds[chNumber * 4 + 2];
                    float y2 = bounds[chNumber * 4 + 3];

                    int charWidth = charWidthArray[chNumber];

                    vertices[vertexIndex++] = startX;
                    vertices[vertexIndex++] = startY + charHeight;
                    vertices[vertexIndex++] = x1;
                    vertices[vertexIndex++] = y2;

                    vertices[vertexIndex++] = startX + charWidth;
                    vertices[vertexIndex++] = startY + charHeight;
                    vertices[vertexIndex++] = x2;
                    vertices[vertexIndex++] = y2;

                    vertices[vertexIndex++] = startX + charWidth;
                    vertices[vertexIndex++] = startY;
                    vertices[vertexIndex++] = x2;
                    vertices[vertexIndex++] = y1;

                    vertices[vertexIndex++] = startX;
                    vertices[vertexIndex++] = startY;
                    vertices[vertexIndex++] = x1;
                    vertices[vertexIndex++] = y1;

                    indices[indexIndex++] = globalIndex;
                    indices[indexIndex++] = globalIndex + 1;
                    indices[indexIndex++] = globalIndex + 2;
                    indices[indexIndex++] = globalIndex;
                    indices[indexIndex++] = globalIndex + 2;
                    indices[indexIndex++] = globalIndex + 3;

                    globalIndex += 4;
                    startX += charWidth;
                }
                lineStart = i + 1;
                startY += charHeight;
            }
        }

        int vao = GL30.glGenVertexArrays();
        int vbo = GL15.glGenBuffers();
        int ebo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        GL20.glEnableVertexAttribArray(1);

        GL11.glDrawElements(GL11.GL_TRIANGLES, indexIndex, GL11.GL_UNSIGNED_INT, 0);

        GL30.glBindVertexArray(0);
        GL15.glDeleteBuffers(vbo);
        GL15.glDeleteBuffers(ebo);
        GL30.glDeleteVertexArrays(vao);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        textShader.unBind();
    }


    public Map<Integer, FontTexture> getFontSizes() {
        return fontSizes;
    }

    public FontTexture getFontTexture(int fontSize) {
        FontTexture fontTexture = fontSizes.get(fontSize);
        if (fontTexture == null) {
            fontTexture = new FontTexture();
            fontTexture.load(new Font(SettingsManager.getSelf().get("game.graphics.ui.font", "Arial"), SettingsManager.getSelf().getInt("game.graphics.ui.font_type", Font.PLAIN), fontSize));
            fontSizes.put(fontSize, fontTexture);
        }
        return fontTexture;
    }

    public void destroy() {
    }

    public static TextRendererGL getSelf(){
        return Engine.getEngine().getTextRenderer();
    }
}
