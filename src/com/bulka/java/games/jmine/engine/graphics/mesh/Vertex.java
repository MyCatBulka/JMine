package com.bulka.java.games.jmine.engine.graphics.mesh;

import org.joml.Vector3f;

public class Vertex {
    private Vector3f position;

    public Vertex(Vector3f position) {
        this.position = position;
    }
    public Vertex(float x, float y, float z) {
        position = new Vector3f();
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public Vector3f getPosition() {
        return position;
    }
    public float getX(){
        return position.x;
    }
    public float getY(){
        return position.y;
    }
    public float getZ(){
        return position.z;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }
}
