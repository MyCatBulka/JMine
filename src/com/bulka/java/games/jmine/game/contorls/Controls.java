package com.bulka.java.games.jmine.game.contorls;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.settings.SettingsManager;
import org.lwjgl.glfw.GLFW;

public class Controls {
    public float mouseSensitivity = 0.2f;
    public int forward = GLFW.GLFW_KEY_W;
    public int back = GLFW.GLFW_KEY_S;
    public int left = GLFW.GLFW_KEY_A;
    public int right = GLFW.GLFW_KEY_D;
    public int up = GLFW.GLFW_KEY_SPACE;
    public int down = GLFW.GLFW_KEY_LEFT_SHIFT;
    public int devMenu = GLFW.GLFW_KEY_F3;

    public void load(){
        mouseSensitivity = SettingsManager.getSelf().getFloat("game.controls.mouse_sensitivity", 0.2f);
        forward = SettingsManager.getSelf().getInt("game.controls.forward", GLFW.GLFW_KEY_W);
        back = SettingsManager.getSelf().getInt("game.controls.back", GLFW.GLFW_KEY_S);
        left = SettingsManager.getSelf().getInt("game.controls.left", GLFW.GLFW_KEY_A);
        right = SettingsManager.getSelf().getInt("game.controls.right", GLFW.GLFW_KEY_D);
        up = SettingsManager.getSelf().getInt("game.controls.up", GLFW.GLFW_KEY_SPACE);
        down = SettingsManager.getSelf().getInt("game.controls.down", GLFW.GLFW_KEY_LEFT_SHIFT);
        devMenu = SettingsManager.getSelf().getInt("game.controls.dev_menu", GLFW.GLFW_KEY_F3);
    }


    public void destroy() {

    }

    public static Controls getSelf(){
        return Engine.getEngine().getGame().getControls();
    }
}
