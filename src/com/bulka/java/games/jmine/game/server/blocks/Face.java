package com.bulka.java.games.jmine.game.server.blocks;

import com.bulka.java.games.jmine.engine.graphics.mesh.Vertex;
import org.joml.GeometryUtils;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.text.NumberFormat;

public class Face {
    public static final int[] INDICES = new int[]{
            0, 1, 2,
            2, 3, 0
    };
    public int textureIDX = 0;
    public int textureIDY = 0;
    public boolean invertedTexture = false;
    public Vertex[] face;
    public float light = 1.0f;
    public Vector3i normal = new Vector3i();



    public Face() {
    }
    public Face(Vertex[] face) {
        this.face = face;
    }
    public Face(int textureIDX, int textureIDY, Vertex[] face) {
        this.textureIDX = textureIDX;
        this.textureIDY = textureIDY;
        this.face = face;
    }

    public Face(Vertex[] face, boolean invertedTexture, float light) {
        this.face = face;
        this.invertedTexture = invertedTexture;
        this.light = light;
    }
    public Face(Vertex[] face, boolean invertedTexture) {
        this.face = face;
        this.invertedTexture = invertedTexture;
    }

    public Face(int textureIDX, int textureIDY, Vertex[] face, boolean invertedTexture) {
        this.textureIDX = textureIDX;
        this.textureIDY = textureIDY;
        this.face = face;
        this.invertedTexture = invertedTexture;
    }

    public Face(int textureIDX, int textureIDY, boolean invertedTexture, Vertex[] face, float light) {
        this.textureIDX = textureIDX;
        this.textureIDY = textureIDY;
        this.invertedTexture = invertedTexture;
        this.face = face;
        this.light = light;
    }

    public void recalcTexture(){
        if(invertedTexture){
            face[0].setTextureCoords(0.015625f*(textureIDX), 0.015625f*(textureIDY+1));
            face[1].setTextureCoords(0.015625f*(textureIDX+1), 0.015625f*(textureIDY+1));
            face[2].setTextureCoords(0.015625f*(textureIDX+1), 0.015625f*(textureIDY));
            face[3].setTextureCoords(0.015625f*(textureIDX), 0.015625f*(textureIDY));
        } else {
            face[0].setTextureCoords(0.015625f * (textureIDX + 1), 0.015625f * (textureIDY));
            face[1].setTextureCoords(0.015625f * (textureIDX), 0.015625f * (textureIDY));
            face[2].setTextureCoords(0.015625f * (textureIDX), 0.015625f * (textureIDY + 1));
            face[3].setTextureCoords(0.015625f * (textureIDX + 1), 0.015625f * (textureIDY + 1));
        }
        Vector3f norm = new Vector3f();
        GeometryUtils.normal(face[0].getPosition(), face[1].getPosition(), face[2].getPosition(), norm);
        normal = new Vector3i((int) norm.x, (int) norm.y, (int) norm.z);
    }

    public Vertex[] getFace() {
        return face;
    }

    public void setFace(Vertex[] face) {
        this.face = face;
    }

    public int getTextureIDX() {
        return textureIDX;
    }

    public void setTextureIDX(int textureIDX) {
        this.textureIDX = textureIDX;
    }

    public int getTextureIDY() {
        return textureIDY;
    }

    public void setTextureIDY(int textureIDY) {
        this.textureIDY = textureIDY;
    }

    public boolean isInvertedTexture() {
        return invertedTexture;
    }

    public void setInvertedTexture(boolean invertedTexture) {
        this.invertedTexture = invertedTexture;
    }

    public float getLight() {
        return light;
    }

    public void setLight(float light) {
        this.light = light;
    }

    public Vector3i getNormal() {
        return normal;
    }
}
