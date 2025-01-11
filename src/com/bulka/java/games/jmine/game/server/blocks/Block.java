package com.bulka.java.games.jmine.game.server.blocks;


import com.bulka.java.games.jmine.engine.graphics.mesh.Vertex;

public class Block {
    public int id = 0;
    public boolean hasAlfa = false;
    private float xMin, yMin, zMin;
    private float xMax, yMax, zMax;
    private boolean isFocusable = true;

    public Face back = new Face(new Vertex[]{
            new Vertex(0.0f, 1.0f, 0.0f, 0.0f, 0.0f),
            new Vertex(1.0f, 1.0f, 0.0f, 0.0f, 0.0f),
            new Vertex(1.0f, 0.0f, 0.0f, 0.0f, 0.0f),
            new Vertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
    }, false, 0.85f);

    public Face front = new Face(new Vertex[]{
            new Vertex(0.0f, 0.0f, 1.0f, 0.0f, 0.0f),
            new Vertex(1.0f, 0.0f, 1.0f, 0.0f, 0.0f),
            new Vertex(1.0f, 1.0f, 1.0f, 0.0f, 0.0f),
            new Vertex(0.0f, 1.0f, 1.0f, 0.0f, 0.0f),

    }, true, 0.85f);

    public Face left = new Face(new Vertex[]{
            new Vertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
            new Vertex(0.0f, 0.0f, 1.0f, 0.0f, 0.0f),
            new Vertex(0.0f, 1.0f, 1.0f, 0.0f, 0.0f),
            new Vertex(0.0f, 1.0f, 0.0f, 0.0f, 0.0f)
    }, true, 0.85f);

    public Face right = new Face(new Vertex[]{
            new Vertex(1.0f, 1.0f, 0.0f, 0.0f, 0.0f),
            new Vertex(1.0f, 1.0f, 1.0f, 0.0f, 0.0f),
            new Vertex(1.0f, 0.0f, 1.0f, 0.0f, 0.0f),
            new Vertex(1.0f, 0.0f, 0.0f, 0.0f, 0.0f)
    }, false, 0.85f);

    public Face bottom = new Face(new Vertex[]{
            new Vertex(0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
            new Vertex(1.0f, 0.0f, 0.0f, 0.0f, 0.0f),
            new Vertex(1.0f, 0.0f, 1.0f, 0.0f, 0.0f),
            new Vertex(0.0f, 0.0f, 1.0f, 0.0f, 0.0f)
    }, true, 0.75f);

    public Face top = new Face(new Vertex[]{
            new Vertex(0.0f, 1.0f, 1.0f, 0.0f, 0.0f),
            new Vertex(1.0f, 1.0f, 1.0f, 0.0f, 0.0f),
            new Vertex(1.0f, 1.0f, 0.0f, 0.0f, 0.0f),
            new Vertex(0.0f, 1.0f, 0.0f, 0.0f, 0.0f)
    }, false, 1.0f);

    public Face[] sides = {front, back, left, right, top, bottom};

    public Block() {
    }

    public Block(int id) {
        this.id = id;
    }

    public Block(int id, int textureIDX, int textureIDY) {
        this.id = id;
        front.setTextureIDX(textureIDX);
        front.setTextureIDY(textureIDY);
        back.setTextureIDX(textureIDX);
        back.setTextureIDY(textureIDY);
        left.setTextureIDX(textureIDX);
        left.setTextureIDY(textureIDY);
        right.setTextureIDX(textureIDX);
        right.setTextureIDY(textureIDY);
        top.setTextureIDX(textureIDX);
        top.setTextureIDY(textureIDY);
        bottom.setTextureIDX(textureIDX);
        bottom.setTextureIDY(textureIDY);
        recalcFaces();
    }

    public Block(int id,
            int frontTextureIDX, int frontTextureIDY,
            int backTextureIDX, int backTextureIDY,
            int leftTextureIDX, int leftTextureIDY,
            int rightTextureIDX, int rightTextureIDY,
            int topTextureIDX, int topTextureIDY,
            int bottomTextureIDX, int bottomTextureIDY) {
        this.id = id;
        front.setTextureIDX(frontTextureIDX);
        front.setTextureIDY(frontTextureIDY);
        back.setTextureIDX(backTextureIDX);
        back.setTextureIDY(backTextureIDY);
        left.setTextureIDX(leftTextureIDX);
        left.setTextureIDY(leftTextureIDY);
        right.setTextureIDX(rightTextureIDX);
        right.setTextureIDY(rightTextureIDY);
        top.setTextureIDX(topTextureIDX);
        top.setTextureIDY(topTextureIDY);
        bottom.setTextureIDX(bottomTextureIDX);
        bottom.setTextureIDY(bottomTextureIDY);
        recalcFaces();
    }

    public Block(boolean hasAlfa) {
        this.hasAlfa = hasAlfa;
    }

    public Block(int id, boolean hasAlfa) {
        this.id = id;
        this.hasAlfa = hasAlfa;
    }

    public Block(int id, boolean hasAlfa, int textureIDX, int textureIDY) {
        this.id = id;
        this.hasAlfa = hasAlfa;
        front.setTextureIDX(textureIDX);
        front.setTextureIDY(textureIDY);
        back.setTextureIDX(textureIDX);
        back.setTextureIDY(textureIDY);
        left.setTextureIDX(textureIDX);
        left.setTextureIDY(textureIDY);
        right.setTextureIDX(textureIDX);
        right.setTextureIDY(textureIDY);
        top.setTextureIDX(textureIDX);
        top.setTextureIDY(textureIDY);
        bottom.setTextureIDX(textureIDX);
        bottom.setTextureIDY(textureIDY);
        recalcFaces();
    }

    public Block(int id, boolean hasAlfa,
            int frontTextureIDX, int frontTextureIDY,
            int backTextureIDX, int backTextureIDY,
            int leftTextureIDX, int leftTextureIDY,
            int rightTextureIDX, int rightTextureIDY,
            int topTextureIDX, int topTextureIDY,
            int bottomTextureIDX, int bottomTextureIDY) {
        this.id = id;
        this.hasAlfa = hasAlfa;
        front.setTextureIDX(frontTextureIDX);
        front.setTextureIDY(frontTextureIDY);
        back.setTextureIDX(backTextureIDX);
        back.setTextureIDY(backTextureIDY);
        left.setTextureIDX(leftTextureIDX);
        left.setTextureIDY(leftTextureIDY);
        right.setTextureIDX(rightTextureIDX);
        right.setTextureIDY(rightTextureIDY);
        top.setTextureIDX(topTextureIDX);
        top.setTextureIDY(topTextureIDY);
        bottom.setTextureIDX(bottomTextureIDX);
        bottom.setTextureIDY(bottomTextureIDY);
        recalcFaces();
    }
    public Block(int id, boolean hasAlfa, boolean isFocusable, int textureIDX, int textureIDY) {
        this.id = id;
        this.hasAlfa = hasAlfa;
        this.isFocusable = isFocusable;
        front.setTextureIDX(textureIDX);
        front.setTextureIDY(textureIDY);
        back.setTextureIDX(textureIDX);
        back.setTextureIDY(textureIDY);
        left.setTextureIDX(textureIDX);
        left.setTextureIDY(textureIDY);
        right.setTextureIDX(textureIDX);
        right.setTextureIDY(textureIDY);
        top.setTextureIDX(textureIDX);
        top.setTextureIDY(textureIDY);
        bottom.setTextureIDX(textureIDX);
        bottom.setTextureIDY(textureIDY);
        recalcFaces();
    }

    public Block(int id, boolean hasAlfa, boolean isFocusable,
            int frontTextureIDX, int frontTextureIDY,
            int backTextureIDX, int backTextureIDY,
            int leftTextureIDX, int leftTextureIDY,
            int rightTextureIDX, int rightTextureIDY,
            int topTextureIDX, int topTextureIDY,
            int bottomTextureIDX, int bottomTextureIDY) {
        this.id = id;
        this.hasAlfa = hasAlfa;
        this.isFocusable = isFocusable;
        front.setTextureIDX(frontTextureIDX);
        front.setTextureIDY(frontTextureIDY);
        back.setTextureIDX(backTextureIDX);
        back.setTextureIDY(backTextureIDY);
        left.setTextureIDX(leftTextureIDX);
        left.setTextureIDY(leftTextureIDY);
        right.setTextureIDX(rightTextureIDX);
        right.setTextureIDY(rightTextureIDY);
        top.setTextureIDX(topTextureIDX);
        top.setTextureIDY(topTextureIDY);
        bottom.setTextureIDX(bottomTextureIDX);
        bottom.setTextureIDY(bottomTextureIDY);
        recalcFaces();
    }

    public void recalcFaces() {
        front.recalcTexture();
        back.recalcTexture();
        left.recalcTexture();
        right.recalcTexture();
        bottom.recalcTexture();
        top.recalcTexture();
        xMin = 0;
        yMin = 0;
        zMin = 0;
        xMax = 0;
        yMax = 0;
        zMax = 0;
        for (Face face : sides) {
            for (Vertex vertex : face.getFace()){
                if(vertex.getX() < xMin)
                    xMin = vertex.getX();
                else if(vertex.getX() > xMax)
                    xMax = vertex.getX();

                if(vertex.getY() < yMin)
                    yMin = vertex.getY();
                else if(vertex.getY() > yMax)
                    yMax = vertex.getY();

                if(vertex.getZ() < zMin)
                    zMin = vertex.getZ();
                else if(vertex.getZ() > zMax)
                    zMax = vertex.getZ();
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Face getFront() {
        return front;
    }

    public void setFront(Face front) {
        this.front = front;
    }

    public Face getBack() {
        return back;
    }

    public void setBack(Face back) {
        this.back = back;
    }

    public Face getLeft() {
        return left;
    }

    public void setLeft(Face left) {
        this.left = left;
    }

    public Face getRight() {
        return right;
    }

    public void setRight(Face right) {
        this.right = right;
    }

    public Face getBottom() {
        return bottom;
    }

    public void setBottom(Face bottom) {
        this.bottom = bottom;
    }

    public Face getTop() {
        return top;
    }

    public void setTop(Face top) {
        this.top = top;
    }

    public boolean isHasAlfa() {
        return hasAlfa;
    }

    public void setHasAlfa(boolean hasAlfa) {
        this.hasAlfa = hasAlfa;
    }

    public Face[] getSides() {
        return sides;
    }
    public Face getSide(int i) {
        return sides[i];
    }

    public float getxMin() {
        return xMin;
    }

    public float getyMin() {
        return yMin;
    }

    public float getzMin() {
        return zMin;
    }

    public float getxMax() {
        return xMax;
    }

    public float getyMax() {
        return yMax;
    }

    public float getzMax() {
        return zMax;
    }

    public boolean isFocusable() {
        return isFocusable;
    }

    public void setFocusable(boolean focusable) {
        isFocusable = focusable;
    }
}
