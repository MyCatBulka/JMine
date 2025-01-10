package com.bulka.java.games.jmine.game;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.graphics.camera.Hero;
import com.bulka.java.games.jmine.engine.graphics.mesh.Mesh;
import com.bulka.java.games.jmine.engine.graphics.mesh.Vertex;
import com.bulka.java.games.jmine.engine.graphics.objects.GameObject;
import com.bulka.java.games.jmine.engine.graphics.textures.Textures;
import com.bulka.java.games.jmine.game.client.contorls.Controls;
import com.bulka.java.games.jmine.game.client.graphics.Crosshair;
import com.bulka.java.games.jmine.game.server.blocks.Blocks;
import com.bulka.java.games.jmine.game.server.level.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.logging.Logger;

public class Game {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private GameObject testGameObject;
    private Hero hero;
    private Controls controls;
    private DevMenu devMenu;
    private Crosshair crosshair;
    private Blocks blocks;
    private World world;


    public Game() {

    }

    public void init(){
        logger.info("Initializing DevMenu");
        devMenu = new DevMenu();
        devMenu.init();
        logger.info("Initialized DevMenu");

//        float size = 0.5f;
//        testGameObject = new GameObject(new Mesh(new Vertex[]{
//                new Vertex(-size, -size, 0, 0.0f, 1.0f),
//                new Vertex(size, -size, 0, 1.0f, 1.0f),
//                new Vertex(size, size, 0, 1.0f, 0.0f),
//                new Vertex(-size, size, 0, 0.0f, 0.0f),
//        }, new int[]{
//                2, 1, 0,
//                0, 3, 2
//        }), Textures.getSelf().getTexture("/textures/items/apple.png"));

//        testGameObject.setModelMatrix(new Matrix4f());
//        testGameObject.getModelMatrix().translate(new Vector3f(0, 0, 0));
//        testGameObject.postInit();
//        logger.info("Creating mesh");
//        testGameObject.getMesh().create();
//        logger.info("Created mesh");
        logger.info("Loading test material");
        logger.info("Loaded test material");

        logger.info("Loading contorls");
        controls = new Controls();
        controls.load();
        logger.info("Loaded contorls");
        logger.info("Initializing crosshair");
        crosshair = new Crosshair();
        crosshair.init();
        logger.info("Initialized crosshair");
        logger.info("Creating blocks");
        blocks = new Blocks();
        blocks.init();
        logger.info("Created blocks");


        logger.info("Initializing Hero");
        hero = new Hero(new Vector3f(0, 12, 0), new Vector3f(0, 0, 0));
        hero.init();
        logger.info("Initialized Hero");

        logger.info("Creating world");
        world = new World();
        world.init();
        logger.info("Created world");

    }

    public void postInit(){
        hero.postInit();
        devMenu.postInit();
        world.postInit();
   }

    public void preUpdate(){

    }

    public void update(){
        hero.update();
        world.update();

        devMenu.update();
    }

    public void postUpdate(){

    }

    public void render(){
//        testGameObject.render();
        world.render();
        hero.render();

        devMenu.render();
        crosshair.render();
   }

    public void destroy(){
//        testGameObject.destroy();
        world.destroy();
        devMenu.destroy();
        controls.destroy();
        crosshair.destroy();
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

    public DevMenu getDevMenu() {
        return devMenu;
    }

    public Blocks getBlocks() {
        return blocks;
    }

    public static Game getSelf(){
        return Engine.getEngine().getGame();
    }

    public World getWorld() {
        return world;
    }
}
