package com.bulka.java.games.jmine.engine.graphics.camera;

import com.bulka.java.games.jmine.engine.Engine;
import org.joml.Vector3f;

import java.util.logging.Logger;

public class Hero {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Vector3f position;
    private Vector3f rotation;
    private Camera camera;
    private float speed = 10f;
    private float verticalSpeed = 10f;
    private float rotSpeed = 0.2f;

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

        if(Engine.getEngine().getInputManager().isInWindow() && !Engine.getEngine().isShowCursor()) {
            if (Engine.getEngine().getInputManager().getMouseMovementX() != 0) {
                addRotation(0, (float) Engine.getEngine().getInputManager().getMouseMovementX() * rotSpeed, 0);
                changed = true;
            }
            if (Engine.getEngine().getInputManager().getMouseMovementY() != 0) {
                addRotation((float) Engine.getEngine().getInputManager().getMouseMovementY() * rotSpeed, 0, 0);
                changed = true;
            }
        }

        if(Engine.getEngine().getInputManager().isKeyDown(Engine.getEngine().getGame().getControls().forward)){
            addPosition(0, 0, -speed * deltaTime);
            changed = true;
        }
        if(Engine.getEngine().getInputManager().isKeyDown(Engine.getEngine().getGame().getControls().back)){
            addPosition(0, 0, speed * deltaTime);
            changed = true;
        }
        if(Engine.getEngine().getInputManager().isKeyDown(Engine.getEngine().getGame().getControls().left)){
            addPosition(-speed * deltaTime, 0, 0);
            changed = true;
        }
        if(Engine.getEngine().getInputManager().isKeyDown(Engine.getEngine().getGame().getControls().right)){
            addPosition(speed * deltaTime, 0, 0);
            changed = true;
        }
        if(Engine.getEngine().getInputManager().isKeyDown(Engine.getEngine().getGame().getControls().up)){
            addPosition(0, verticalSpeed * deltaTime, 0);
            changed = true;
        }
        if(Engine.getEngine().getInputManager().isKeyDown(Engine.getEngine().getGame().getControls().down)){
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
}
