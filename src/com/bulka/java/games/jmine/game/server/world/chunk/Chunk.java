package com.bulka.java.games.jmine.game.server.world.chunk;

public class Chunk {
    public static final int WIDTH = 16;
    public static final int NUM_SUB_CHUNKS = 8;
    public static final int HEIGHT = WIDTH*NUM_SUB_CHUNKS;
    private SubChunk[] subChunks;

    public Chunk() {
        subChunks = new SubChunk[NUM_SUB_CHUNKS];
    }
}
