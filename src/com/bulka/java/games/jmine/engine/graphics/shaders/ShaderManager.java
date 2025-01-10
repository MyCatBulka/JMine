package com.bulka.java.games.jmine.engine.graphics.shaders;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.ILogic;

import java.util.logging.Logger;

public class ShaderManager implements ILogic {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Shader Base3DShader;
    private Shader textShader;
    private Shader chunkShader;

    public ShaderManager() {

    }

    public void load(){
        Base3DShader = new Shader();
        Base3DShader.create("/shaders/3d/3dbase_vertex.glsl", "/shaders/3d/3dbase_fragment.glsl");
        textShader = new Shader();
        textShader.create("/shaders/2d/text_vertex.glsl", "/shaders/2d/text_fragment.glsl");
        chunkShader = new Shader();
        chunkShader.create("/shaders/3d/chunk/chunk_vertex.glsl", "/shaders/3d/chunk/chunk_fragment.glsl");
    }

    public Shader getTextShader() {
        return textShader;
    }

    public Shader getBase3DShader() {
        return Base3DShader;
    }

    public Shader getChunkShader() {
        return chunkShader;
    }

    @Override
    public void destroy(){
        Base3DShader.destroy();
        textShader.destroy();
        chunkShader.destroy();
    }


    public static ShaderManager getSelf() {
        return Engine.getEngine().getShaderManager();
    }
}
