package com.bulka.java.games.jmine.game.server.level.world.chunk.mesh;

import com.bulka.java.games.jmine.engine.utils.MemoryUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ChunkMesh {
    private ChunkVertex[] vertices;
    private int[] indices;
    private int vao = 0, pbo = 0, ibo = 0, tbo = 0, lbo = 0;
    private IntBuffer indicesBuffer;
    private FloatBuffer positionBuffer;
    private FloatBuffer textureCordsBuffer;
    private FloatBuffer lightBuffer;

    public ChunkMesh() {

    }

    public ChunkMesh(ChunkVertex[] vertices) {
        this.vertices = vertices;
    }

    public ChunkMesh(ChunkVertex[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }

    public void create() {
        if (vertices == null || indices == null) {
            return;
        }

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        try {
            indicesBuffer = MemoryUtils.arrayToIntBuffer(indices);
            ibo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

            float[] positionData = new float[vertices.length * 3];
            for (int i = 0; i < vertices.length; i++) {
                positionData[i * 3] = vertices[i].getX();
                positionData[i * 3 + 1] = vertices[i].getY();
                positionData[i * 3 + 2] = vertices[i].getZ();
            }
            positionBuffer = MemoryUtils.arrayToFloatBuffer(positionData);
            pbo = storeData(positionBuffer, 0, 3, GL11.GL_FLOAT);

            float[] textureData = new float[vertices.length * 2];
            for (int i = 0; i < vertices.length; i++) {
                textureData[i * 2] = vertices[i].getU();
                textureData[i * 2 + 1] = vertices[i].getV();
            }
            textureCordsBuffer = MemoryUtils.arrayToFloatBuffer(textureData);
            tbo = storeData(textureCordsBuffer, 1, 2, GL11.GL_FLOAT);

            float[] lightData = new float[vertices.length];
            for (int i = 0; i < vertices.length; i++) {
                lightData[i] = vertices[i].getLight();
            }
            lightBuffer = MemoryUtils.arrayToFloatBuffer(lightData);
            lbo = storeData(lightBuffer, 2, 1, GL11.GL_FLOAT);

        } finally {

        }

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }
    public float calculateSizeInMB() {
        int vertexCount = vertices.length;
        int vertexSize = vertexCount * (3 * Float.BYTES + 2 * Float.BYTES + Float.BYTES); // x, y, z, u, v, light

        int indexSize = indices.length * Integer.BYTES;

        int totalSizeBytes = vertexSize + indexSize;

        return totalSizeBytes / (1024.0f * 1024.0f);
    }

    private int storeData(FloatBuffer buffer, int index, int size, int type) {
        int bufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, size, type, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return bufferID;
    }

    public void recreate(){
        destroy();
        create();
    }

    public void destroy() {
        GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        if (pbo != 0) {
            GL15.glDeleteBuffers(pbo);
            pbo = 0;
        }
        if (ibo != 0) {
            GL15.glDeleteBuffers(ibo);
            ibo = 0;
        }
        if (tbo != 0) {
            GL15.glDeleteBuffers(tbo);
            tbo = 0;
        }
        if (lbo != 0) {
            GL15.glDeleteBuffers(lbo);
            lbo = 0;
        }
        if (vao != 0) {
            GL30.glDeleteVertexArrays(vao);
            vao = 0;
        }

        MemoryUtil.memFree(indicesBuffer);
        indicesBuffer = null;
        MemoryUtil.memFree(positionBuffer);
        positionBuffer = null;
        MemoryUtil.memFree(textureCordsBuffer);
        textureCordsBuffer = null;
        MemoryUtil.memFree(lightBuffer);
        lightBuffer = null;
    }

    public int[] getIndices() {
        return indices;
    }

    public ChunkVertex[] getVertices() {
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
    public int getLBO() {
        return lbo;
    }

    public void setVertices(ChunkVertex[] vertices) {
        this.vertices = vertices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public FloatBuffer getLightBuffer() {
        return lightBuffer;
    }

    public FloatBuffer getTextureCordsBuffer() {
        return textureCordsBuffer;
    }

    public FloatBuffer getPositionBuffer() {
        return positionBuffer;
    }

    public IntBuffer getIndicesBuffer() {
        return indicesBuffer;
    }
}
