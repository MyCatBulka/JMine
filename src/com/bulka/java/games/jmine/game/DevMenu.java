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
    private int fontSize = 16;
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
//                Engine.getEngine().getTextRenderer().render(lines[i], 0, (int) fontSize * (i + 1), fontSize, color);
        if(show)
            Engine.getEngine().getTextRenderer().render(text, 0, 300, fontSize, color);
    }

    public void destroy(){

    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
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
