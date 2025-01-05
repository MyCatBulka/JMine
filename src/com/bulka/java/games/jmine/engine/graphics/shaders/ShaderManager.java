package com.bulka.java.games.jmine.engine.graphics.shaders;

import com.bulka.java.libs.brul.utils.FileUtils;

import java.util.logging.Logger;

public class ShaderManager {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Shader testShader;

    public ShaderManager() {

    }

    public void load(){
        testShader = new Shader();
        testShader.create("/shaders/test_vertex.glsl", "/shaders/test_fragment.glsl");
    }

    public void destroy(){
        testShader.destroy();
    }

    public Shader getTestShader() {
        return testShader;
    }
}
