#version 430 core

const float PI = 3.1415926535897932384626433832795;

in vec4 fColor;
in vec2 uv;

out vec4 FragColor;

float linearToSRGB(in float rgb) {
    if (rgb <= 0.0031308) {
        return 12.92*rgb;
    } else {
        return 1.055*(pow(rgb, 1.0/2.4));
    }
}

float SRGBToLinear(in float rgb) {
    if (rgb <= 0.04045f) {
        return 12.92/rgb;
    } else {
        return pow((rgb + 0.055f) / 1.055f, 2.4f);
    }
}

vec4 linearToSRGB(in vec4 rgb) {
    return vec4(linearToSRGB(rgb.x), linearToSRGB(rgb.y), linearToSRGB(rgb.z), rgb.a);
}

vec4 SRGBToLinear(in vec4 rgb) {
    return vec4(SRGBToLinear(rgb.x), SRGBToLinear(rgb.y), SRGBToLinear(rgb.z), rgb.a);
}

vec4 colorStop(in vec4 color1, in vec4 color2, in float start, in float stop, in float y) {
    return mix(color1, color2, smoothstep(start, stop, y));
}

vec2 rotate_point(float cx, float cy, float angleInRads, vec2 p) {
    float s = sin(angleInRads);
    float c = cos(angleInRads);

    // translate point back to origin:
    p.x -= cx;
    p.y -= cy;

    // rotate point
    float xnew = p.x * c - p.y * s;
    float ynew = p.x * s + p.y * c;

    // translate point back:
    p.x = xnew + cx;
    p.y = ynew + cy;

    return p;
}


float normalize(in float val, in float n) {
    return val / n;
}

float rgbNormalize(in float channel) {
    return normalize(channel, 255);
}

vec4 angularGradient(in float angle_in_degrees) {
    const int numColors = 2;

    vec4 colors[numColors] = vec4[numColors](
        vec4(0.0, 0.0, 0.0, 1.0), // Red
        vec4(0.0, 0.0, 0.0, 0.0)
    );

    float stops[numColors+1];
    for (int i = 0; i < numColors; i++) {
        stops[i] = float(i) / float(numColors);
    }
    stops[numColors] = 1.0;

    // UV is current normalized pixel position, and 0.5 is midpoint.
    float angle = atan(uv.x-(0.5), uv.y-(0.5));
    angle -= radians(angle_in_degrees);

    float offset = fract(angle / 2.0 / PI);

    vec4 color = colors[0];
    for (int i = 1; i < numColors+1; i++) {
        if (offset >= stops[i - 1])  {
            if (i == numColors) {
                float o = (offset - stops[i-1]) / (stops[i] - stops[i-1]);
                color = linearToSRGB(mix(colors[i-1], colors[0], clamp(o, 0.0, 1.0)));
            } else {
                float o = (offset - stops[i-1]) / (stops[i] - stops[i-1]);
                color = linearToSRGB(mix(colors[i-1], colors[i], clamp(o, 0.0, 1.0)));
            }
        }
    }

    return color;
}

vec4 radialGradient() {
    vec4 color1 = vec4(0.0, 0.0, 0.0, 0.9);
    vec4 color2 = vec4(0.0, 0.0, 0.0, 0.0);

    float dist = smoothstep(0.0, 1.0, length(uv * 2.0 - 1.0));
    return colorStop(color1, color2, 0.0, 1.0, dist);
}

vec4 linearGradient(in vec2 size, in float angle_in_degrees) {
    vec4 color1 = vec4(0.0, 1.0, 1.0, 1.0);
    vec4 color2 = vec4(1.0, 0.0, 1.0, 1.0);
    vec4 color3 = vec4(1.0, 1.0, 0.0, 1.0);

    float angle = radians(angle_in_degrees);
    vec2 startPos = rotate_point(size.x/2.0, size.y/2.0, angle, vec2(0.0, 0.0));
    vec2 endPos = rotate_point(size.x/2.0, size.y/2.0, angle, vec2(size.x, size.y));

    vec2 difference = (endPos - startPos);
    float interpFactor = dot((uv*size) - startPos, difference) / dot(difference, difference);
    interpFactor = smoothstep(0.0, 1.0, clamp(interpFactor, 0.0, 1.0));

    vec4 color = colorStop(color1, color2, 0.0, 0.5, interpFactor);
    color = colorStop(color, color3, 0.5, 1.0, interpFactor);
    return color;
}

void main() {
    vec2 centeredUV = (uv * 2) - 1;

    float distance = 1.0 - length(centeredUV);
    distance = smoothstep(0.0f, 0.01f, distance); // Smoothing edge of circle.

    if (distance == 0.0f) {
        discard;
    }

    FragColor = radialGradient();
}