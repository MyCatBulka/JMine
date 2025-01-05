package com.bulka.java.games.jmine.game;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.graphics.material.Material;
import com.bulka.java.games.jmine.engine.graphics.mesh.Mesh;
import com.bulka.java.games.jmine.engine.graphics.mesh.Vertex;
import com.bulka.java.games.jmine.engine.graphics.render.BasicRenderer;
import com.bulka.java.games.jmine.engine.graphics.shaders.ShaderManager;

import java.util.logging.Logger;

public class Game {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Mesh testMesh;
    private Material testMaterial;


    public Game() {

    }

    public void init(){
        logger.info("Creating mesh");
        testMesh = new Mesh(new Vertex[]{
                new Vertex(-1f, 1f, 0.0f, 0.0f, 0.0f),
                new Vertex(1f, 1f, 0.0f, 1.0f, 0.0f),
                new Vertex(1f, -1f, 0.0f, 1.0f, 1.0f),
                new Vertex(-1f, -1f, 0.0f, 0.0f, 1.0f),
        }, new int[]{
                0, 1, 2,
                2, 3, 0
        });
        testMesh.create();
        logger.info("Created mesh");
        logger.info("Loading test material");
        testMaterial = new Material();
        testMaterial.load("/textures/items/apple.png");
        logger.info("Loaded test material");

    }

    public void preUpdate(){

    }

    public void update(){

    }

    public void postUpdate(){

    }

    public void render(){
        BasicRenderer.renderMesh(testMesh, Engine.getEngine().getShaderManager().getTestShader(), testMaterial);
    }

    public void destroy(){
        testMesh.destroy();
        testMaterial.destroy();
    }
}
