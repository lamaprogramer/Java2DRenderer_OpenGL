#version 430 core
#define MAX_COLORS 16

struct GeometryModelData {
    mat4 modelMatrix;
    vec4 color;
};

layout (std430, binding = 2) buffer ModelData {
    GeometryModelData modelsData[];
};

layout (location = 0) in vec4 aPos;
layout (location = 1) in vec2 aUV;

uniform mat4 perspective;
uniform mat4 view;

uniform int type;
uniform float gradientRotation;
uniform int gammaCorrection;
uniform int numColors;
uniform vec4 colors[MAX_COLORS];

out vec4 fColor;
out vec2 uv;

out flat int gType;
out float gRotation;
out flat int gGammaCorrection;
out flat int gNumColors;
out vec4 gColors[MAX_COLORS];

void main() {
    GeometryModelData instance = modelsData[gl_InstanceID];
    vec4 modelSpace = instance.modelMatrix * aPos;

    fColor = instance.color;
    uv = aUV;

    gType = type;
    gRotation = gradientRotation;
    gGammaCorrection = gammaCorrection;
    gNumColors = numColors;
    if (gammaCorrection == 1) {
        for (int i = 0; i < colors.length(); i++) {
            gColors[i] = vec4(pow(colors[i].rgb, vec3(2.2)), colors[i].a);
        }
    } else {
        gColors = colors;
    }

    gl_Position = perspective * mat4(1.0) * modelSpace;
}