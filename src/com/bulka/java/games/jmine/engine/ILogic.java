package com.bulka.java.games.jmine.engine;

public interface ILogic {
    default void init(){}
    default void postInit(){}
    default void preUpdate(){}
    default void update(){}
    default void postUpdate(){}
    default void preRender(){}
    default void render(){}
    default void postRender(){}
    default void destroy(){}
}
