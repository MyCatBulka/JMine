package com.bulka.java.games.jmine.game.client.graphics;

import com.bulka.java.games.jmine.engine.io.Window;
import com.bulka.java.games.jmine.settings.SettingsManager;
import org.lwjgl.opengl.GL11;

import java.util.logging.Logger;

public class Crosshair {
    private Logger logger = Logger.getLogger(Crosshair.class.getName());
    private float size = SettingsManager.getSelf().getFloat("game.graphics.crosshair_size", 0.008f);
    public void init(){

    }

    public void render(){
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glVertex3f(-size, 0.0f, -1.0f);
        GL11.glVertex3f(size, 0.0f, -1.0f);
        GL11.glVertex3f(0.0f, -(float) (size * Window.getSelf().getAspect()), -1.0f);
        GL11.glVertex3f(0.0f, (float) (size * Window.getSelf().getAspect()), -1.0f);
        GL11.glEnd();
    }

    public void destroy(){


    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
