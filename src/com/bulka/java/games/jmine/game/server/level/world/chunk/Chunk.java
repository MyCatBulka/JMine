package com.bulka.java.games.jmine.game.server.level.world.chunk;

import com.bulka.java.games.jmine.game.server.level.world.World;

public class Chunk {
    public static final int WIDTH = 16;
    public static final int NUM_SUB_CHUNKS = 8;
    public static final int HEIGHT = WIDTH*NUM_SUB_CHUNKS;
    public World world;
    public int chunkX = 0;
    public int chunkZ = 0;
    public int startX = 0;
    public int startZ = 0;
    public SubChunk[] subChunks;

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

    public void create(){
        subChunks = new SubChunk[NUM_SUB_CHUNKS];
        for (int i = 0; i < NUM_SUB_CHUNKS; i++) {
            subChunks[i] = new SubChunk(this, chunkX, i, chunkZ);
            subChunks[i].create();
            subChunks[i].updateMesh();
        }
    }
    public void update() {
        for (int i = 0; i < NUM_SUB_CHUNKS; i++) {
            subChunks[i].update();
        }
    }
    public void render() {
        for (int i = 0; i < NUM_SUB_CHUNKS; i++) {
            subChunks[i].render();
        }
    }

    public void destroy(){
        for (int i = 0; i < NUM_SUB_CHUNKS; i++) {
            subChunks[i].destroy();
        }
    }

    public SubChunk getSubChunk(int y){
        if(y < 0 || y >= NUM_SUB_CHUNKS)
            return null;
        return subChunks[y];
    }
    public SubChunk getSubChunkChecked(int y){
        if(y < 0 || y > NUM_SUB_CHUNKS)
            throw new IllegalArgumentException("SubChunk out of bounds (" + y + ")");
        return subChunks[y];
    }

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
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
}
