package com.bulka.java.games.jmine.engine.utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class GLUtils {
    public static FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case GL11.GL_INVALID_ENUM:
                return "Invalid Enum";
            case GL11.GL_INVALID_VALUE:
                return "Invalid Value";
            case GL11.GL_INVALID_OPERATION:
                return "Invalid Operation";
            case GL11.GL_STACK_OVERFLOW:
                return "Stack Overflow";
            case GL11.GL_STACK_UNDERFLOW:
                return "Stack Underflow";
            case GL11.GL_OUT_OF_MEMORY:
                return "Out of Memory";
            case GL30.GL_INVALID_FRAMEBUFFER_OPERATION:
                return "Invalid Framebuffer Operation";
            default:
                return "Unknown GL Error (" + errorCode + ")";
        }
    }
}
