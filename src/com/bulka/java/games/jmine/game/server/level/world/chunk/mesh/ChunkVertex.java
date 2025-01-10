package com.bulka.java.games.jmine.game.server.level.world.chunk.mesh;

import com.bulka.java.games.jmine.engine.graphics.mesh.Vertex;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class ChunkVertex extends Vertex {
    public ChunkVertex(float x, float y, float z) {
        super(x, y, z);
    }

    public ChunkVertex(float x, float y, float z, float u, float v) {
        super(x, y, z, u, v);
    }

    public ChunkVertex(Vector3f position) {
        super(position);
    }

    public ChunkVertex(Vector3f position, Vector2f textureCoords) {
        super(position, textureCoords);
    }

    public ChunkVertex() {
    }
}
