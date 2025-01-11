package com.bulka.java.games.jmine.game.server.blocks;

import com.bulka.java.games.jmine.game.Game;

import java.util.HashMap;
import java.util.Map;

public class Blocks {
    private Map<Integer, Block> blocks = new HashMap<>();

    public void init(){
        Block air = new Block(0, true, false,15, 15);
        air.recalcFaces();
        blocks.put(0, air);
        Block stone = new Block(1, false, true,0, 0);
//        Block stone = new Block(1, false,
//                1, 0,
//                1, 0,
//                1, 0,
//                1, 0,
//                1, 0,
//                1, 0
//        );
        stone.recalcFaces();
        blocks.put(1, stone);

        Block dirt = new Block(2, false, true, 3, 0);
        dirt.recalcFaces();
        blocks.put(2, dirt);

        Block grass = new Block(3, false, true,
                1, 0,
                1, 0,
                1, 0,
                1, 0,
                2, 0,
                3, 0
        );
        grass.recalcFaces();
        blocks.put(3, grass);
    }

    public Block getBlock(int id){
        return blocks.get(id);
    }

    public void load(){

    }

    public void destroy(){

    }

    public static Blocks getSelf(){
        return Game.getSelf().getBlocks();
    }
}
