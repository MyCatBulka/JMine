package com.bulka.java.games.jmine.game;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.graphics.camera.Hero;
import com.bulka.java.games.jmine.engine.graphics.render.TextRendererGL;
import com.bulka.java.games.jmine.engine.io.InputManager;
import com.bulka.java.games.jmine.engine.io.Window;
import com.bulka.java.games.jmine.game.client.contorls.Controls;
import com.bulka.java.games.jmine.settings.SettingsManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Locale;
import java.util.logging.Logger;

public class DevMenu {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private int textUpdatePeriod = 100; //Millis
    private long textUpdateperiodStart = System.currentTimeMillis();
    public static final String JAVA_VERSION = System.getProperty("java.version");
    public static final String RENDERER = GL11.glGetString(GL11.GL_RENDERER);
    public static final String VENDOR = GL11.glGetString(GL11.GL_VENDOR);
    private String textLeft = "";
    private String textRight = "";
    private Color color = new Color(255, 255, 255, 255);
    private Color bgColor = new Color(0, 0, 0, 127);
    private int fontSize = 14;
    private boolean show = false;
    private double deltaTime;
    private double updateTime;
    private double renderTime;
    private int allocated;
    private int used;
    private int free;
    private int maxMemory;

    public void init() {

    }

    public void postInit() {
        updateValues();
    }

    public void update() {
        if (InputManager.getSelf().isKeyTypedClicked(Controls.getSelf().devMenu))
            show = !show;
        if (show) {
            if (System.currentTimeMillis() - textUpdateperiodStart >= textUpdatePeriod) {
                textUpdateperiodStart = System.currentTimeMillis();
                updateValues();
                textLeft = String.format(Locale.US,
                        "JMine %s\nFPS: %s; Time: %.1fs\nDelta time: %.4fms, Update: %.4fms, Render: %.4fms\nXYZ:%.3f / %.3f / %.3f, p:%.1f; y:%.1f",
                        Engine.VERSION,
                        Engine.getEngine().getFPS(), GLFW.glfwGetTime(),
                        deltaTime, updateTime, renderTime,
                        Hero.getSelf().getPosition().x, Hero.getSelf().getPosition().y, Hero.getSelf().getPosition().z, Hero.getSelf().getRotation().x, Hero.getSelf().getRotation().y

                );
                textRight = String.format("Java %s %s bit\nMemory Allocated: %dMB; Used: %dMB; Free: %dMB; MAX: %dMB\n\nDisplay: %dx%d\n%s (%s)",
                        JAVA_VERSION, System.getProperty("os.arch"),
                        allocated, used, free, maxMemory,
                        Window.getSelf().getScreenWidth(), Window.getSelf().getScreenHeight(),
                        RENDERER, VENDOR
                );
            }
        }
    }

    public void render() {
        if (show) {
            TextRendererGL.getSelf().render(textLeft, 0f, 0f, false, true, true, fontSize, color, bgColor);
            TextRendererGL.getSelf().render(textRight, 0f, 0f, false, false, true, fontSize, color, bgColor);
        }
    }

    public void updateValues() {
        deltaTime = Engine.getEngine().getDeltaTime() * 1000;
        updateTime = Engine.getEngine().getUpdateTime() * 1000;
        renderTime = Engine.getEngine().getRenderTime() * 1000;
        allocated = (int) (Runtime.getRuntime().totalMemory() / 1048576);
        free = (int) (Runtime.getRuntime().freeMemory() / 1048576);
        maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1048576);
        used = allocated - free;
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

    public String getTextRight() {
        return textRight;
    }

    public String getTextLeft() {
        return textLeft;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public static DevMenu getSelf() {
        return Engine.getEngine().getGame().getDevMenu();
    }
}
