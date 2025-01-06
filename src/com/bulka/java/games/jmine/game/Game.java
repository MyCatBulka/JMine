package com.bulka.java.games.jmine.game;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.graphics.camera.Camera;
import com.bulka.java.games.jmine.engine.graphics.camera.Hero;
import com.bulka.java.games.jmine.engine.graphics.material.Material;
import com.bulka.java.games.jmine.engine.graphics.mesh.Mesh;
import com.bulka.java.games.jmine.engine.graphics.mesh.Vertex;
import com.bulka.java.games.jmine.engine.graphics.objects.GameObject;
import com.bulka.java.games.jmine.engine.graphics.render.BasicRenderer;
import com.bulka.java.games.jmine.engine.graphics.shaders.ShaderManager;
import com.bulka.java.games.jmine.game.contorls.Controls;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.logging.Logger;

public class Game {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private GameObject testGameObject;
    private Hero hero;
    private Controls controls;
    private DevMenu devMenu;


    public Game() {

    }

    public void init(){
        logger.info("Initializing DevMenu");
        devMenu = new DevMenu();
        devMenu.init();
        logger.info("Initialized DevMenu");

        float size = 0.5f;
        testGameObject = new GameObject(new Mesh(new Vertex[]{
                new Vertex(-size, -size, 0, 0.0f, 1.0f),
                new Vertex(size, -size, 0, 1.0f, 1.0f),
                new Vertex(size, size, 0, 1.0f, 0.0f),
                new Vertex(-size, size, 0, 0.0f, 0.0f),
        }, new int[]{
                0, 1, 2,
                2, 3, 0
        }), new Material());
        testGameObject.setModelMatrix(new Matrix4f());
        testGameObject.getModelMatrix().translate(new Vector3f(0, 0, 0));
        testGameObject.postInit();
        logger.info("Creating mesh");
        testGameObject.getMesh().create();
        logger.info("Created mesh");
        logger.info("Loading test material");
        testGameObject.getMaterial().load("/textures/items/apple.png");
        logger.info("Loaded test material");

        logger.info("Loading contorls");
        controls = new Controls();
        controls.load();
        logger.info("Loaded contorls");


        logger.info("Initializing Hero");
        hero = new Hero(new Vector3f(0, 0, 5), new Vector3f(0, 0, 0));
        hero.init();
        logger.info("Initialized Hero");

    }

    public void postInit(){
        hero.postInit();
        devMenu.postInit();
    }

    public void preUpdate(){

    }

    public void update(){
        hero.update();
        devMenu.update();
    }

    public void postUpdate(){

    }

    public void render(){
        testGameObject.render();
        hero.render();
        devMenu.render();
//        BasicRenderer.renderMesh(testGameObject.getMesh(), testGameObject.getShader(), testGameObject.getMaterial(), null);
    }

    public void destroy(){
        testGameObject.destroy();
        controls.destroy();
    }

    public Hero getHero() {
        return hero;
    }

    public GameObject getTestGameObject() {
        return testGameObject;
    }

    public Controls getControls() {
        return controls;
    }
}
