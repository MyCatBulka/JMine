package com.bulka.java.games.jmine.engine.utils;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class MemoryUtils {
    public static FloatBuffer arrayToFloatBuffer(float[] array){
        FloatBuffer buffer = MemoryUtil.memAllocFloat(array.length);
        buffer.put(array).flip();
        return buffer;
    }
    public static IntBuffer arrayToIntBuffer(int[] array){
        IntBuffer buffer = MemoryUtil.memAllocInt(array.length);
        buffer.put(array).flip();
        return buffer;
    }
}
