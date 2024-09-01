#version 430 core

in vec4 fColor;
in vec2 uv;

out vec4 FragColor;

void main() {
    vec2 centeredUV = (uv * 2) - 1;

    float distance = 1.0 - length(centeredUV);
    distance = smoothstep(0.0f, 0.01f, distance); // Smoothing edge of circle.

    if (distance == 0.0f) {
        discard;
    }

    FragColor = vec4(fColor.rgb, fColor.a);
}