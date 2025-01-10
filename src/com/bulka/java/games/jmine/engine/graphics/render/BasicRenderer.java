package com.bulka.java.games.jmine.engine.graphics.render;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.graphics.objects.GameObject;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.util.logging.Logger;

public class BasicRenderer {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public void init(){

    }

    public void update(){

    }

    public void render(){

    }


    public void destroy(){

    }

    public static void renderSubChunk(){

    }

    public static void render(GameObject gameObject){
        GL30.glBindVertexArray(gameObject.getMesh().getVAO());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, gameObject.getMesh().getIBO());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL13.glBindTexture(GL13.GL_TEXTURE_2D, gameObject.getTexture());
        gameObject.getShader().bind();
        gameObject.getShader().setUniform("modelMat", gameObject.getModelMatrix());
        gameObject.getShader().setUniform("time", (float) GLFW.glfwGetTime());
        GL11.glDrawElements(GL11.GL_TRIANGLES, gameObject.getMesh().getIndices().length, GL11.GL_UNSIGNED_INT, 0);
        gameObject.getShader().unBind();
        GL13.glBindTexture(GL13.GL_TEXTURE_2D, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);


        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    public static BasicRenderer getSelf(){
        return Engine.getEngine().getBasicRenderer();
    }

}
