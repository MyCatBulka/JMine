package com.bulka.java.games.jmine.engine.graphics.mesh;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vertex {
    private Vector3f position;
    private Vector2f textureCoords;

    public Vertex() {
    }

    public Vertex(Vector3f position, Vector2f textureCoords) {
        this.position = position;
        this.textureCoords = textureCoords;
    }

    public Vertex(Vector3f position) {
        this.position = position;
    }
    public Vertex(float x, float y, float z, float u, float v) {
        position = new Vector3f(x, y, z);
        textureCoords = new Vector2f(u, v);
    }
    public Vertex(float x, float y, float z) {
        position = new Vector3f(x, y, z);
        textureCoords = new Vector2f();
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector2f getTextureCoords() {
        return textureCoords;
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
    public float getU(){
        return textureCoords.x;
    }
    public float getV(){
        return textureCoords.y;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }
    public void setTextureCoords(Vector2f textureCoords) {
        this.textureCoords = textureCoords;
    }
    public void setTextureCoords(float u, float v) {
        this.textureCoords.x = u;
        this.textureCoords.y = v;
    }
}
