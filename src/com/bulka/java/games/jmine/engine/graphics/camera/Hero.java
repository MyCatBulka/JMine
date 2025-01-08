package com.bulka.java.games.jmine.engine.graphics.camera;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.io.InputManager;
import com.bulka.java.games.jmine.game.client.contorls.Controls;
import org.joml.Vector3f;

import java.util.logging.Logger;

public class Hero {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Vector3f position;
    private Vector3f rotation;
    private Camera camera;
    private float speed = 10f;
    private float verticalSpeed = 10f;

    public Hero() {

    }

    public Hero(Vector3f position) {
        this.position = position;
    }

    public Hero(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void init(){
        camera = new Camera();
        camera.init();
    }

    public void postInit(){
        camera.postInit();
        camera.setPos(position);
        camera.setRot(rotation);
        camera.updateProjectionMatrix();
        camera.updateViewMatrix();
    }

    public void update(){
        boolean changed = false;
        float deltaTime = (float) Engine.getEngine().getDeltaTime();

        if(InputManager.getSelf().isInWindow() && !Engine.getEngine().isShowCursor()) {
            if (InputManager.getSelf().getMouseMovementX() != 0) {
                addRotation(0, (float) InputManager.getSelf().getMouseMovementX() * Controls.getSelf().mouseSensitivity, 0);
                changed = true;
            }
            if (InputManager.getSelf().getMouseMovementY() != 0) {
                addRotation((float) InputManager.getSelf().getMouseMovementY() * Controls.getSelf().mouseSensitivity, 0, 0);
                changed = true;
            }
        }

        if(InputManager.getSelf().isKeyDown(Controls.getSelf().forward)){
            addPosition(0, 0, -speed * deltaTime);
            changed = true;
        }
        if(InputManager.getSelf().isKeyDown(Controls.getSelf().back)){
            addPosition(0, 0, speed * deltaTime);
            changed = true;
        }
        if(InputManager.getSelf().isKeyDown(Controls.getSelf().left)){
            addPosition(-speed * deltaTime, 0, 0);
            changed = true;
        }
        if(InputManager.getSelf().isKeyDown(Controls.getSelf().right)){
            addPosition(speed * deltaTime, 0, 0);
            changed = true;
        }
        if(InputManager.getSelf().isKeyDown(Controls.getSelf().up)){
            addPosition(0, verticalSpeed * deltaTime, 0);
            changed = true;
        }
        if(InputManager.getSelf().isKeyDown(Controls.getSelf().down)){
            addPosition(0, -verticalSpeed * deltaTime, 0);
            changed = true;
        }

        if(changed){
            camera.setPos(position);
            camera.setRot(rotation);
            camera.updateViewMatrix();
        }
    }

    public void addPosition(float x, float y, float z) {
        float dx = (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * z + (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
        float dz = (float) Math.cos(Math.toRadians(rotation.y)) * z + (float)Math.cos(Math.toRadians(rotation.y - 90)) * x;
        position.add(dx, y, dz);
    }
    public void setRotation(float x, float y, float z) {
        rotation.set(x, y, z);
        if(rotation.y <= -360)
            rotation.y = rotation.y % 360;
        if(rotation.y >= 360)
            rotation.y = rotation.y % 360;

        if(rotation.x < -90)
            rotation.x = -90;
        if(rotation.x > 90)
            rotation.x = 90;
    }
    public void addRotation(float x, float y, float z) {
        rotation.add(x, y, z);
        if(rotation.y <= -360)
            rotation.y = rotation.y % 360;
        if(rotation.y >= 360)
            rotation.y = rotation.y % 360;

        if(rotation.x < -90)
            rotation.x = -90;
        if(rotation.x > 90)
            rotation.x = 90;
    }

    public void render(){

    }

    public Camera getCamera() {
        return camera;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public float getVerticalSpeed() {
        return verticalSpeed;
    }

    public void setVerticalSpeed(float verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }

    public static Hero getSelf(){
        return Engine.getEngine().getGame().getHero();
    }
}
