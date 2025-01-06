package com.bulka.java.games.jmine.game;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.game.contorls.Controls;
import com.bulka.java.libs.brul.utils.TextUtils;
import jdk.nashorn.internal.runtime.Version;
import org.joml.Vector2i;

import java.awt.*;
import java.util.Locale;
import java.util.logging.Logger;

public class DevMenu {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String text = "";
    private Color color = Color.BLACK;
    private float fontSize = 16f;
    private boolean show = false;

    public void init(){

    }
    public void postInit(){
//        lineHeight = Engine.getEngine().getTextRenderer().getTextBounds("Bulka", fontSize, "default").y;
    }

    public void update() {
        if(Engine.getEngine().getInputManager().isKeyTypedClicked(Engine.getEngine().getGame().getControls().devMenu))
            show = !show;
        if(show) {
            text = TextUtils.format("JMine $\nFPS: $\nHero: x:$; y:$; z:$, p:$; y:$", Engine.VERSION,
                    Engine.getEngine().getFPS(),
                    Engine.getEngine().getGame().getHero().getPosition().x, Engine.getEngine().getGame().getHero().getPosition().y, Engine.getEngine().getGame().getHero().getPosition().z, Engine.getEngine().getGame().getHero().getRotation().x, Engine.getEngine().getGame().getHero().getRotation().y
            );
        }
    }

    public void render(){
        if(show) {
            String[] lines = text.split("\n");
            for (int i = 0; i < lines.length; i++) {
                Engine.getEngine().getTextRenderer().render(lines[i], 0, (int) fontSize * (i + 1), fontSize, "default", color);
            }
        }
    }

    public void destroy(){

    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }
}
