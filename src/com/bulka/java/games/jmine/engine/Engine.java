package com.bulka.java.games.jmine.engine;


import com.bulka.java.games.jmine.engine.graphics.textures.Texture;
import com.bulka.java.games.jmine.engine.graphics.render.BasicRenderer;
import com.bulka.java.games.jmine.engine.graphics.render.TextRendererGL;
import com.bulka.java.games.jmine.engine.graphics.shaders.ShaderManager;
import com.bulka.java.games.jmine.engine.io.InputManager;
import com.bulka.java.games.jmine.engine.utils.GLUtils;
import com.bulka.java.games.jmine.engine.io.Window;
import com.bulka.java.games.jmine.game.Game;
import com.bulka.java.games.jmine.launcher.Main;
import com.bulka.java.games.jmine.localization.LocalizationManager;
import com.bulka.java.games.jmine.settings.SettingsManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Engine {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public static final String VERSION = "0.0.1a";
    private InputManager inputManager;
    private LocalizationManager localizationManager;
    private SettingsManager settingsManager;
    private Window window;
    private Game game;
    private BasicRenderer basicRenderer;
    private ShaderManager shaderManager;
    private TextRendererGL textRenderer;

    private boolean showCursor = true;
    private boolean running = false;

    //FPS:
    private long FPS;
    private long FPSCounter;
    private long FPSTime;
    public static final int FPS_PERIOD = 500;
    public static final int FPS_CHANGING_IN_SECONDS = 1000 / FPS_PERIOD;
    private double deltaTime;
    private double updateTime;
    private double renderTime;



    public void start() {
        try {
            logger.info("Starting engine");

            Thread trayThread = new Thread(() -> addInTray(), "SystemTrayAddIcon");
            trayThread.start();

            logger.info("Initializing GLFW");
            if (!GLFW.glfwInit()) {
                logger.severe("Can`t initialize GLFW!! Terminating program");
                throw new RuntimeException("Can`t init GLFW ");
            }
            logger.info("Initialized GLFW");

            Texture.init();

            logger.info("Initializing Settings Manager");
            settingsManager = new SettingsManager();
            settingsManager.load();
            logger.info("Initialized Settings Manager");

            logger.info("Initializing Localization Manager");
            localizationManager = new LocalizationManager();
            localizationManager.load();
            logger.info("Using locale: " + localizationManager.getLocale().getDisplayName());
            logger.info(localizationManager.get("game.title"));
            logger.info("Initialized Localization Manager");

            logger.info("Initializing Window");
            window = new Window();
            window.init();
            logger.info("Creating Window");
            window.create();
            window.setV_sync(Boolean.parseBoolean(settingsManager.get("window.vsync")));
            window.setCursorMode(showCursor);
            logger.info("Initialized Window");
            logger.info("Successful created window");

            logger.info("Initializing Input Manager");
            inputManager = new InputManager();
            inputManager.init();
            logger.info("Initialized Input Manager");

            logger.info("Initializing ShaderManager and loading Shaders");
            shaderManager = new ShaderManager();
            shaderManager.load();
            logger.info("Initialized ShaderManager and loaded Shaders");

            logger.info("Initializing Game");
            game = new Game();
            game.init();
            logger.info("Initialized Game");


            logger.info("Initializing basic renderer");
            basicRenderer = new BasicRenderer();
            basicRenderer.init();
            logger.info("Initialized basic renderer");

            logger.info("Initializing text renderer");
            textRenderer = new TextRendererGL();
            textRenderer.load();
            logger.info("Initialized text renderer");


            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_STENCIL_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);


            logger.info("All is initialized, starting postInit");
            postInit();
            logger.info("Successful postInitialized");
            logger.info("GC");
            System.gc();
            logger.info("GC");

            logger.info("Showing window");
            window.show(true);
            running = true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Can`t start game ", e);
            destroy();
            throw e;
        }

        try {
            logger.info("Starting main game loop");
            loop();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Loop thrown an exception", e);
            destroy();
            throw e;
        }

    }

    private void postInit(){
        window.postInit();
        game.postInit();
    }

    private void addInTray() {
        try {
            logger.info("Adding logo in system tray");
            SystemTray tray = SystemTray.getSystemTray();
            Image image = ImageIO.read(Objects.requireNonNull(Engine.class.getResourceAsStream("/textures/items/apple.png")));
            TrayIcon trayIcon = new TrayIcon(image);
            trayIcon.setToolTip("JMine");

            PopupMenu popupMenu = new PopupMenu();
            popupMenu.setName("JMine");
            MenuItem exitMenuItem = new MenuItem("Exit");
            exitMenuItem.addActionListener(e -> {
                logger.info("Exiting by system tray");
                running = false;
            });
            MenuItem hardExitMenuItem = new MenuItem("HARD EXIT (Don`t recommended)");
            hardExitMenuItem.addActionListener(e -> {
                logger.info("Hard exiting by system tray");
                running = false;
                exit(0);
            });
            popupMenu.add(exitMenuItem);
            popupMenu.add(hardExitMenuItem);
            trayIcon.setPopupMenu(popupMenu);
            tray.add(trayIcon);

            logger.info("Added logo in system tray");
        } catch (Exception e) {
            logger.warning("Can`t add logo in system tray");
        }


    }

    public void loop() {
        long timeFrameStart;
        long timeUpdateStart;
        long timeRenderStart;
        while (running) {
            timeFrameStart = System.nanoTime();
            timeUpdateStart = System.nanoTime();
            preUpdate();
            update();
            postUpdate();
            GLFW.glfwPollEvents();
            updateTime = (double) (System.nanoTime() - timeUpdateStart) / 1_000_000_000;
            timeRenderStart = System.nanoTime();
            render();
            GLFW.glfwSwapBuffers(window.getWindow());
            renderTime = (double) (System.nanoTime() - timeRenderStart) / 1_000_000_000;
            printErrorsGL();

            FPSCounter++;

            deltaTime = (double) (System.nanoTime() - timeFrameStart) / 1_000_000_000;

            if (System.currentTimeMillis() - FPSTime >= FPS_PERIOD) {
                FPS = FPSCounter * FPS_CHANGING_IN_SECONDS;
                FPSTime = System.currentTimeMillis();
                FPSCounter = 0;
                window.setTitle(Engine.getEngine().getWindow().getBasicTitle() + "; FPS: " + FPS);
//                System.out.println(String.format(Locale.US, "FPS: %d; deltaTime: %f; updateTime: %f; renderTime: %f", FPS, deltaTime, updateTime, renderTime));
            }
        }
        exit(0);
    }

    public int checkErrorsGL(){
        int error = GL11.glGetError();
        if (error != GL11.GL_NO_ERROR)
            return error;
        else
            return 0;
    }

    public void printErrorsGL(){
        int error = checkErrorsGL();
        if(error != 0) {
            String errorMessage = GLUtils.getErrorMessage(error);
            logger.severe("Got OpenGL error " + error + ": " + errorMessage);
        }
    }

    public void preUpdate() {
        game.preUpdate();
    }

    public void update() {
        inputManager.update();
        updateInput();
        window.update();
        game.update();
        basicRenderer.update();

    }

    public void updateInput() {
        if (inputManager.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            logger.info("Escape is pressed, exiting");
            running = false;
        }
        if (inputManager.isKeyTypedClicked(GLFW.GLFW_KEY_F11)) {
            boolean fsm = !window.isFullScreenMode();
            logger.info("Changing full screen mode to " + fsm);
            window.setFullScreenMode(fsm);
        }
        if (inputManager.isKeyTypedClicked(GLFW.GLFW_KEY_TAB)) {
            showCursor = !showCursor;
            window.setCursorMode(showCursor);
        }
    }

    public void postUpdate() {
        game.postUpdate();
        inputManager.postUpdate();
    }

    public void render() {
        window.clearBG();


//        NanoVG.nvgBeginFrame(textRenderer.getVg(), window.getWidth(), window.getHeight(), 1.0f);
        game.render();
        basicRenderer.render();
//        NanoVG.nvgEndFrame(textRenderer.getVg());
    }

    public static Engine getEngine() {
        return Main.getEngine();
    }

    public Game getGame() {
        return game;
    }

    public void exit(int status) {
        destroy();
        System.exit(status);
    }

    public void destroy() {
        logger.info("Destroying");
        if(window != null)
            window.destroy();
        if (inputManager != null)
            inputManager.destroy();
        if (settingsManager != null)
            settingsManager.destroy();
        if (localizationManager != null)
            localizationManager.destroy();
        if (game != null)
            game.destroy();
        if (basicRenderer != null)
            basicRenderer.destroy();
        if (shaderManager != null)
            shaderManager.destroy();
        if (textRenderer != null)
            textRenderer.destroy();
        GLFW.glfwTerminate();
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public LocalizationManager getLocalizationManager() {
        return localizationManager;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public Window getWindow() {
        return window;
    }

    public boolean isShowCursor() {
        return showCursor;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public BasicRenderer getBasicRenderer() {
        return basicRenderer;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public double getUpdateTime() {
        return updateTime;
    }

    public double getRenderTime() {
        return renderTime;
    }

    public long getFPS() {
        return FPS;
    }

    public ShaderManager getShaderManager() {
        return shaderManager;
    }

    public TextRendererGL getTextRenderer() {
        return textRenderer;
    }
}
