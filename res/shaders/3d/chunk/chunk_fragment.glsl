#version 330 core

in vec3 pos;
in vec2 texCoords;
in float light;

out vec4 fragColor;

uniform sampler2D tex;
uniform float time;

void main(){
    vec4 color = texture(tex, texCoords);
    fragColor = vec4(color.xyz*light, 1.0);
}