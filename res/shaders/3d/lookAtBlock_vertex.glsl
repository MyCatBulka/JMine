#version 330 core

layout(location = 0) in vec3 position;

out vec3 pos;

uniform mat4 projViewMat;
uniform mat4 worldPosMat;

void main(){
    gl_Position = projViewMat * worldPosMat * vec4(position.x, position.y, position.z, 1.0);

    pos = position;
}