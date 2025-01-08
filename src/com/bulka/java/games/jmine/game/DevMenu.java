package com.bulka.java.games.jmine.game;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.graphics.camera.Hero;
import com.bulka.java.games.jmine.engine.graphics.render.TextRendererGL;
import com.bulka.java.games.jmine.engine.io.InputManager;
import com.bulka.java.games.jmine.game.client.contorls.Controls;
import com.bulka.java.games.jmine.settings.SettingsManager;
import com.bulka.java.libs.brul.utils.TextUtils;

import java.awt.*;
import java.util.Locale;
import java.util.logging.Logger;

public class DevMenu {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private String text = "";
    private Color color = new Color(255, 255, 255, 255);
    private Color bgColor = new Color(0, 0, 0, 127);
    private int fontSize = 14;
    private boolean show = false;
    private double deltaTime;
    private double updateTime;
    private double renderTime;

    public void init() {
        fontSize = (int) (14 * SettingsManager.getSelf().getFloat("game.graphics.ui.scale", 1f));
    }

    public void postInit() {
        updateValues();
    }

    public void update() {
        if (InputManager.getSelf().isKeyTypedClicked(Controls.getSelf().devMenu))
            show = !show;
        if (show) {
            text = String.format(Locale.US,
                    "JMine %s\nFPS: %s\nDelta time: %.4fms, Update: %.4fms, Render: %.4fms\nHero: x:%.3f; y:%.3f; z:%.3f, p:%.1f; y:%.1f",
                    Engine.VERSION,
                    Engine.getEngine().getFPS(),
                    deltaTime, updateTime, renderTime,
                    Hero.getSelf().getPosition().x, Hero.getSelf().getPosition().y, Hero.getSelf().getPosition().z, Hero.getSelf().getRotation().x, Hero.getSelf().getRotation().y

            );
        }
    }

    public void render() {
        if (show) {
            TextRendererGL.getSelf().render(text, 0, 0, fontSize, color, bgColor);
        }
    }

    public void updateValues(){
        deltaTime = Engine.getEngine().getDeltaTime()*1000;
        updateTime = Engine.getEngine().getUpdateTime()*1000;
        renderTime = Engine.getEngine().getRenderTime()*1000;
    }

    public void destroy() {

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

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public static DevMenu getSelf(){
        return Engine.getEngine().getGame().getDevMenu();
    }
}
