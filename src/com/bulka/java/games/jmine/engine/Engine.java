package com.bulka.java.games.jmine.engine;


import com.bulka.java.games.jmine.engine.io.InputManager;
import com.bulka.java.games.jmine.engine.window.Window;
import com.bulka.java.games.jmine.game.Game;
import com.bulka.java.games.jmine.launcher.Main;
import com.bulka.java.games.jmine.localization.LocalizationManager;
import com.bulka.java.games.jmine.settings.SettingsManager;
import org.lwjgl.glfw.GLFW;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Engine {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private InputManager inputManager;
    private LocalizationManager localizationManager;
    private SettingsManager settingsManager;
    private Window window;
    private boolean showCursor = true;
    private FPSCounter fpsCounter;
    private Game game;
    private boolean running = false;


    public void start() {
        try {
            logger.info("Starting engine");
            logger.info("Adding logo in system tray");
            addInTray();

            logger.info("Initializing GLFW");
            if (!GLFW.glfwInit()) {
                logger.severe("Can`t initialize GLFW!! Terminating program");
                throw new RuntimeException("Can`t init GLFW ");
            }
            logger.info("Initialized GLFW");

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

            logger.info("Initializing Game");
            game = new Game();
            game.init();
            logger.info("Initialized Game");

            fpsCounter = new FPSCounter();

            logger.info("All is initialized");


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

    private void addInTray() {
        try {
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
            popupMenu.add(exitMenuItem);
            trayIcon.setPopupMenu(popupMenu);
            tray.add(trayIcon);

            logger.info("Added logo in system tray");
        } catch (Exception e) {
            logger.warning("Can`t add logo in system tray");
        }


    }

    public void loop() {
        while (running) {
            preUpdate();
            update();
            postUpdate();
            GLFW.glfwPollEvents();
            render();
            GLFW.glfwSwapBuffers(window.getWindow());
            fpsCounter.frame();
        }
        exit(0);
    }

    public void preUpdate() {
        game.preUpdate();
    }

    public void update() {
        inputManager.update();
        updateInput();
        window.update();
        game.update();

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
    }

    public void postUpdate() {
        game.postUpdate();
        inputManager.postUpdate();
    }

    public void render() {
        window.clearBG();
        game.render();
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
        if (inputManager != null)
            inputManager.destroy();
        if (settingsManager != null)
            settingsManager.destroy();
        if (localizationManager != null)
            localizationManager.destroy();
        if (game != null)
            game.destroy();
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
}
