package com.bulka.java.games.jmine.engine.graphics.shaders;

import com.bulka.java.games.jmine.engine.utils.GameFileUtils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Shader {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private int vertexID = 0;
    private int fragmentID = 0;
    private int programID = 0;
    private boolean successful = false;

    public Shader(){

    }

    public void create(String vertexPath, String fragmentPath) {
        logger.config("Loading shaders: " + vertexPath + "; " + fragmentPath);
        String vertexCode = "";
        try {
            vertexCode = GameFileUtils.loadResourceAsString(vertexPath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "!!!Can`t load shader code: " + vertexPath, e);
            return;
        }
        String fragmentCode = "";
        try {
            fragmentCode = GameFileUtils.loadResourceAsString(fragmentPath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "!!!Can`t load shader code: " + fragmentPath, e);
            return;
        }
        logger.config("Loaded shaders code: " + vertexPath + "; " + fragmentPath);


        programID = GL20.glCreateProgram();
        vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexID, vertexCode);
        logger.config("Compiling shader: " + vertexPath);
        GL20.glCompileShader(vertexID);
        if(GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
            logger.severe("!!!Can`t compile shader: " + vertexPath + ". ERROR: " + GL20.glGetShaderInfoLog(vertexID));
            return;
        }
        logger.config("Successful compiled shader: " + vertexPath);

        fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentID, fragmentCode);
        logger.config("Compiling shader: " + fragmentPath);
        GL20.glCompileShader(fragmentID);
        if(GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
            logger.severe("!!!Can`t compile shader: " + fragmentPath + ". ERROR: " + GL20.glGetShaderInfoLog(fragmentID));
            return;
        }
        logger.config("Successful compiled shader: " + fragmentPath);

        GL20.glAttachShader(programID, vertexID);
        GL20.glAttachShader(programID, fragmentID);

        GL20.glLinkProgram(programID);
        if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE){
            logger.severe("!!!Can`t link program: " + vertexPath + ", " + fragmentPath + ". ERROR: " + GL20.glGetProgramInfoLog(programID));
            return;
        }
        GL20.glValidateProgram(programID);
        if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE){
            logger.severe("!!!Can`t validate program: " + vertexPath + ", " + fragmentPath + ". ERROR: " + GL20.glGetProgramInfoLog(programID));
            return;
        }

        successful = true;

        logger.info("Successful loaded shaders: " + vertexPath + "; " + fragmentPath);
    }

    public void bind(){
        if(successful)
            GL20.glUseProgram(programID);
    }
    public void unBind(){
        GL20.glUseProgram(0);
    }

    public int getUniformLocation(String name){
        if(successful)
            return GL20.glGetUniformLocation(programID, name);
        else return 0;
    }
    public void setUniform(String name, float value){
        if(successful)
            GL20.glUniform1f(getUniformLocation(name), value);
    }
    public void setUniform(String name, int value){
        if(successful)
            GL20.glUniform1i(getUniformLocation(name), value);
    }
    public void setUniform(String name, Vector2f value){
        if(successful)
            GL20.glUniform2f(getUniformLocation(name), value.x, value.y);
    }
    public void setUniform(String name, Vector3f value){
        if(successful)
            GL20.glUniform3f(getUniformLocation(name), value.x, value.y, value.z);
    }
    public void setUniform(String name, Vector4f value){
        if(successful)
            GL20.glUniform4f(getUniformLocation(name), value.x, value.y, value.z, value.w);
    }
    public void setUniform(String name, boolean value){
        if(successful)
            GL20.glUniform1i(getUniformLocation(name), value ? 1 : 0);
    }
    public void setUniform(String name, Matrix4f value){
        FloatBuffer buffer = MemoryUtil.memAllocFloat(16);
        value.get(buffer);
        if(successful)
            GL20.glUniformMatrix4fv(getUniformLocation(name), false, buffer);
    }

    public void destroy(){
        GL20.glDetachShader(programID, vertexID);
        GL20.glDetachShader(programID, fragmentID);
        GL20.glDeleteShader(vertexID);
        GL20.glDeleteShader(fragmentID);
        GL20.glDeleteProgram(programID);
    }

    public boolean isSuccessful() {
        return successful;
    }
}
