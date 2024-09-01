package net.iamaprogrammer.math;

public class MathUtil {
    public static float normalize(float value, float upperBound) {
        return value/upperBound;
    }

    public static float normalizeRGB(float value) {
        return normalize(value, 255);
    }
}
