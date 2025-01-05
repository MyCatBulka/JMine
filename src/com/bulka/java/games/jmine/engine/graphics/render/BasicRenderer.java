package com.bulka.java.games.jmine.engine.graphics.render;

import com.bulka.java.games.jmine.engine.graphics.material.Material;
import com.bulka.java.games.jmine.engine.graphics.mesh.Mesh;
import com.bulka.java.games.jmine.engine.graphics.shaders.Shader;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.util.logging.Logger;

public class BasicRenderer {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void init(){

    }

    public void update(){

    }

    public void render(){

    }


    public void destroy(){

    }

    public static void renderMesh(Mesh mesh, Shader shader, Material material){
        GL30.glBindVertexArray(mesh.getVAO());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        if(mesh.getIBO() != 0) {
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIBO());
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL13.glBindTexture(GL13.GL_TEXTURE_2D, material.getTextureID());
            shader.bind();
            shader.setUniform("time", (float) GLFW.glfwGetTime());
            GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
            shader.unBind();
            GL13.glBindTexture(GL13.GL_TEXTURE_2D, 0);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        } else {
            shader.bind();
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, mesh.getVertices().length);
            shader.unBind();
        }


        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

}
