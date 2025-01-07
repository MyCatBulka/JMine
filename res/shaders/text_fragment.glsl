#version 330 core

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D fontTexture;
uniform vec4 textColor;

void main() {
    float c = texture(fontTexture, texCoord).r;
    fragColor = r * textColor;
}