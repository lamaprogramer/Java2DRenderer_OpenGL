#version 430 core
#define MAX_COLORS 16
#define PI 3.1415926535897932384626433832795

in vec4 fColor;
in vec2 uv;

in flat int gType;
in float gRotation;
in flat int gGammaCorrection;
in flat int gNumColors;
in vec4 gColors[MAX_COLORS];

out vec4 FragColor;

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

float gradientNoise(in vec2 uv) {
    const vec3 magic = vec3(0.06711056, 0.00583715, 52.9829189);
    return fract(magic.z * fract(dot(uv, magic.xy)));
}

vec4 angularGradient(in vec4 colors[MAX_COLORS], in int numColors, in float angle_in_degrees, in int gammaCorrection) {
    float stops[MAX_COLORS+1];
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
                color = mix(colors[i-1], colors[0], clamp(o, 0.0, 1.0));
            } else {
                float o = (offset - stops[i-1]) / (stops[i] - stops[i-1]);
                color = mix(colors[i-1], colors[i], clamp(o, 0.0, 1.0));
            }
        }
    }

    if (gammaCorrection == 1) {
        return vec4(pow(color.rgb, vec3(1.0/2.2)), color.a);
    }

    return color;
}

vec4 radialGradient(in vec4 colors[MAX_COLORS], in int numColors, in int gammaCorrection) {
    float dist = smoothstep(0.0, 1.0, length(uv * 2.0 - 1.0));
    vec4 color = colors[0];

    float offset = 0.0;
    float offset2 = 1.0/(numColors-1);
    for (int i = 1; i < numColors; i++) {
        color = colorStop(color, colors[i], offset, offset2, dist);

        offset += offset2;
        offset2 += 1.0/(numColors-1);
    }
    if (gammaCorrection == 1) {
        return vec4(pow(color.rgb, vec3(1.0/2.2)), color.a);
    }
    return color;
}

vec4 linearGradient(in vec4 colors[MAX_COLORS], in int numColors, in float angle_in_degrees, in int gammaCorrection) {
    float angle = radians(angle_in_degrees);
    vec2 startPos = rotate_point(0.5, 0.5, angle, vec2(0.0, 0.0));
    vec2 endPos = rotate_point(0.5, 0.5, angle, vec2(1.0, 1.0));

    vec2 difference = (endPos - startPos);
    float interpFactor = dot((uv*1.0) - startPos, difference) / dot(difference, difference);
    interpFactor = smoothstep(0.0, 1.0, clamp(interpFactor, 0.0, 1.0));

    vec4 color = colors[0];

    float offset = 0.0;
    float offset2 = 1.0/(numColors-1.0);
    for (int i = 1; i < numColors; i++) {
        color = colorStop(color, colors[i], offset, offset2, interpFactor);

        offset += offset2;
        offset2 += 1.0/(numColors-1.0);
    }

    if (gammaCorrection == 1) {
        return vec4(pow(color.rgb, vec3(1.0/2.2)), color.a);
    }
    return color;
}

void main() {
    if (gType == 0) {
        FragColor = linearGradient(gColors, gNumColors, gRotation, gGammaCorrection);
    } else if (gType == 1) {
        FragColor = radialGradient(gColors, gNumColors, gGammaCorrection);
    } else if (gType == 2) {
        FragColor = angularGradient(gColors, gNumColors, gRotation, gGammaCorrection);
    }
}