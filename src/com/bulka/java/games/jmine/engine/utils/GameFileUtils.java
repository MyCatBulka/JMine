package com.bulka.java.games.jmine.engine.utils;

import java.io.*;

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
}
