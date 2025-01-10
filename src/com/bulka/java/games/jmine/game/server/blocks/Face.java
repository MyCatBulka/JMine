package com.bulka.java.games.jmine.game.server.blocks;

import com.bulka.java.games.jmine.engine.graphics.mesh.Vertex;

public class Face {
    public static final int[] INDICES = new int[]{
            0, 1, 2,
            2, 3, 0
    };
    public int textureIDX = 0;
    public int textureIDY = 0;
    public boolean isFull = true;
    public boolean invertedTexture = false;
    public Vertex[] face;


    public Face() {
    }

    public Face(boolean ifFull) {
        this.isFull = ifFull;
    }

    public Face(Vertex[] face) {
        this.face = face;
    }
    public Face(Vertex[] face, boolean ifFull) {
        this.face = face;
        this.isFull = ifFull;
    }
    public Face(int textureIDX, int textureIDY, Vertex[] face) {
        this.textureIDX = textureIDX;
        this.textureIDY = textureIDY;
        this.face = face;
    }

    public Face(int textureIDX, int textureIDY, Vertex[] face, boolean ifFull) {
        this.textureIDX = textureIDX;
        this.textureIDY = textureIDY;
        this.face = face;
        this.isFull = ifFull;
    }
    public Face(boolean ifFull, boolean invertedTexture) {
        this.isFull = ifFull;
        this.invertedTexture = invertedTexture;
    }

    public Face(Vertex[] face, boolean ifFull, boolean invertedTexture) {
        this.face = face;
        this.isFull = ifFull;
        this.invertedTexture = invertedTexture;
    }

    public Face(int textureIDX, int textureIDY, Vertex[] face, boolean ifFull, boolean invertedTexture) {
        this.textureIDX = textureIDX;
        this.textureIDY = textureIDY;
        this.face = face;
        this.isFull = ifFull;
        this.invertedTexture = invertedTexture;
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

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public boolean isInvertedTexture() {
        return invertedTexture;
    }

    public void setInvertedTexture(boolean invertedTexture) {
        this.invertedTexture = invertedTexture;
    }
}
