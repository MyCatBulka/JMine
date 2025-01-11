package com.bulka.java.games.jmine.engine.graphics.mesh;

import com.bulka.java.games.jmine.engine.utils.MemoryUtils;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BasicLinesMesh {
    private Vector3f[] vertices;
    private int vao, pbo;

    public BasicLinesMesh() {

    }

    public BasicLinesMesh(Vector3f[] vertices) {
        this.vertices = vertices;
    }

    public void create() {
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        float[] positionData = new float[vertices.length * 3];
        for (int i = 0; i < vertices.length; i++) {
            positionData[i*3] = vertices[i].x;
            positionData[i*3 + 1] = vertices[i].y();
            positionData[i*3 + 2] = vertices[i].z();
        }
        FloatBuffer positionBuffer = MemoryUtils.arrayToFloatBuffer(positionData);
        pbo = storeData(positionBuffer, 0, 3, GL11.GL_FLOAT);
    }

    private int storeData(FloatBuffer buffer, int index, int size, int type) {
        int bufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, size, type, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return bufferID;
    }


    public Vector3f[] getVertices() {
        return vertices;
    }

    public void setVertices(Vector3f[] vertices) {
        this.vertices = vertices;
    }

    public int getVAO() {
        return vao;
    }

    public int getPBO() {
        return pbo;
    }

    public void destroy(){
        GL15.glDeleteBuffers(pbo);

        GL30.glDeleteVertexArrays(vao);
    }

}
