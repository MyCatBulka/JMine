package com.bulka.java.games.jmine.game.server.level.world;

public class WorldProvider {
    private World world;
    public void init(){
        world = new World();
        world.init();
        generate();
    }
    public void postInit(){
        world.postInit();
    }
    public void generate(){

    }
    public void update(){

    }
    public void render(){

    }
    public void destroy(){

    }
}
