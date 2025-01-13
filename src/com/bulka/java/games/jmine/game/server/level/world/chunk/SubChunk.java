package com.bulka.java.games.jmine.game.server.level.world.chunk;

import com.bulka.java.games.jmine.engine.graphics.mesh.Vertex;
import com.bulka.java.games.jmine.engine.graphics.shaders.Shader;
import com.bulka.java.games.jmine.engine.graphics.shaders.ShaderManager;
import com.bulka.java.games.jmine.engine.graphics.textures.Textures;
import com.bulka.java.games.jmine.game.server.blocks.Block;
import com.bulka.java.games.jmine.game.server.blocks.Blocks;
import com.bulka.java.games.jmine.game.server.blocks.Face;
import com.bulka.java.games.jmine.game.server.level.world.World;
import com.bulka.java.games.jmine.game.server.level.world.chunk.mesh.ChunkMesh;
import com.bulka.java.games.jmine.game.server.level.world.chunk.mesh.ChunkVertex;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubChunk {
    public static final int WIDTH = 16;
    public static final int HEIGHT = 16;
    public static final int SIZE = WIDTH * HEIGHT;
    private Chunk chunk;
    private int chunkX = 0;
    private int chunkY = 0;
    private int chunkZ = 0;
    private int startBlockX = 0;
    private int startBlockY = 0;
    private int startBlockZ = 0;
    private short[] blocks;
    private ChunkMesh mesh;
    private Matrix4f worldPositionMatrix;
    private FloatBuffer worldPositionBuffer;
    private boolean isEmpty = true;
    private static int renderMethod = GL11.GL_TRIANGLES;
    private boolean needUpdateMesh = false;

    public SubChunk() {

    }

    public SubChunk(int chunkZ, int chunkY, int chunkX) {
        this.chunkZ = chunkZ;
        this.chunkY = chunkY;
        this.chunkX = chunkX;
        startBlockX = chunkX * WIDTH;
        startBlockY = chunkY * HEIGHT;
        startBlockZ = chunkZ * WIDTH;
    }

    public SubChunk(Chunk chunk, int chunkX, int chunkY, int chunkZ) {
        this.chunk = chunk;
        this.chunkX = chunkX;
        this.chunkY = chunkY;
        this.chunkZ = chunkZ;
        startBlockX = chunkX * WIDTH;
        startBlockY = chunkY * HEIGHT;
        startBlockZ = chunkZ * WIDTH;
    }

    public void create() {
        blocks = new short[WIDTH * WIDTH * HEIGHT];
        Arrays.fill(blocks, (short) 0);
        mesh = new ChunkMesh();
        mesh.create();
        worldPositionMatrix = new Matrix4f();
        worldPositionMatrix.identity();
        worldPositionMatrix = worldPositionMatrix.translate(startBlockX, startBlockY, startBlockZ);
        worldPositionBuffer = Shader.matrix4fToBuffer(worldPositionMatrix);
    }

    public void update() {
        if(needUpdateMesh) {
            updateMesh();
            needUpdateMesh = false;
        }
    }

    public void updateMatrix(){
        worldPositionMatrix.identity();
        worldPositionMatrix = worldPositionMatrix.translate(startBlockX, startBlockY, startBlockZ);
        MemoryUtil.memFree(worldPositionBuffer);
        worldPositionBuffer = Shader.matrix4fToBuffer(worldPositionMatrix);
    }

    public void updateMesh() {
        List<ChunkVertex> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        updateMatrix();
        int faceIndex = 0;
        boolean[] alphaNeighbours = new boolean[6];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int z = 0; z < WIDTH; z++) {
                    int i = z * (SIZE) + y * WIDTH + x;
                    short blockID = blocks[i];
                    if(blockID == 0)
                        continue;
                    Block block = Blocks.getSelf().getBlock(blockID);
                    Arrays.fill(alphaNeighbours, false);

                    for (int side = 0; side < 6; side++) {
                        int nx = x, ny = y, nz = z;
                        int nChunkX = chunkX, nChunkY = chunkY, nChunkZ = chunkZ;

                        switch (side) {
                            case 0: nz++; break; // Front
                            case 1: nz--; break; // Back
                            case 2: nx--; break; // Left
                            case 3: nx++; break; // Right
                            case 4: ny++; break; // Top
                            case 5: ny--; break; // Bottom
                        }

                        if (nx < 0) { nx += WIDTH; nChunkX--; }
                        else if (nx >= WIDTH) { nx -= WIDTH; nChunkX++; }
                        if (nz < 0) { nz += WIDTH; nChunkZ--; }
                        else if (nz >= WIDTH) { nz -= WIDTH; nChunkZ++; }
                        if (ny < 0) { ny += HEIGHT; nChunkY--; }
                        else if (ny >= HEIGHT) { ny -= HEIGHT; nChunkY++; }


                        Chunk neighbourChunk = World.getSelf().getChunk(nChunkX, nChunkZ);
                        if (neighbourChunk != null) {
                            SubChunk neighbourSubChunk = neighbourChunk.getSubChunk(nChunkY);
                            if (neighbourSubChunk != null) {
                                int neighbourIndex = nz * SIZE + ny * WIDTH + nx;
                                short neighbourBlock = neighbourSubChunk.blocks[neighbourIndex];
                                alphaNeighbours[side] = Blocks.getSelf().getBlock(neighbourBlock).hasAlfa;
                            } else {
                                alphaNeighbours[side] = true;
                            }
                        } else {
                            alphaNeighbours[side] = true;
                        }
                    }

                    for (int side = 0; side < 6; side++) {
                        if (alphaNeighbours[side]) {
                            Face face = block.getSide(side);
                            float l = face.getLight();
                            for (Vertex vertex : face.getFace()) {
                                vertices.add(new ChunkVertex(
                                        vertex.getX() + x,
                                        vertex.getY() + y,
                                        vertex.getZ() + z,
                                        vertex.getU(),
                                        vertex.getV(), l));
                            }
                            indices.add(faceIndex + Face.INDICES[0]);
                            indices.add(faceIndex + Face.INDICES[1]);
                            indices.add(faceIndex + Face.INDICES[2]);
                            indices.add(faceIndex + Face.INDICES[3]);
                            indices.add(faceIndex + Face.INDICES[4]);
                            indices.add(faceIndex + Face.INDICES[5]);
                            faceIndex += 4;
                        }
                    }
                }
            }
        }

        if(vertices.isEmpty()) {
            isEmpty = true;
        } else {
            isEmpty = false;
//            mesh = new ChunkMesh(vertices.toArray(new ChunkVertex[0]), indices.stream().mapToInt(i -> i).toArray());
//            mesh.destroy();
            mesh.setVertices(vertices.toArray(new ChunkVertex[0]));
            mesh.setIndices(indices.stream().mapToInt(i -> i).toArray());
            mesh.recreate();
        }
        vertices.clear();
        indices.clear();
    }

    public void render() {
        if (!isEmpty) {
            GL30.glBindVertexArray(mesh.getVAO());
            GL30.glEnableVertexAttribArray(0);
            GL30.glEnableVertexAttribArray(1);
            GL30.glEnableVertexAttribArray(2);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIBO());
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL13.glBindTexture(GL13.GL_TEXTURE_2D, Textures.getSelf().getBlocksTexture());
            ShaderManager.getSelf().getChunkShader().bind();
            ShaderManager.getSelf().getChunkShader().setUniformMat4f("worldPosMat", worldPositionBuffer);
            GL11.glDrawElements(renderMethod, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
            ShaderManager.getSelf().getChunkShader().unBind();
            GL13.glBindTexture(GL13.GL_TEXTURE_2D, 0);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);


            GL30.glDisableVertexAttribArray(0);
            GL30.glDisableVertexAttribArray(1);
            GL30.glDisableVertexAttribArray(2);
            GL30.glBindVertexArray(0);
        }
    }

    public void destroy() {
        mesh.destroy();
    }

    public short getBlock(int x, int y, int z) {
        if(x < 0 || x > WIDTH || y < 0 || y > HEIGHT || z < 0 || z > WIDTH) {
            return -1;
        }
        return blocks[z * (SIZE) + y * WIDTH + x];
    }

    public void setBlock(short block, int x, int y, int z) {
        if(x < 0 || x > WIDTH || y < 0 || y > HEIGHT || z < 0 || z > WIDTH) {
            return;
        }
        blocks[z * (SIZE) + y * WIDTH + x] = block;
    }


    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
        startBlockX = chunkX * WIDTH;
    }

    public int getChunkY() {
        return chunkY;
    }

    public void setChunkY(int chunkY) {
        this.chunkY = chunkY;
        startBlockY = chunkY * HEIGHT;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
        startBlockZ = chunkZ * WIDTH;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public ChunkMesh getMesh() {
        return mesh;
    }

    public void setMesh(ChunkMesh mesh) {
        this.mesh = mesh;
    }

    public short[] getBlocks() {
        return blocks;
    }

    public int getStartBlockX() {
        return startBlockX;
    }

    public int getStartBlockY() {
        return startBlockY;
    }

    public int getStartBlockZ() {
        return startBlockZ;
    }

    public Matrix4f getWorldPositionMatrix() {
        return worldPositionMatrix;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public static int getRenderMethod() {
        return renderMethod;
    }

    public static void setRenderMethod(int renderMethod) {
        SubChunk.renderMethod = renderMethod;
    }

    public boolean isNeedUpdateMesh() {
        return needUpdateMesh;
    }

    public void setNeedUpdateMesh(boolean needUpdateMesh) {
        this.needUpdateMesh = needUpdateMesh;
    }
}
