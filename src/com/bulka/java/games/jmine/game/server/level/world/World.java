package com.bulka.java.games.jmine.game.server.level.world;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.game.Game;
import com.bulka.java.games.jmine.game.server.blocks.Blocks;
import com.bulka.java.games.jmine.game.server.level.world.chunk.Chunk;
import com.bulka.java.games.jmine.game.server.level.world.chunk.SubChunk;

import java.util.Random;

public class World {
    private int width = 4;
    private int blocksWidth = width * Chunk.WIDTH;
    public Chunk[][] chunks;

    public void init(){
        generate();

    }
    public void postInit() {

    }
    public void load(){

    }

    public void save(){

    }

    public void generate(){
        chunks = new Chunk[width][width];

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < width; z++) {
                Chunk chunk = new Chunk(x, z, this);
                chunks[x][z] = chunk;
                chunks[x][z].create();
            }
        }

        Random random = new Random();
        for (int x = 0; x < blocksWidth; x++) {
            for (int z = 0; z < blocksWidth; z++) {
                Chunk chunk = chunks[(int) x/Chunk.WIDTH][(int) z/Chunk.WIDTH];
                for (int y = 0; y < Chunk.HEIGHT; y++) {
                    SubChunk subChunk = chunk.getSubChunk((int)y/SubChunk.HEIGHT);
                    if(y < 60){
                        subChunk.setBlock((short) Blocks.getSelf().getBlock(1).getId(), (byte) 0, x%Chunk.WIDTH, y%SubChunk.HEIGHT, z%Chunk.WIDTH);
                    }
                    if(y >= 60 && y < 63){
                        subChunk.setBlock((short) Blocks.getSelf().getBlock(2).getId(), (byte) 0, x%Chunk.WIDTH, y%SubChunk.HEIGHT, z%Chunk.WIDTH);
                    }
                    if(y == 63){
                        subChunk.setBlock((short) Blocks.getSelf().getBlock(3).getId(), (byte) 0, x%Chunk.WIDTH, y%SubChunk.HEIGHT, z%Chunk.WIDTH);
                    }
                }
                if(random.nextInt(2)==0){
                    chunk.getSubChunk((int)64/SubChunk.HEIGHT).setBlock((short) Blocks.getSelf().getBlock(3).getId(), (byte) 0, x%Chunk.WIDTH, 64%SubChunk.HEIGHT, z%Chunk.WIDTH);
                }
            }
        }

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < width; z++) {
                Chunk chunk = chunks[x][z];
                for (int y = 0; y < Chunk.NUM_SUB_CHUNKS; y++) {
                    chunk.getSubChunk(y).updateMesh();
                }
            }
        }


    }
    public void update(){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                chunks[x][y].update();
            }
        }
    }
    public void render(){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                chunks[x][y].render();
            }
        }
    }
    public void destroy(){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                chunks[x][y].destroy();
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        blocksWidth = width * Chunk.WIDTH;
    }
    public Chunk getChunk(int x, int z) {
        if(x < 0 || x >= width || z < 0 || z >= width)
            return null;
        return chunks[x][z];
    }

    public Chunk[][] getChunks() {
        return chunks;
    }

    public static World getSelf(){
        return Game.getSelf().getWorld();
    }

    public int getBlocksWidth() {
        return blocksWidth;
    }
}

