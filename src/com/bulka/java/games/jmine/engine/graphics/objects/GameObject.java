package com.bulka.java.games.jmine.engine.graphics.objects;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.graphics.shaders.ShaderManager;
import com.bulka.java.games.jmine.engine.graphics.textures.Texture;
import com.bulka.java.games.jmine.engine.graphics.mesh.Mesh;
import com.bulka.java.games.jmine.engine.graphics.render.BasicRenderer;
import com.bulka.java.games.jmine.engine.graphics.shaders.Shader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class GameObject {
    private Mesh mesh;
    private int texture;
    private Matrix4f modelMatrix = new Matrix4f();
    private Shader shader;

    public GameObject() {

    }

    public GameObject(Mesh mesh) {
        this.mesh = mesh;
        modelMatrix = new Matrix4f();
    }

    public GameObject(Mesh mesh, Matrix4f modelMatrix) {
        this.mesh = mesh;
        this.modelMatrix = modelMatrix;
    }

    public GameObject(Mesh mesh,Matrix4f modelMatrix, Shader shader) {
        this.mesh = mesh;
        this.modelMatrix = modelMatrix;
        this.shader = shader;
    }

    public GameObject(Mesh mesh, int texture) {
        this.mesh = mesh;
        this.texture = texture;
    }

    public GameObject(Mesh mesh, int texture, Matrix4f modelMatrix, Shader shader) {
        this.mesh = mesh;
        this.texture = texture;
        this.modelMatrix = modelMatrix;
        this.shader = shader;
    }

    public void init(){

    }

    public void postInit(){
        shader = ShaderManager.getSelf().getTestShader();
    }

    public void update(){

    }
    public void render(){
        BasicRenderer.render(this);
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public int getTexture() {
        return texture;
    }

    public void setTexture(int texture) {
        this.texture = texture;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public void setModelMatrix(Matrix4f modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public void destroy(){
        mesh.destroy();
        GL11.glDeleteTextures(texture);
    }
}
