package com.bulka.java.games.jmine.launcher;

import com.bulka.java.games.jmine.engine.Engine;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static Engine engine;
    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(com.bulka.java.libs.brul.Main.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        engine = new Engine();
        logger.info("Starting");
        Thread thread = new Thread(engine);
        thread.setName("Engine");
        thread.start();
    }

    public static Engine getEngine() {
        return engine;
    }
}