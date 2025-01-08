package com.bulka.java.games.jmine.engine.graphics.shaders;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.ILogic;

import java.util.logging.Logger;

public class ShaderManager implements ILogic {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Shader testShader;
    private Shader textShader;

    public ShaderManager() {

    }

    public void load(){
        testShader = new Shader();
        testShader.create("/shaders/test_vertex.glsl", "/shaders/test_fragment.glsl");
        textShader = new Shader();
        textShader.create("/shaders/text_vertex.glsl", "/shaders/text_fragment.glsl");
    }

    public Shader getTextShader() {
        return textShader;
    }

    public Shader getTestShader() {
        return testShader;
    }

    @Override
    public void destroy(){
        testShader.destroy();
        textShader.destroy();
    }


    public static ShaderManager getSelf() {
        return Engine.getEngine().getShaderManager();
    }
}
