package com.bulka.java.games.jmine.engine.utils;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class GLUtils {
    public static FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }
}
