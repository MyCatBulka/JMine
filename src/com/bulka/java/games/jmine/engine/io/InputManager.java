package com.bulka.java.games.jmine.engine.io;

import com.bulka.java.games.jmine.engine.Engine;
import org.lwjgl.glfw.*;

import java.util.Arrays;

public class InputManager {
    private boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
    private boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];

    private boolean[] typedKeys = new boolean[GLFW.GLFW_KEY_LAST];
    private boolean[] releasedKeys = new boolean[GLFW.GLFW_KEY_LAST];

    private double mouseX = 0;
    private double mouseY = 0;
    private double previousMouseX = 0;
    private double previousMouseY = 0;
    private double mouseMovementX = 0;
    private double mouseMovementY = 0;
    private double scrollX = 0;
    private double scrollY = 0;

    private boolean isInWindow = false;

    private GLFWKeyCallback keyboard;
    private GLFWCursorPosCallback mouseMove;
    private GLFWMouseButtonCallback mouseButtons;
    private GLFWCursorEnterCallback enterCallback;
    private GLFWScrollCallback mouseScroll;

    public InputManager() {
        keyboard = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == GLFW.GLFW_PRESS) {
                    typedKeys[key] = true;
                    releasedKeys[key] = false;
                } else if (action == GLFW.GLFW_RELEASE) {
                    typedKeys[key] = false;
                    releasedKeys[key] = true;
                }
                keys[key] = (action != GLFW.GLFW_RELEASE);
            }
        };
        mouseMove = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mouseX = xpos;
                mouseY = ypos;
            }
        };
        mouseButtons = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                buttons[button] = (action != GLFW.GLFW_RELEASE);
            }
        };
        mouseScroll = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                scrollX = xoffset;
                scrollY = yoffset;
            }
        };
        enterCallback = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
                isInWindow = entered;
            }
        };
    }

    public void init() {
        GLFW.glfwSetKeyCallback(Engine.getEngine().getWindow().getWindow(), keyboard);
        GLFW.glfwSetCursorPosCallback(Engine.getEngine().getWindow().getWindow(), mouseMove);
        GLFW.glfwSetMouseButtonCallback(Engine.getEngine().getWindow().getWindow(), mouseButtons);
        GLFW.glfwSetScrollCallback(Engine.getEngine().getWindow().getWindow(), mouseScroll);
        GLFW.glfwSetCursorEnterCallback(Engine.getEngine().getWindow().getWindow(), enterCallback);
    }

    public void update() {
        mouseMovementX = mouseX - previousMouseX;
        mouseMovementY = mouseY - previousMouseY;
        if (isInWindow && !Engine.getEngine().isShowCursor())
            setCursorPosInCenter();
        previousMouseX = mouseX;
        previousMouseY = mouseY;
    }

    public void postUpdate() {
        Arrays.fill(typedKeys, false);
        Arrays.fill(releasedKeys, false);
    }

    public boolean isKeyDown(int key) {
        return keys[key];
    }

    public boolean isMouseButtonDown(int button) {
        return buttons[button];
    }


    public boolean isKeyTypedClicked(int key) {
        return typedKeys[key];
    }

    public boolean isKeyTypedReleased(int key) {
        return releasedKeys[key];
    }


    public void setCursorPos(int x, int y) {
        mouseX = x;
        mouseY = y;
        GLFW.glfwSetCursorPos(Engine.getEngine().getWindow().getWindow(), x, y);
    }

    public void setCursorPosInCenter() {
        setCursorPos(Engine.getEngine().getWindow().getWidth() / 2, Engine.getEngine().getWindow().getHeight() / 2);
    }

    public void destroy() {
        keyboard.free();
        mouseButtons.free();
        mouseMove.free();
        mouseScroll.free();
    }

    public double getPreviousMouseX() {
        return previousMouseX;
    }

    public double getPreviousMouseY() {
        return previousMouseY;
    }

    public double getMouseMovementX() {
        return mouseMovementX;
    }

    public double getMouseMovementY() {
        return mouseMovementY;
    }

    public boolean[] getKeys() {
        return keys;
    }

    public boolean[] getButtons() {
        return buttons;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public GLFWKeyCallback getKeyboard() {
        return keyboard;
    }

    public GLFWCursorPosCallback getMouseMove() {
        return mouseMove;
    }

    public GLFWMouseButtonCallback getMouseButtons() {
        return mouseButtons;
    }

    public boolean[] getTypedKeys() {
        return typedKeys;
    }

    public boolean[] getReleasedKeys() {
        return releasedKeys;
    }

    public double getScrollX() {
        return scrollX;
    }

    public double getScrollY() {
        return scrollY;
    }

    public GLFWScrollCallback getMouseScroll() {
        return mouseScroll;
    }

    public boolean isInWindow() {
        return isInWindow;
    }

    public GLFWCursorEnterCallback getEnterCallback() {
        return enterCallback;
    }


}
