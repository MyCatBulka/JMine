package com.bulka.java.games.jmine.game.server.level.world;

import com.bulka.java.games.jmine.engine.graphics.camera.Hero;
import com.bulka.java.games.jmine.engine.graphics.mesh.BasicLinesMesh;
import com.bulka.java.games.jmine.engine.graphics.shaders.ShaderManager;
import com.bulka.java.games.jmine.game.server.blocks.Block;
import com.bulka.java.games.jmine.game.server.blocks.Blocks;
import com.bulka.java.games.jmine.game.server.blocks.Face;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HintRenderer {
    private Logger logger = Logger.getLogger(HintRenderer.class.getName());
    private Matrix4f lookAtMatrix = new Matrix4f();
    private FloatBuffer lookAtMatrixBuffer;
    private BasicLinesMesh lookAtMesh = new BasicLinesMesh();
    private boolean isLookingAtBlock = false;
    private Vector3i lookingAtBlockCords = new Vector3i();
    private short lookingAtBlockID = 0;
    private Block lookingAtBlockBlock = null;
    private Face lookingAtBlockFace = null;
    private int lookingDistance = 5;

    public void init(){

    }

    public void update(){
        isLookingAtBlock = false;
        lookingAtBlockBlock = null;
        lookingAtBlockID = -1;
        Vector3f rayPos = new Vector3f(Hero.getSelf().getPosition());
        Vector3f rayDir = new Vector3f(Hero.getSelf().getDirection());
        float step = Face.BLOCK_TEXTURE_WIDTH_FLOAT;

        for (float distance = 0f; distance <= lookingDistance; distance += step) {
            int blockX = (int) Math.floor(rayPos.x);
            int blockY = (int) Math.floor(rayPos.y);
            int blockZ = (int) Math.floor(rayPos.z);
            short currentBlock = WorldProvider.getSelf().getWorld().getBlock(blockX, blockY, blockZ);
            if (currentBlock != -1) {
                Block block = Blocks.getSelf().getBlock(currentBlock);
                if (block.isFocusable()) {
                    setLookAtBlock(currentBlock, blockX, blockY, blockZ);

                    int faceLookingAt = getFaceLookingAt(rayPos, blockX, blockY, blockZ);
                    if (faceLookingAt != -1)
                        lookingAtBlockFace = block.getSide(faceLookingAt);
                    else
                        lookingAtBlockFace = null;
                    isLookingAtBlock = true;
                    return;
                }
            }

            rayPos.add(new Vector3f(rayDir).mul(step));
        }
    }
    private int getFaceLookingAt(Vector3f hitPoint, int blockX, int blockY, int blockZ) {
        float epsilon = Face.BLOCK_TEXTURE_WIDTH_FLOAT;
        if (Math.abs(hitPoint.x - blockX) < epsilon) {
            return 2;
        } else if (Math.abs(hitPoint.x - (blockX + 1)) < epsilon) {
            return 3;
        } else if (Math.abs(hitPoint.y - blockY) < epsilon) {
            return 5;
        } else if (Math.abs(hitPoint.y - (blockY + 1)) < epsilon) {
            return 4;
        } else if (Math.abs(hitPoint.z - blockZ) < epsilon) {
            return 1;
        } else if (Math.abs(hitPoint.z - (blockZ + 1)) < epsilon) {
            return 0;
        }

        return -1;
    }

    private static boolean isRayIntersectingBlock(Vector3f rayPos, Vector3f rayDir, Block block) {
        float blockMinX = block.getxMin();
        float blockMaxX = block.getxMax();
        float blockMinY = block.getyMin();
        float blockMaxY = block.getyMax();
        float blockMinZ = block.getzMin();
        float blockMaxZ = block.getzMax();

        return checkIntersection(rayPos, rayDir, blockMinX, blockMaxX, blockMinY, blockMaxY, blockMinZ, blockMaxZ);
    }

    private static boolean checkIntersection(Vector3f rayPos, Vector3f rayDir,
                                             float minX, float maxX,
                                             float minY, float maxY,
                                             float minZ, float maxZ) {
        float tMin = (minX - rayPos.x) / rayDir.x;
        float tMax = (maxX - rayPos.x()) / rayDir.x;
        if (tMin > tMax) {
            float temp = tMin;
            tMin = tMax;
            tMax = temp;
        }

        float tYMin = (minY - rayPos.y) / rayDir.y;
        float tYMax = (maxY - rayPos.y) / rayDir.y;
        if (tYMin > tYMax) {
            float temp = tYMin;
            tYMin = tYMax;
            tYMax = temp;
        }

        if ((tMin > tYMax) || (tYMin > tMax)) return false;
        tMin = Math.max(tMin, tYMin);
        tMax = Math.min(tMax, tYMax);

        float tZMin = (minZ - rayPos.z) / rayDir.z;
        float tZMax = (maxZ - rayPos.z) / rayDir.z;
        if (tZMin > tZMax) {
            float temp = tZMin;
            tZMin = tZMax;
            tZMax = temp;
        }

        if ((tMin > tZMax) || (tZMin > tMax)) return false;
        tMin = Math.max(tMin, tZMin);
        tMax = Math.min(tMax, tZMax);

        return tMax >= 0;
    }
    public void setLookAtBlock(short id, int x, int y, int z) {
        if(x == lookingAtBlockCords.x && y == lookingAtBlockCords.y && z == lookingAtBlockCords.z)
            return;
        lookingAtBlockBlock = Blocks.getSelf().getBlock(id);
        if (lookingAtBlockCords.x != x || lookingAtBlockCords.y != y || lookingAtBlockCords.z != z || lookingAtBlockID != id) {
            Block block = Blocks.getSelf().getBlock(id);
            List<Vector3f> vertices = new ArrayList<>();
            float offset = 0.003f;
            Vector3f vec0;
            Vector3f vec1;
            Vector3f vec2;
            Vector3f vec3;
            for (int i = 0; i < block.getSides().length; i++) {
                switch (i) {
                    case 0: {
                        vec0 = new Vector3f(
                                block.getSide(i).getFace()[0].getX() - offset,
                                block.getSide(i).getFace()[0].getY() - offset,
                                block.getSide(i).getFace()[0].getZ() + offset
                        );
                        vec1 = new Vector3f(
                                block.getSide(i).getFace()[1].getX() + offset,
                                block.getSide(i).getFace()[1].getY() - offset,
                                block.getSide(i).getFace()[1].getZ() + offset
                        );
                        vec2 = new Vector3f(
                                block.getSide(i).getFace()[2].getX() + offset,
                                block.getSide(i).getFace()[2].getY() + offset,
                                block.getSide(i).getFace()[2].getZ() + offset
                        );
                        vec3 = new Vector3f(
                                block.getSide(i).getFace()[3].getX() - offset,
                                block.getSide(i).getFace()[3].getY() + offset,
                                block.getSide(i).getFace()[3].getZ() + offset
                        );
                        vertices.add(vec0);
                        vertices.add(vec1);
                        vertices.add(vec1);
                        vertices.add(vec2);
                        vertices.add(vec2);
                        vertices.add(vec3);
                        vertices.add(vec3);
                        vertices.add(vec0);
                        break;
                    }
                    case 1: {
                        vec0 = new Vector3f(
                                block.getSide(i).getFace()[3].getX() - offset,
                                block.getSide(i).getFace()[3].getY() - offset,
                                block.getSide(i).getFace()[3].getZ() - offset
                        );
                        vec1 = new Vector3f(
                                block.getSide(i).getFace()[2].getX() + offset,
                                block.getSide(i).getFace()[2].getY() - offset,
                                block.getSide(i).getFace()[2].getZ() - offset
                        );
                        vec2 = new Vector3f(
                                block.getSide(i).getFace()[1].getX() + offset,
                                block.getSide(i).getFace()[1].getY() + offset,
                                block.getSide(i).getFace()[1].getZ() - offset
                        );
                        vec3 = new Vector3f(
                                block.getSide(i).getFace()[0].getX() - offset,
                                block.getSide(i).getFace()[0].getY() + offset,
                                block.getSide(i).getFace()[0].getZ() - offset
                        );
                        vertices.add(vec0);
                        vertices.add(vec1);
                        vertices.add(vec1);
                        vertices.add(vec2);
                        vertices.add(vec2);
                        vertices.add(vec3);
                        vertices.add(vec3);
                        vertices.add(vec0);
                        break;
                    }
                    case 2: {
                        vec0 = new Vector3f(
                                block.getSide(i).getFace()[0].getX() - offset,
                                block.getSide(i).getFace()[0].getY() - offset,
                                block.getSide(i).getFace()[0].getZ() - offset
                        );
                        vec1 = new Vector3f(
                                block.getSide(i).getFace()[1].getX() - offset,
                                block.getSide(i).getFace()[1].getY() - offset,
                                block.getSide(i).getFace()[1].getZ() + offset
                        );
                        vec2 = new Vector3f(
                                block.getSide(i).getFace()[2].getX() - offset,
                                block.getSide(i).getFace()[2].getY() + offset,
                                block.getSide(i).getFace()[2].getZ() + offset
                        );
                        vec3 = new Vector3f(
                                block.getSide(i).getFace()[3].getX() - offset,
                                block.getSide(i).getFace()[3].getY() + offset,
                                block.getSide(i).getFace()[3].getZ() - offset
                        );
                        vertices.add(vec0);
                        vertices.add(vec1);
                        vertices.add(vec1);
                        vertices.add(vec2);
                        vertices.add(vec2);
                        vertices.add(vec3);
                        vertices.add(vec3);
                        vertices.add(vec0);
                        break;
                    }
                    case 3: {
                        vec0 = new Vector3f(
                                block.getSide(i).getFace()[3].getX() + offset,
                                block.getSide(i).getFace()[3].getY() - offset,
                                block.getSide(i).getFace()[3].getZ() - offset
                        );
                        vec1 = new Vector3f(
                                block.getSide(i).getFace()[2].getX() + offset,
                                block.getSide(i).getFace()[2].getY() - offset,
                                block.getSide(i).getFace()[2].getZ() + offset
                        );
                        vec2 = new Vector3f(
                                block.getSide(i).getFace()[1].getX() + offset,
                                block.getSide(i).getFace()[1].getY() + offset,
                                block.getSide(i).getFace()[1].getZ() + offset
                        );
                        vec3 = new Vector3f(
                                block.getSide(i).getFace()[0].getX() + offset,
                                block.getSide(i).getFace()[0].getY() + offset,
                                block.getSide(i).getFace()[0].getZ() - offset
                        );
                        vertices.add(vec0);
                        vertices.add(vec1);
                        vertices.add(vec1);
                        vertices.add(vec2);
                        vertices.add(vec2);
                        vertices.add(vec3);
                        vertices.add(vec3);
                        vertices.add(vec0);
                        break;
                    }
                    case 4: {
                        vec0 = new Vector3f(
                                block.getSide(i).getFace()[0].getX() - offset,
                                block.getSide(i).getFace()[0].getY() + offset,
                                block.getSide(i).getFace()[0].getZ() + offset
                        );
                        vec1 = new Vector3f(
                                block.getSide(i).getFace()[1].getX() + offset,
                                block.getSide(i).getFace()[1].getY() + offset,
                                block.getSide(i).getFace()[1].getZ() + offset
                        );
                        vec2 = new Vector3f(
                                block.getSide(i).getFace()[2].getX() + offset,
                                block.getSide(i).getFace()[2].getY() + offset,
                                block.getSide(i).getFace()[2].getZ() - offset
                        );
                        vec3 = new Vector3f(
                                block.getSide(i).getFace()[3].getX() - offset,
                                block.getSide(i).getFace()[3].getY() + offset,
                                block.getSide(i).getFace()[3].getZ() - offset
                        );
                        vertices.add(vec0);
                        vertices.add(vec1);
                        vertices.add(vec1);
                        vertices.add(vec2);
                        vertices.add(vec2);
                        vertices.add(vec3);
                        vertices.add(vec3);
                        vertices.add(vec0);
                        break;
                    }
                    case 5: {
                        vec0 = new Vector3f(
                                block.getSide(i).getFace()[3].getX() - offset,
                                block.getSide(i).getFace()[3].getY() - offset,
                                block.getSide(i).getFace()[3].getZ() + offset
                        );
                        vec1 = new Vector3f(
                                block.getSide(i).getFace()[2].getX() + offset,
                                block.getSide(i).getFace()[2].getY() - offset,
                                block.getSide(i).getFace()[2].getZ() + offset
                        );
                        vec2 = new Vector3f(
                                block.getSide(i).getFace()[1].getX() + offset,
                                block.getSide(i).getFace()[1].getY() - offset,
                                block.getSide(i).getFace()[1].getZ() - offset
                        );
                        vec3 = new Vector3f(
                                block.getSide(i).getFace()[0].getX() - offset,
                                block.getSide(i).getFace()[0].getY() - offset,
                                block.getSide(i).getFace()[0].getZ() - offset
                        );
                        vertices.add(vec0);
                        vertices.add(vec1);
                        vertices.add(vec1);
                        vertices.add(vec2);
                        vertices.add(vec2);
                        vertices.add(vec3);
                        vertices.add(vec3);
                        vertices.add(vec0);
                        break;
                    }

                }
            }
            lookAtMesh = new BasicLinesMesh(vertices.toArray(new Vector3f[0]));
            lookAtMesh.destroy();
            lookAtMesh.create();

            lookAtMatrix.identity();
            lookAtMatrix.translate(x, y, z);
            ShaderManager.getSelf().getLookAtBlockShader().bind();
            lookAtMatrixBuffer = MemoryUtil.memAllocFloat(16);
            lookAtMatrix.get(lookAtMatrixBuffer);
            ShaderManager.getSelf().getLookAtBlockShader().setUniformMat4f("worldPosMat", lookAtMatrixBuffer);
            ShaderManager.getSelf().getLookAtBlockShader().unBind();
            lookingAtBlockCords.set(x, y, z);
            lookingAtBlockID = id;
        }
    }

    public void render(){
        if(isLookingAtBlock) {
            GL30.glBindVertexArray(lookAtMesh.getVAO());
            GL30.glEnableVertexAttribArray(0);
            ShaderManager.getSelf().getLookAtBlockShader().bind();
            GL11.glDrawArrays(GL11.GL_LINES, 0, lookAtMesh.getVertices().length);
            ShaderManager.getSelf().getLookAtBlockShader().unBind();

            GL30.glDisableVertexAttribArray(0);
            GL30.glBindVertexArray(0);
        }
    }
    public void destroy(){

    }

    public int getLookingDistance() {
        return lookingDistance;
    }

    public Face getLookingAtBlockFace() {
        return lookingAtBlockFace;
    }

    public Block getLookingAtBlockBlock() {
        return lookingAtBlockBlock;
    }

    public short getLookingAtBlockID() {
        return lookingAtBlockID;
    }

    public Vector3i getLookingAtBlockCords() {
        return lookingAtBlockCords;
    }

    public boolean isLookingAtBlock() {
        return isLookingAtBlock;
    }

    public BasicLinesMesh getLookAtMesh() {
        return lookAtMesh;
    }

    public FloatBuffer getLookAtMatrixBuffer() {
        return lookAtMatrixBuffer;
    }

    public Matrix4f getLookAtMatrix() {
        return lookAtMatrix;
    }

    public void setLookingDistance(int lookingDistance) {
        this.lookingDistance = lookingDistance;
    }
}
