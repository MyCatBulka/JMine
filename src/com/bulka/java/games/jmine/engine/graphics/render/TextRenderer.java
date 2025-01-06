package com.bulka.java.games.jmine.engine.graphics.render;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;

import java.awt.*;
import java.util.Arrays;
import java.util.logging.Logger;

public class TextRenderer {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private long vg;
    private String fontPath;
    private NVGColor color;

    public void load(){
        logger.info("Loading font: " + fontPath);
        try {
            vg = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS | NanoVGGL3.NVG_STENCIL_STROKES);
            if(vg == 0){
                throw new IllegalStateException("Can`t initialize NVG");
            }
            fontPath = getDefaultFontPath();
            long font = NanoVG.nvgCreateFont(vg, "default", fontPath);
            if(font == -1)
                logger.severe("Can`t load font: " + fontPath);
            else
                logger.info("Successful loaded font: " + fontPath);
            NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_TOP);
        } catch (Exception e) {
            throw new RuntimeException("Can`t load font: " + fontPath, e);
        }
        color = NVGColor.create();
    }


    public void render(String text, int x, int y, float fontSize, String fontFace, Vector4f color){
        NanoVG.nvgFontSize(vg, fontSize);
        NanoVG.nvgFontFace(vg, fontFace);
        this.color.r(color.x).g(color.y).b(color.z).a(color.w);
        NanoVG.nvgFillColor(vg, this.color);
        NanoVG.nvgText(vg, x, y, text);
    }
    public void render(String text, int x, int y, float fontSize, String fontFace, Color color){
        NanoVG.nvgFontSize(vg, fontSize);
        NanoVG.nvgFontFace(vg, fontFace);
        this.color.r(color.getRed()).g(color.getGreen()).b(color.getBlue()).a(color.getAlpha());
        NanoVG.nvgFillColor(vg, this.color);
        NanoVG.nvgText(vg, x, y, text);

    }
    public void setAlign(int align){
        NanoVG.nvgTextAlign(vg, align);
    }

    public Vector2i getTextBounds(String text, float fontSize, String fontFace){
        float[] bounds = new float[4];
        NanoVG.nvgFontSize(vg, fontSize);
        NanoVG.nvgFontFace(vg, fontFace);
        NanoVG.nvgTextBounds(vg,0, 0, text, bounds);
        return new Vector2i((int)(bounds[2] - bounds[0]), (int)(bounds[3] - bounds[1]));
    }

    private String getDefaultFontPath() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return "C:/Windows/Fonts/arial.ttf";
        } else if (osName.contains("mac")) {
            return "/System/Library/Fonts/Supplemental/Arial.ttf";
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf";
        } else {
            throw new UnsupportedOperationException("!!Operating System don`t support: " + osName);
        }
    }

    public void destroy(){
        NanoVGGL3.nvgDelete(vg);
    }

    public NVGColor getColor() {
        return color;
    }

    public String getFontPath() {
        return fontPath;
    }

    public long getVg() {
        return vg;
    }
}
