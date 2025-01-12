package com.bulka.java.games.jmine.game.server.level.world.chunk;

import com.bulka.java.games.jmine.game.server.level.world.World;

public class Chunk {
    public static final int WIDTH = 16;
    public static final int NUM_SUB_CHUNKS = 8;
    public static final int HEIGHT = WIDTH * NUM_SUB_CHUNKS;
    public World world;
    public int chunkX = 0;
    public int chunkZ = 0;
    public int startX = 0;
    public int startZ = 0;
    public SubChunk[] subChunks;
    private boolean needUpdateMeshes = false;

    public Chunk() {

    }

    public Chunk(int chunkX, int chunkZ) {
        this.chunkZ = chunkZ;
        this.chunkX = chunkX;
        startX = chunkX * WIDTH;
        startZ = chunkZ * WIDTH;
    }

    public Chunk(int chunkX, int chunkZ, World world) {
        this.chunkZ = chunkZ;
        this.chunkX = chunkX;
        this.world = world;
        startX = chunkX * WIDTH;
        startZ = chunkZ * WIDTH;
    }

    public void create() {
        subChunks = new SubChunk[NUM_SUB_CHUNKS];
        for (int i = 0; i < NUM_SUB_CHUNKS; i++) {
            subChunks[i] = new SubChunk(this, chunkX, i, chunkZ);
            subChunks[i].create();
//            subChunks[i].updateMesh();
        }
    }

    public void setPosition(int x, int z){
        for (int i = 0; i < NUM_SUB_CHUNKS; i++) {
            subChunks[i].getWorldPositionMatrix().identity();
            subChunks[i].getWorldPositionMatrix().translate(x, 0, z);
        }
    }
    public void addPosition(int x, int z){
        for (int i = 0; i < NUM_SUB_CHUNKS; i++) {
            subChunks[i].getWorldPositionMatrix().translate(x, 0, z);
        }
    }

    public void update() {
        if(needUpdateMeshes) {
            updateMeshes();
            needUpdateMeshes = false;
        }
        for (int i = 0; i < NUM_SUB_CHUNKS; i++) {
            subChunks[i].update();
        }
    }

    public void render() {
        for (int i = 0; i < NUM_SUB_CHUNKS; i++) {
            subChunks[i].render();
        }
    }

    public void destroy() {
        for (int i = 0; i < NUM_SUB_CHUNKS; i++) {
            subChunks[i].destroy();
        }
    }

    public void updateMeshes(){
        for (int i = 0; i < NUM_SUB_CHUNKS; i++) {
            subChunks[i].updateMesh();
        }
    }
    public void updateMatrices(){
        for (int i = 0; i < NUM_SUB_CHUNKS; i++) {
            subChunks[i].updateMatrix();
        }
    }

    public short getBlock(int x, int y, int z) {
        if (y < 0 || y > HEIGHT)
            return -1;
        return subChunks[(int) y / SubChunk.HEIGHT].getBlock(x, y % SubChunk.HEIGHT, z);
    }


    public void setBlock(short id, int x, int y, int z) {
        if (y < 0 || y > HEIGHT)
            return;
        subChunks[y / SubChunk.HEIGHT].setBlock(id, x, y % SubChunk.HEIGHT, z);
    }

    public SubChunk getSubChunk(int y) {
        if (y < 0 || y >= NUM_SUB_CHUNKS)
            return null;
        return subChunks[y];
    }

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
        for (int i = 0; i < NUM_SUB_CHUNKS; i++) {
            subChunks[i].setChunkX(chunkX);
        }
        startX = chunkX * WIDTH;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
        for (int i = 0; i < NUM_SUB_CHUNKS; i++) {
            subChunks[i].setChunkZ(chunkZ);
        }
        startZ = chunkZ * WIDTH;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartZ() {
        return startZ;
    }

    public SubChunk[] getSubChunks() {
        return subChunks;
    }

    public boolean isNeedUpdateMeshes() {
        return needUpdateMeshes;
    }

    public void setNeedUpdateMeshes(boolean needUpdateMeshes) {
        this.needUpdateMeshes = needUpdateMeshes;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public void setStartZ(int startZ) {
        this.startZ = startZ;
    }
}
