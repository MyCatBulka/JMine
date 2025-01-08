#version 330 core

layout (location = 0) in vec2 aPos;
layout (location = 1) in vec2 aTexCoord;

out vec2 texCoord;

uniform mat4 orthoProj;
uniform mat4 normOrthoProj;

void main() {
    gl_Position = orthoProj * vec4(aPos, 1.0, 1.0);
    texCoord = aTexCoord;
}