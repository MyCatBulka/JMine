#version 330 core

in vec3 pos;
in vec2 texCoords;

out vec4 outColor;

uniform sampler2D tex;
uniform float time;

void main(){
    outColor = texture(tex, texCoords);
//    outColor = vec4(pos, 1.0);
}