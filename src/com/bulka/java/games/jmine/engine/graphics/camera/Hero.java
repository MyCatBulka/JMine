package com.bulka.java.games.jmine.engine.graphics.camera;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.io.InputManager;
import com.bulka.java.games.jmine.game.client.contorls.Controls;
import com.bulka.java.games.jmine.game.server.level.world.WorldProvider;
import com.bulka.java.games.jmine.game.server.level.world.chunk.Chunk;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.logging.Logger;

public class Hero {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Vector3f position = new Vector3f();
    private Vector3d globalPosition = new Vector3d();
    private Vector3f rotation = new Vector3f();
    private Vector3f direction = new Vector3f();
    private Camera camera;
    private float speed = 100f;
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
        updateDirection();
    }

    public void update(){
        boolean changed = false;
        float deltaTime = (float) Engine.getEngine().getDeltaTime();

        if(InputManager.getSelf().isPressedButton(0)){
            if(WorldProvider.getSelf().getHintRenderer().isLookingAtBlock()) {
                if(WorldProvider.getSelf().getHintRenderer().getLookingAtBlockFace() != null) {
                    int x = (WorldProvider.getSelf().getHintRenderer().getLookingAtBlockCords().x);
                    int y = (WorldProvider.getSelf().getHintRenderer().getLookingAtBlockCords().y);
                    int z = (WorldProvider.getSelf().getHintRenderer().getLookingAtBlockCords().z);
    //                WorldProvider.getSelf().getWorld().setBlock((short) 0, (byte) 0, x, y, z);
    //                WorldProvider.getSelf().getWorld().getChunk((int) Math.floor((double) x/16),(int) Math.floor((double)z/16)).getSubChunk(y/16).updateMesh();
                    WorldProvider.getSelf().setBlockAndUpdateMeshes((short) 0, x, y, z);
                }
            }
        }
        if(InputManager.getSelf().isPressedButton(1)){
            if(WorldProvider.getSelf().getHintRenderer().isLookingAtBlock()) {
                if (WorldProvider.getSelf().getHintRenderer().getLookingAtBlockFace() != null) {
                    Vector3i normal = WorldProvider.getSelf().getHintRenderer().getLookingAtBlockFace().getNormal();
                    int x = (WorldProvider.getSelf().getHintRenderer().getLookingAtBlockCords().x + normal.x);
                    int y = (WorldProvider.getSelf().getHintRenderer().getLookingAtBlockCords().y + normal.y);
                    int z = (WorldProvider.getSelf().getHintRenderer().getLookingAtBlockCords().z + normal.z);
                    WorldProvider.getSelf().setBlockAndUpdateMeshes((short) 1, x, y, z);
                }
            }
        }

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
            moveInDirection(0, 0, -speed * deltaTime);
            changed = true;
        }
        if(InputManager.getSelf().isKeyDown(Controls.getSelf().back)){
            moveInDirection(0, 0, speed * deltaTime);
            changed = true;
        }
        if(InputManager.getSelf().isKeyDown(Controls.getSelf().left)){
            moveInDirection(-speed * deltaTime, 0, 0);
            changed = true;
        }
        if(InputManager.getSelf().isKeyDown(Controls.getSelf().right)){
            moveInDirection(speed * deltaTime, 0, 0);
            changed = true;
        }
        if(InputManager.getSelf().isKeyDown(Controls.getSelf().up)){
            moveInDirection(0, verticalSpeed * deltaTime, 0);
            changed = true;
        }
        if(InputManager.getSelf().isKeyDown(Controls.getSelf().down)){
            moveInDirection(0, -verticalSpeed * deltaTime, 0);
            changed = true;
        }



        if(changed){
            int moveChunksX = 0;
            int moveChunksZ = 0;
            if(position.x >= Chunk.WIDTH){
                position.x = position.x - Chunk.WIDTH;
                moveChunksX = 1;
            }
            else if(position.x < 0){
                position.x = position.x + Chunk.WIDTH;
                moveChunksX = -1;
            }
            else if(position.z >= Chunk.WIDTH){
                position.z = position.z - Chunk.WIDTH;
                moveChunksZ = 1;
            }
            else if(position.z < 0){
                position.z = position.z + Chunk.WIDTH;
                moveChunksZ = -1;
            }

            WorldProvider.getSelf().moveChunks(moveChunksX, moveChunksZ);
            camera.setPos(position);
            camera.setRot(rotation);
            camera.updateViewMatrix();
        }
    }

    public void moveInDirection(float x, float y, float z) {
        float dx = (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * z + (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
        float dz = (float) Math.cos(Math.toRadians(rotation.y)) * z + (float)Math.cos(Math.toRadians(rotation.y - 90)) * x;
        position.add(dx, y, dz);
        globalPosition.add(dx, y, dz);
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
        updateDirection();
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
        updateDirection();
    }

    public void updateDirection() {
        float pitch = (float) Math.toRadians(rotation.x);
        float yaw = (float) Math.toRadians(rotation.y);

        float dirX = (float) (Math.cos(pitch) * Math.sin(yaw));
        float dirY = (float) -Math.sin(pitch);
        float dirZ = (float) -(Math.cos(pitch) * Math.cos(yaw));

        direction.x = dirX;
        direction.y = dirY;
        direction.z = dirZ;
    }

    public Vector3f getDirection() {
        return direction;
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

    public Vector3d getGlobalPosition() {
        return globalPosition;
    }

    public void setGlobalPosition(Vector3d globalPosition) {
        this.globalPosition = globalPosition;
    }
}
