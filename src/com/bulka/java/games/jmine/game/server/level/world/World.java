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
                Chunk chunk = new Chunk(x-width/2, z-width/2, this);
                chunks[x][z] = chunk;
                chunks[x][z].create();
            }
        }

        Random random = new Random();
        for (int x = -width*Chunk.WIDTH/2; x < blocksWidth / 2; x++) {
            for (int z = -width*Chunk.WIDTH/2; z < blocksWidth / 2; z++) {
                for (int y = 0; y < 10; y++) {
                    setBlock((short) 3, (byte) 0, x, y, z);
                }
            }
        }
//        for (int x = -width*Chunk.WIDTH/2; x < blocksWidth / 2; x++) {
//            for (int z = -width*Chunk.WIDTH/2; z < blocksWidth / 2; z++) {
//                for (int y = 0; y < 10; y++) {
//                    int block = getBlockID(x, y, z);
//                    if(block == -1)
//                        System.out.printf("X %s Y %s Z %s - %s", x, y, z, block);
//                }
//            }
//        }

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

    public short getBlock(int x, int y, int z){
        if(x < -blocksWidth / 2 || x > blocksWidth / 2 || y < 0 || y > Chunk.HEIGHT || z < -blocksWidth / 2 || z > blocksWidth / 2)
            return -1;
        return chunks[(int) (x+blocksWidth/2)/Chunk.WIDTH][(int) (z+blocksWidth/2)/Chunk.WIDTH].getBlock(Math.abs(x%Chunk.WIDTH), y, Math.abs(z%Chunk.WIDTH));
    }
    public short getBlockID(int x, int y, int z){
        if(x < -blocksWidth / 2 || x > blocksWidth / 2 || y < 0 || y > Chunk.HEIGHT || z < -blocksWidth / 2 || z > blocksWidth / 2)
            return -1;
        return chunks[(int) (x+blocksWidth/2)/Chunk.WIDTH][(int) (z+blocksWidth/2)/Chunk.WIDTH].getBlockID(Math.abs(x%Chunk.WIDTH), y, Math.abs(z%Chunk.WIDTH));
    }
    public short getBlockState(int x, int y, int z){
        if(x < -blocksWidth / 2 || x > blocksWidth / 2 || y < 0 || y > Chunk.HEIGHT || z < -blocksWidth / 2 || z > blocksWidth / 2)
            return -1;
       return chunks[(int) (x+blocksWidth/2)/Chunk.WIDTH][(int) (z+blocksWidth/2)/Chunk.WIDTH].getBlockState(Math.abs(x%Chunk.WIDTH), y, Math.abs(z%Chunk.WIDTH));
    }
    public void setBlock(short id, byte state, int x, int y, int z){
        if(x < -blocksWidth / 2 || x > blocksWidth / 2 || y < 0 || y > Chunk.HEIGHT || z < -blocksWidth / 2 || z > blocksWidth / 2)
            return;
        chunks[(int) (x+blocksWidth/2)/Chunk.WIDTH][(int) (z+blocksWidth/2)/Chunk.WIDTH].setBlock(id, state,  Math.abs(x%Chunk.WIDTH), y, Math.abs(z%Chunk.WIDTH));
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        blocksWidth = width * Chunk.WIDTH;
    }
    public Chunk getChunk(int x, int z) {
        if(x < -width/2 || x >= width/2 || z < -width/2 || z >= width/2)
            return null;
        return chunks[x+width/2][z+width/2];
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

