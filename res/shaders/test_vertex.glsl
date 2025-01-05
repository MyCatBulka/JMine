#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 textureCoords;

out vec3 pos;
out vec2 texCoords;

void main(){
    gl_Position = vec4(position, 1.0);
    pos = position;
    texCoords = textureCoords;
}