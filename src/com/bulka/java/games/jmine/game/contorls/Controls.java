package com.bulka.java.games.jmine.game.contorls;

import com.bulka.java.games.jmine.engine.Engine;
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
        mouseSensitivity = Engine.getEngine().getSettingsManager().getFloat("game.controls.mouse_sensitivity", 0.2f);
        forward = Engine.getEngine().getSettingsManager().getInt("game.controls.forward", GLFW.GLFW_KEY_W);
        back = Engine.getEngine().getSettingsManager().getInt("game.controls.back", GLFW.GLFW_KEY_S);
        left = Engine.getEngine().getSettingsManager().getInt("game.controls.left", GLFW.GLFW_KEY_A);
        right = Engine.getEngine().getSettingsManager().getInt("game.controls.right", GLFW.GLFW_KEY_D);
        up = Engine.getEngine().getSettingsManager().getInt("game.controls.up", GLFW.GLFW_KEY_SPACE);
        down = Engine.getEngine().getSettingsManager().getInt("game.controls.down", GLFW.GLFW_KEY_LEFT_SHIFT);
        devMenu = Engine.getEngine().getSettingsManager().getInt("game.controls.dev_menu", GLFW.GLFW_KEY_F3);
    }


    public void destroy() {

    }
}
