package com.bulka.java.games.jmine.game.server.level.world;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.graphics.camera.Hero;
import com.bulka.java.games.jmine.engine.graphics.mesh.BasicLinesMesh;
import com.bulka.java.games.jmine.engine.graphics.shaders.ShaderManager;
import com.bulka.java.games.jmine.game.Game;
import com.bulka.java.games.jmine.game.server.blocks.Block;
import com.bulka.java.games.jmine.game.server.blocks.Blocks;
import com.bulka.java.games.jmine.game.server.blocks.Face;
import com.bulka.java.games.jmine.game.server.level.world.chunk.Chunk;
import com.bulka.java.games.jmine.game.server.level.world.chunk.SubChunk;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class WorldProvider {
    private Logger logger = Logger.getLogger(WorldProvider.class.getName());
    private World world;
    private HintRenderer hintRenderer;

    public void init() {
        world = new World();
        world.init();
        hintRenderer = new HintRenderer();
        hintRenderer.init();
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

        for (int x = 0; x < renderDistance; x++) {
            for (int z = 0; z < renderDistance; z++) {
                Chunk chunk = world.chunks[x][z];
                generateChunk(chunk);
            }
        }

        world.setBlock((short) 3, 0, 10, 0);

        for (int x = 0; x < renderDistance; x++) {
            for (int z = 0; z < renderDistance; z++) {
                world.chunks[x][z].updateMeshes();
            }
        }
    }

    public void generateChunk(Chunk chunk) {
        Random random = new Random(chunk.getChunkX() * 31L + chunk.getChunkZ() * 17L);
        for (int x = 0; x < Chunk.WIDTH; x++) {
            for (int z = 0; z < Chunk.WIDTH; z++) {
                int height = random.nextInt(3) + 1;
                for (int y = 0; y < height; y++) {
                    chunk.setBlock((short) 2, x, y, z);
                }
                chunk.setBlock((short) 3, x, height, z);
            }
        }
    }

        public void moveChunks(int offsetX, int offsetZ) {
            if (offsetX == 0 && offsetZ == 0) {
                return;
            }

            int renderDistance = world.getRenderDistance();
            Chunk[][] newChunks = new Chunk[renderDistance][renderDistance];

            for (int x = 0; x < renderDistance; x++) {
                for (int z = 0; z < renderDistance; z++) {
                    int newX = x - offsetX;
                    int newZ = z - offsetZ;

                    if (newX >= 0 && newX < renderDistance && newZ >= 0 && newZ < renderDistance) {
                        newChunks[newX][newZ] = world.chunks[x][z];
                        newChunks[newX][newZ].setChunkX(newX - renderDistance / 2);
                        newChunks[newX][newZ].setChunkZ(newZ - renderDistance / 2);
                        newChunks[newX][newZ].updateMatrices();
                    }
                }
            }

            for (int x = 0; x < renderDistance; x++) {
                for (int z = 0; z < renderDistance; z++) {
                    if (newChunks[x][z] == null) {
                        int chunkWorldX = x - renderDistance / 2;
                        int chunkWorldZ = z - renderDistance / 2;

                        Chunk newChunk = new Chunk(chunkWorldX, chunkWorldZ, world);
                        newChunk.create();
                        generateChunk(newChunk);
                        newChunks[x][z] = newChunk;
                        newChunk.setNeedUpdateMeshes(true);

                        Chunk neighChunk;
                        if(x != 0) {
                            neighChunk = newChunks[x - 1][z];
                            if (neighChunk != null)
                                neighChunk.setNeedUpdateMeshes(true);
                        }
                        if(x != renderDistance -1) {
                            neighChunk = newChunks[x + 1][z];
                            if (neighChunk != null)
                                neighChunk.setNeedUpdateMeshes(true);
                        }
                        if(z != 0) {
                            neighChunk = newChunks[x][z - 1];
                            if (neighChunk != null)
                                neighChunk.setNeedUpdateMeshes(true);
                        }
                        if(z != renderDistance -1) {
                            neighChunk = newChunks[x][z+1];
                            if(neighChunk != null)
                                neighChunk.setNeedUpdateMeshes(true);
                        }
                    }
                }
            }

            world.chunks = newChunks;
        }


    public void update() {
        world.update();
        hintRenderer.update();
    }

    public void render() {
        world.render();
        hintRenderer.render();
    }

    public void destroy() {
        world.destroy();
        hintRenderer.destroy();
    }


    public void setBlockAndUpdateMeshes(short id, int x, int y, int z) {
        if (x < -world.getBlocksWidth() / 2 || x >= world.getBlocksWidth() / 2 || y < 0 || y >= Chunk.HEIGHT || z < -world.getBlocksWidth() / 2 || z >= world.getBlocksWidth() / 2)
            return;
        WorldProvider.getSelf().getWorld().setBlock(id, x, y, z);

        int chunkX = (int) Math.floor((double) x / Chunk.WIDTH);
        int chunkY = (int) Math.floor((double) y / SubChunk.HEIGHT);
        int chunkZ = (int) Math.floor((double) z / Chunk.WIDTH);

        int inChunkX = (x % Chunk.WIDTH + Chunk.WIDTH) % Chunk.WIDTH;
        int inChunkY = (y % SubChunk.HEIGHT + SubChunk.HEIGHT) % SubChunk.HEIGHT;
        int inChunkZ = (z % Chunk.WIDTH + Chunk.WIDTH) % Chunk.WIDTH;

        if (inChunkX == 0) {
            Chunk chunk =  WorldProvider.getSelf().getWorld().getChunk(chunkX - 1, chunkZ);
            if(chunk != null) {
                SubChunk subChunk = chunk.getSubChunk(chunkY);
                if(subChunk != null){
                    subChunk.updateMesh();
                }
            }
        } else if (inChunkX == Chunk.WIDTH - 1) {
            Chunk chunk =  WorldProvider.getSelf().getWorld().getChunk(chunkX + 1, chunkZ);
            if(chunk != null) {
                SubChunk subChunk = chunk.getSubChunk(chunkY);
                if(subChunk != null){
                    subChunk.updateMesh();
                }
            }
        }
        if (inChunkZ == 0) {
            Chunk chunk =  WorldProvider.getSelf().getWorld().getChunk(chunkX, chunkZ-1);
            if(chunk != null) {
                SubChunk subChunk = chunk.getSubChunk(chunkY);
                if(subChunk != null){
                    subChunk.updateMesh();
                }
            }
        } else if (inChunkZ == Chunk.WIDTH - 1) {
            Chunk chunk =  WorldProvider.getSelf().getWorld().getChunk(chunkX, chunkZ+1);
            if(chunk != null) {
                SubChunk subChunk = chunk.getSubChunk(chunkY);
                if(subChunk != null){
                    subChunk.updateMesh();
                }
            }
        }
        if (inChunkY == 0) {
            Chunk chunk =  WorldProvider.getSelf().getWorld().getChunk(chunkX, chunkZ);
            if(chunk != null) {
                SubChunk subChunk = chunk.getSubChunk(chunkY-1);
                if(subChunk != null){
                    subChunk.updateMesh();
                }
            }
        }
        else if (inChunkY == SubChunk.HEIGHT - 1) {
            Chunk chunk = WorldProvider.getSelf().getWorld().getChunk(chunkX, chunkZ);
            if(chunk != null) {
                SubChunk subChunk = chunk.getSubChunk(chunkY+1);
                if(subChunk != null){
                    subChunk.updateMesh();
                }
            }
        }

        WorldProvider.getSelf().getWorld().getChunk(chunkX, chunkZ).getSubChunk(chunkY).updateMesh();

    }

    public static WorldProvider getSelf() {
        return Game.getSelf().getWorldProvider();
    }

    public World getWorld() {
        return world;
    }

    public HintRenderer getHintRenderer() {
        return hintRenderer;
    }
}
