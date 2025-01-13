package com.bulka.java.games.jmine.engine.graphics.mesh;

import com.bulka.java.games.jmine.engine.utils.MemoryUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh {
    private Vertex[] vertices;
    private int[] indices;
    private int vao, pbo, ibo, tbo;

    public Mesh() {

    }

    public Mesh(Vertex[] vertices) {
        this.vertices = vertices;
    }

    public Mesh(Vertex[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }

    public void create() {
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        float[] positionData = new float[vertices.length * 3];
        for (int i = 0; i < vertices.length; i++) {
            positionData[i*3] = vertices[i].getX();
            positionData[i*3 + 1] = vertices[i].getY();
            positionData[i*3 + 2] = vertices[i].getZ();
        }
        FloatBuffer positionBuffer = MemoryUtils.arrayToFloatBuffer(positionData);
        pbo = storeData(positionBuffer, 0, 3, GL11.GL_FLOAT);

        float[] textureData = new float[vertices.length * 2];
        for (int i = 0; i < vertices.length; i++) {
            textureData[i*2] = vertices[i].getU();
            textureData[i*2 + 1] = vertices[i].getV();
        }
        FloatBuffer textureCoordsBuffer = MemoryUtils.arrayToFloatBuffer(textureData);
        tbo = storeData(textureCoordsBuffer, 1, 2, GL11.GL_FLOAT);

        if (indices != null) {
            IntBuffer indicesBuffer = MemoryUtils.arrayToIntBuffer(indices);
            ibo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        } else {
            ibo = 0;
        }

    }

    private int storeData(FloatBuffer buffer, int index, int size, int type) {
        int bufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, size, type, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return bufferID;
    }

    public void destroy(){
        GL15.glDeleteBuffers(pbo);
        GL15.glDeleteBuffers(ibo);
        GL15.glDeleteBuffers(tbo);

        GL30.glDeleteVertexArrays(vao);
    }

    public int[] getIndices() {
        return indices;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public int getVAO() {
        return vao;
    }

    public int getPBO() {
        return pbo;
    }

    public int getIBO() {
        return ibo;
    }

    public int getTBO() {
        return tbo;
    }

    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }
}
