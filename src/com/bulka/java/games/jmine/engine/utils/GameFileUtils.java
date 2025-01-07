package com.bulka.java.games.jmine.engine.utils;

import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class GameFileUtils {
    public static String loadResourceAsString(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(GameFileUtils.class.getResourceAsStream(path)))){
            while (true){
                String line = reader.readLine();
                if(line == null)
                    break;
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    public static ByteBuffer ioResourceToByteBuffer(InputStream is) throws Exception{
        ByteBuffer buffer;
        ReadableByteChannel rbc = Channels.newChannel(is);
        buffer = BufferUtils.createByteBuffer(is.available());

        int bytes;
        while (true) {
            bytes = rbc.read(buffer);
            if (bytes == -1) break;
            if (buffer.remaining() == 0)
                buffer = resizeBuffer(buffer, buffer.capacity() * 2);
        }

        buffer.flip();
        return buffer;
    }

    public static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
