package com.bulka.java.games.jmine.engine.graphics.camera;

import com.bulka.java.games.jmine.engine.Engine;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera {
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Vector3f pos = new Vector3f();
    private Vector3f rot = new Vector3f();

    public Camera() {
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }

    public void init(){

    }
    public void postInit(){
        updateProjectionMatrix();
        updateViewMatrix();

    }

    public void updateUniforms(){
        Engine.getEngine().getShaderManager().getTestShader().bind();
        Matrix4f result = new Matrix4f(projectionMatrix).mul(viewMatrix);
        Engine.getEngine().getShaderManager().getTestShader().setUniform("projViewMat", result);
        Engine.getEngine().getShaderManager().getTestShader().unBind();
    }

    public void updateProjectionMatrix(){
        float fov;
        try {
            fov = (float) Math.toRadians(Float.parseFloat(Engine.getEngine().getSettingsManager().get("game.graphics.fov", 67f)));
        } catch (Exception e){
            fov = (float) Math.toRadians(67);
        }
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, (float) Engine.getEngine().getWindow().getAspect(), 0.1f, 1000.0f);

        updateUniforms();
    }

    public void updateViewMatrix(){
        viewMatrix.identity()
                .rotateX((float) Math.toRadians(rot.x))
                .rotateY((float) Math.toRadians(rot.y))
                .rotateZ((float) Math.toRadians(rot.z))
                .translate(-pos.x, -pos.y, -pos.z);

        updateUniforms();
    }

    public Vector3f getRot() {
        return rot;
    }

    public void setRot(Vector3f rot) {
        this.rot = rot;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
