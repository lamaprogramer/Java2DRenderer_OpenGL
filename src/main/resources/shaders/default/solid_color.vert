#version 430 core

struct GeometryModelData {
    mat4 modelMatrix;
    vec4 color;
};

layout (location = 0) in vec4 aPos;
layout (location = 1) in vec2 aUV;

layout (std430, binding = 2) buffer ModelData {
    GeometryModelData modelsData[];
};

uniform mat4 perspective;
uniform mat4 view;

out vec4 fColor;
out vec2 uv;

void main() {
    GeometryModelData instance = modelsData[gl_InstanceID];
    vec4 modelSpace = instance.modelMatrix * aPos;

    fColor = instance.color;
    uv = aUV;

    gl_Position = perspective * mat4(1.0) * modelSpace;
}