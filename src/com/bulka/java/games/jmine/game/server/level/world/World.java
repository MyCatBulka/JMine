package com.bulka.java.games.jmine.game.server.level.world;

import com.bulka.java.games.jmine.game.Game;
import com.bulka.java.games.jmine.game.server.level.world.chunk.Chunk;
import com.bulka.java.games.jmine.settings.SettingsManager;


public class World {
    private int renderDistance = 2 * 2 + 1;
    private int blocksWidth = renderDistance * Chunk.WIDTH;
    public Chunk[][] chunks;

    public void init() {
        renderDistance = SettingsManager.getSelf().getInt("game.graphics.render_distance", 2) * 2 + 1;
        blocksWidth = renderDistance * Chunk.WIDTH;
        generate();
    }

    public void postInit() {

    }

    public void load() {

    }

    public void save() {

    }

    public void generate() {

    }

    public void update() {
        for (int x = 0; x < renderDistance; x++) {
            for (int y = 0; y < renderDistance; y++) {
                chunks[x][y].update();
            }
        }
    }

    public void render() {
        for (int x = 0; x < renderDistance; x++) {
            for (int y = 0; y < renderDistance; y++) {
                chunks[x][y].render();
            }
        }
    }

    public void destroy() {
        for (int x = 0; x < renderDistance; x++) {
            for (int y = 0; y < renderDistance; y++) {
                chunks[x][y].destroy();
            }
        }
    }


    public short getBlock(int x, int y, int z) {
        if (x < -blocksWidth / 2 || x > blocksWidth / 2 || y < 0 || y > Chunk.HEIGHT || z < -blocksWidth / 2 || z > blocksWidth / 2)
            return -1;
        return chunks[(int) (x + blocksWidth / 2) / Chunk.WIDTH][(int) (z + blocksWidth / 2) / Chunk.WIDTH].getBlock((x % Chunk.WIDTH + Chunk.WIDTH) % Chunk.WIDTH, y, (z % Chunk.WIDTH + Chunk.WIDTH) % Chunk.WIDTH);
    }

    public short getBlockID(int x, int y, int z) {
        if (x < -blocksWidth / 2 || x > blocksWidth / 2 || y < 0 || y > Chunk.HEIGHT || z < -blocksWidth / 2 || z > blocksWidth / 2)
            return -1;
        return chunks[(int) (x + blocksWidth / 2) / Chunk.WIDTH][(int) (z + blocksWidth / 2) / Chunk.WIDTH].getBlockID((x % Chunk.WIDTH + Chunk.WIDTH) % Chunk.WIDTH, y, (z % Chunk.WIDTH + Chunk.WIDTH) % Chunk.WIDTH);
    }

    public short getBlockState(int x, int y, int z) {
        if (x < -blocksWidth / 2 || x > blocksWidth / 2 || y < 0 || y > Chunk.HEIGHT || z < -blocksWidth / 2 || z > blocksWidth / 2)
            return -1;
        return chunks[(int) (x + blocksWidth / 2) / Chunk.WIDTH][(int) (z + blocksWidth / 2) / Chunk.WIDTH].getBlockState((x % Chunk.WIDTH + Chunk.WIDTH) % Chunk.WIDTH, y, (z % Chunk.WIDTH + Chunk.WIDTH) % Chunk.WIDTH);
    }

    public void setBlock(short id, byte state, int x, int y, int z) {
        if (x < -blocksWidth / 2 || x > blocksWidth / 2 || y < 0 || y > Chunk.HEIGHT || z < -blocksWidth / 2 || z > blocksWidth / 2)
            return;
        chunks[(int) (x + blocksWidth / 2) / Chunk.WIDTH][(int) (z + blocksWidth / 2) / Chunk.WIDTH].setBlock(id, state, (x % Chunk.WIDTH + Chunk.WIDTH) % Chunk.WIDTH, y, (z % Chunk.WIDTH + Chunk.WIDTH) % Chunk.WIDTH);
    }

    public int getRenderDistance() {
        return renderDistance;
    }

    public void setRenderDistance(int renderDistance) {
        this.renderDistance = renderDistance;
        blocksWidth = renderDistance * Chunk.WIDTH;
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        int indexX = chunkX + renderDistance / 2;
        int indexZ = chunkZ + renderDistance / 2;

        if (indexX < 0 || indexX >= renderDistance || indexZ < 0 || indexZ >= renderDistance) {
            return null;
        }

        return chunks[indexX][indexZ];
    }
    public Chunk[][] getChunks() {
        return chunks;
    }

    public static World getSelf() {
        return Game.getSelf().getWorldProvider().getWorld();
    }

    public int getBlocksWidth() {
        return blocksWidth;
    }
}

