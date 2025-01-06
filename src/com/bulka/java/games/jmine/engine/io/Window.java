package com.bulka.java.games.jmine.engine.io;

import com.bulka.java.games.jmine.engine.Engine;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.logging.Logger;

public class Window {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private int width;
    private int height;
    private int originalWidth;
    private int originalHeight;
    private boolean isResized = true;
    private int screenWidth;
    private int screenHeight;
    private int xPos;
    private int yPos;
    private String basicTitle = "";
    private String title;
    private long window;
    private long monitor;
    private GLFWVidMode vidMode;
    private boolean fullScreenMode = false;
    private double aspect;
    private boolean v_sync = false;
    private Vector3f background = new Vector3f(0.6f, 0.9f, 1.0f); //SKY
    private Matrix4f projectionMatrix = new Matrix4f();
//    private Vector3f background = new Vector3f(0.0f, 0.0f, 0.0f);

    public void init() {
        width = Engine.getEngine().getSettingsManager().getInt("window.width", 640);
        height = Engine.getEngine().getSettingsManager().getInt("window.height", 640);
        basicTitle = Engine.getEngine().getLocalizationManager().get("game.title");
        title = basicTitle;
        
    }

    public void create() {
        monitor = GLFW.glfwGetPrimaryMonitor();
        vidMode = GLFW.glfwGetVideoMode(monitor);
        if (vidMode == null) {
            logger.warning("VidMode is null!!");
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            screenWidth = (int) screenSize.getWidth();
            screenHeight = (int) screenSize.getHeight();
        } else {
            screenWidth = vidMode.width();
            screenHeight = vidMode.height();
        }
        if (width <= 0)
            width = screenWidth / 2;
        if (height <= 0)
            height = screenHeight / 2;
        originalWidth = width;
        originalHeight = height;
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        window = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (window == 0) {
            logger.severe("Can`t create window!!! Terminating start");
            throw new IllegalStateException("Can`t create window (window is 0)");
        }
        aspect = (double) width / height;

        setWindowInCenter();
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GLFW.glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                setWidth(width);
                setHeight(height);
                isResized = true;
            }
        });

    }

    public void postInit(){
    }

    public void show(boolean val) {
        if (val)
            GLFW.glfwShowWindow(window);
        else
            GLFW.glfwHideWindow(window);
    }

    public void update() {
        if (isResized) {
            GL11.glViewport(0, 0, width, height);
            aspect = (double) width / height;
            updateProjectionMatrix();
            isResized = false;
        }
        if (windowShouldClose()) {
            logger.info("Window should close");
            Engine.getEngine().setRunning(false);
        }

    }

    public void updateProjectionMatrix(){
        Engine.getEngine().getGame().getHero().getCamera().updateProjectionMatrix();
    }

    public void clearBG(){
        GL11.glClearColor(background.x, background.y, background.z, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public void resizeWindow(int width, int height) {
        this.width = width;
        this.height = height;
        GLFW.glfwSetWindowSize(window, width, height);
    }

    public void setWindowInCenter() {
        xPos = (screenWidth - width) / 2;
        yPos = (screenHeight - height) / 2;
        GLFW.glfwSetWindowPos(window, xPos, yPos);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(int originalWidth) {
        this.originalWidth = originalWidth;
    }

    public int getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(int originalHeight) {
        this.originalHeight = originalHeight;
    }

    public String getTitle() {
        return title;
    }

    public String getBasicTitle() {
        return basicTitle;
    }

    public void setBasicTitle(String basicTitle) {
        this.basicTitle = basicTitle;
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
        this.title = title;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public boolean isFullScreenMode() {
        return fullScreenMode;
    }

    public boolean isV_sync() {
        return v_sync;
    }

    public void setFullScreenMode(boolean fullScreenMode) {
        this.fullScreenMode = fullScreenMode;
        if (fullScreenMode) {
            width = vidMode.width();
            height = vidMode.height();
            GLFW.glfwSetWindowMonitor(window, monitor, xPos, yPos, width, height, GLFW.GLFW_DONT_CARE);
        } else {
            width = originalWidth;
            height = originalHeight;
            GLFW.glfwSetWindowMonitor(window, 0, xPos, yPos, width, height, GLFW.GLFW_DONT_CARE);
        }
        isResized = true;
    }


    public void setV_sync(boolean v_sync) {
        this.v_sync = v_sync;
        if (v_sync)
            GLFW.glfwSwapInterval(1);
        else
            GLFW.glfwSwapInterval(0);
    }


    public double getAspect() {
        return aspect;
    }

    public void close() {
        GLFW.glfwDestroyWindow(window);
    }


    public void showCursor() {
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    }

    public void hideCursor() {
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
    }

    public void setCursorMode(boolean mode) {
        if (mode) {
            showCursor();
        } else {
            hideCursor();
        }
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public long getMonitor() {
        return monitor;
    }

    public GLFWVidMode getVidMode() {
        return vidMode;
    }

    public long getWindow() {
        return window;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector3f getBackground() {
        return background;
    }

    public void setBackground(Vector3f background) {
        this.background = background;
    }
    public void setBackground(float r, float g, float b) {
        background.x = r;
        background.y = g;
        background.z = b;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void destroy() {
        GLFW.glfwDestroyWindow(window);
    }
}
