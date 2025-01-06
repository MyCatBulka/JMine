package com.bulka.java.games.jmine.engine.graphics.objects;

import com.bulka.java.games.jmine.engine.Engine;
import com.bulka.java.games.jmine.engine.graphics.material.Material;
import com.bulka.java.games.jmine.engine.graphics.mesh.Mesh;
import com.bulka.java.games.jmine.engine.graphics.render.BasicRenderer;
import com.bulka.java.games.jmine.engine.graphics.shaders.Shader;
import org.joml.Matrix4f;

public class GameObject {
    private Mesh mesh;
    private Material material;
    private Matrix4f modelMatrix;
    private Shader shader;

    public GameObject() {

    }

    public GameObject(Mesh mesh) {
        this.mesh = mesh;
        modelMatrix = new Matrix4f();
    }

    public GameObject(Mesh mesh, Material material) {
        this.mesh = mesh;
        this.material = material;
        modelMatrix = new Matrix4f();
    }

    public GameObject(Mesh mesh, Material material, Matrix4f modelMatrix) {
        this.mesh = mesh;
        this.material = material;
        this.modelMatrix = modelMatrix;
    }

    public GameObject(Mesh mesh, Material material, Matrix4f modelMatrix, Shader shader) {
        this.mesh = mesh;
        this.material = material;
        this.modelMatrix = modelMatrix;
        this.shader = shader;
    }

    public void init(){

    }

    public void postInit(){
        shader = Engine.getEngine().getShaderManager().getTestShader();
    }

    public void update(){

    }
    public void render(){
        BasicRenderer.renderMesh(mesh, shader, material, modelMatrix);
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
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
        material.destroy();
    }
}
