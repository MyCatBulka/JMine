#version 330 core

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D fontTexture;
uniform vec4 textColor;
uniform vec4 bgColor;

void main() {
    float c = texture(fontTexture, texCoord).r;
    fragColor = textColor*c+bgColor;
}