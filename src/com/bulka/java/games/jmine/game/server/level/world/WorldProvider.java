package com.bulka.java.games.jmine.game.server.level.world;

import com.bulka.java.games.jmine.game.Game;
import com.bulka.java.games.jmine.game.server.level.world.chunk.Chunk;

import java.util.Random;
import java.util.logging.Logger;

public class WorldProvider {
    private Logger logger = Logger.getLogger(WorldProvider.class.getName());
    private World world;

    public void init() {
        world = new World();
        world.init();
        generate();
    }

    public void postInit() {
        world.postInit();
    }

    public void load() {
        world.load();
    }

    public void save() {
        world.save();
    }

    public void generate() {
        int renderDistance = world.getRenderDistance();
        world.chunks = new Chunk[renderDistance][renderDistance];

        for (int x = 0; x < renderDistance; x++) {
            for (int z = 0; z < renderDistance; z++) {
                Chunk chunk = new Chunk(x - renderDistance / 2, z - renderDistance / 2, world);
                world.chunks[x][z] = chunk;
                world.chunks[x][z].create();
            }
        }

        Random random = new Random();
//        for (int x = -renderDistance * Chunk.WIDTH / 2; x < world.getBlocksWidth() / 2; x++) {
//            for (int z = -renderDistance * Chunk.WIDTH / 2; z < world.getBlocksWidth() / 2; z++) {
//                for (int y = 0; y < 10; y++) {
//                    world.setBlock((short) 3, (byte) 0, x, y, z);
//                }
//            }
//        }

        for (int x = 0; x < renderDistance; x++) {
            for (int z = 0; z < renderDistance; z++) {
                Chunk chunk = world.chunks[x][z];
                generateChunk(chunk);
            }
        }
        for (int x = 0; x < renderDistance; x++) {
            for (int z = 0; z < renderDistance; z++) {
                for (int y = 0; y < Chunk.NUM_SUB_CHUNKS; y++) {
                    world.chunks[x][z].getSubChunk(y).updateMesh();
                }
            }
        }
    }

    public void generateChunk(Chunk chunk) {
        for (int x = chunk.startX; x < chunk.startX + Chunk.WIDTH; x++) {
            for (int z = chunk.startZ; z < chunk.startZ + Chunk.WIDTH; z++) {
                for (int y = 0; y < 10; y++) {
                    world.setBlock((short) 3, (byte) 0, x, y, z);
                }
            }
        }
    }

    public void moveChunks(int x, int z) {
        //TODO
//        int renderDistance = world.getRenderDistance();
//        Chunk[][] newChunks = new Chunk[renderDistance][renderDistance];
//
//        for (int i = 0; i < renderDistance; i++) {
//            for (int j = 0; j < renderDistance; j++) {
//                int newX = i - x;
//                int newZ = j - z;
//
//                if (newX >= 0 && newX < renderDistance && newZ >= 0 && newZ < renderDistance) {
//                    newChunks[i][j] = world.chunks[newX][newZ];
//                } else {
//                    int chunkStartX = (i - renderDistance / 2) * Chunk.WIDTH;
//                    int chunkStartZ = (j - renderDistance / 2) * Chunk.WIDTH;
//                    Chunk newChunk = new Chunk(chunkStartX, chunkStartZ, world);
//                    newChunk.create();
//                    generateChunk(newChunk);
//                    newChunks[i][j] = newChunk;
//                }
//            }
//        }
//
//        world.chunks = newChunks;
//
//        for (int i = 0; i < renderDistance; i++) {
//            for (int j = 0; j < renderDistance; j++) {
//                for (int y = 0; y < Chunk.NUM_SUB_CHUNKS; y++) {
//                    world.chunks[i][j].getSubChunk(y).updateMesh();
//                }
//            }
//        }
    }

    public void update() {
        world.update();
    }

    public void render() {
        world.render();
    }

    public void destroy() {
        world.destroy();
    }

    public static WorldProvider getSelf() {
        return Game.getSelf().getWorldProvider();
    }

    public World getWorld() {
        return world;
    }
}
