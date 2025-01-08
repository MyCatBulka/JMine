package com.bulka.java.games.jmine.game;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.graphics.camera.Hero;
import com.bulka.java.games.jmine.engine.graphics.render.TextRendererGL;
import com.bulka.java.games.jmine.engine.io.InputManager;
import com.bulka.java.games.jmine.game.contorls.Controls;
import com.bulka.java.games.jmine.settings.SettingsManager;
import com.bulka.java.libs.brul.utils.LoremIpsumGenerator;
import com.bulka.java.libs.brul.utils.TextUtils;

import java.awt.*;
import java.util.logging.Logger;

public class DevMenu {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private String text = "";
    private Color color = Color.WHITE;
    private int fontSize = 14;
    private boolean show = false;

    public void init() {
        fontSize = (int) (14 * SettingsManager.getSelf().getFloat("game.graphics.ui.scale", 1f));
    }

    public void postInit() {
    }

    public void update() {
        if (InputManager.getSelf().isKeyTypedClicked(Controls.getSelf().devMenu))
            show = !show;
        if (show) {
            text = TextUtils.format("JMine $\nFPS: $\nHero: x:$; y:$; z:$, p:$; y:$", Engine.VERSION,
                    Engine.getEngine().getFPS(),
                    Hero.getSelf().getPosition().x, Hero.getSelf().getPosition().y, Hero.getSelf().getPosition().z, Hero.getSelf().getRotation().x, Hero.getSelf().getRotation().y
            );
        }
    }

    public void render() {
        if (show) {
            TextRendererGL.getSelf().render(text, 0, 0, fontSize, color);
        }
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

    public static DevMenu getSelf(){
        return Engine.getEngine().getGame().getDevMenu();
    }
}
