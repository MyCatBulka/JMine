package com.bulka.java.games.jmine.engine;

import java.util.logging.Logger;

public class FPSCounter {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    public static final int MILLIS = 500;
    public static final int IN_SECONDS = 1000 / MILLIS;
    private long fps;
    private long counter;
    private long time = System.currentTimeMillis();

    public void frame(){
        counter++;
        if(System.currentTimeMillis() - time >= MILLIS){
            fps = counter*IN_SECONDS;
            time = System.currentTimeMillis();
            counter = 0;
            Engine.getEngine().getWindow().setTitle(Engine.getEngine().getWindow().getBasicTitle() + "; FPS: " + fps);
//            System.out.println("FPS: " + fps);
        }
    }

    public long getFps() {
        return fps;
    }
}
