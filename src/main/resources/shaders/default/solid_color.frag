#version 430 core

in vec4 fColor;
in vec2 uv;

out vec4 FragColor;

void main() {
    FragColor = fColor;
}


// Y = 2.2, where Y is the standard gamma.
// Gamma Encoding (Gamma Compresion) : gammaLinear^Y
// Gamma Decoding (Gamma Expansion)  : gammaEncoded^(1/Y)