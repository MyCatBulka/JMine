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
        for (int x = 0; x < Chunk.WIDTH; x++) {
            for (int z = 0; z < Chunk.WIDTH; z++) {
                for (int y = 0; y < 10; y++) {
                    chunk.setBlock((short) 3, (byte) 0, x, y, z);
                }
            }
        }
    }

//    public void moveChunks(int offsetX, int offsetZ) {
//        if (offsetX == 0 && offsetZ == 0) {
//            return; // Никакого смещения не требуется
//        }
//
//        int renderDistance = world.getRenderDistance();
//        Chunk[][] newChunks = new Chunk[renderDistance][renderDistance];
//
//        // Сдвигаем существующие чанки
//        for (int x = 0; x < renderDistance; x++) {
//            for (int z = 0; z < renderDistance; z++) {
//                int newX = x - offsetX;
//                int newZ = z - offsetZ;
//
//                // Проверяем, находится ли новый индекс в пределах массива
//                if (newX >= 0 && newX < renderDistance && newZ >= 0 && newZ < renderDistance) {
//                    newChunks[newX][newZ] = world.chunks[x][z];
//                }
//            }
//        }
//
//        // Генерируем новые чанки для пустых мест
//        for (int x = 0; x < renderDistance; x++) {
//            for (int z = 0; z < renderDistance; z++) {
//                if (newChunks[x][z] == null) {
//                    // Рассчитываем мировые координаты нового чанка
//                    int chunkWorldX = x - renderDistance / 2;
//                    int chunkWorldZ = z - renderDistance / 2;
//
//                    Chunk newChunk = new Chunk(chunkWorldX, chunkWorldZ, world);
//                    newChunk.create();
//                    generateChunk(newChunk); // Генерация содержимого нового чанка
//                    newChunks[x][z] = newChunk;
//                }
//            }
//        }
//
//        // Заменяем старый массив чанков на новый
//        world.chunks = newChunks;
//
//        // Обновляем сетку (например, рендеринг)
//        for (int x = 0; x < renderDistance; x++) {
//            for (int z = 0; z < renderDistance; z++) {
//                for (int y = 0; y < Chunk.NUM_SUB_CHUNKS; y++) {
//                    world.chunks[x][z].getSubChunk(y).updateMesh();
//                }
//            }
//        }
//    }

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
