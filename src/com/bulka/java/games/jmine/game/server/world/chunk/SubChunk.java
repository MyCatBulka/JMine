package com.bulka.java.games.jmine.game.server.world.chunk;

public class SubChunk {
    public static final int WIDTH = 16;
    public static final int HEIGHT = 16;
    private short[] blocks;

    public SubChunk() {
        blocks = new short[WIDTH*WIDTH*HEIGHT];
    }
}
