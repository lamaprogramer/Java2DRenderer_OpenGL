package net.iamaprogrammer.renderer;

import net.iamaprogrammer.math.Vector4f;

public class Gradient {
    private final Pattern pattern;
    private final float angle;
    private final boolean gammaCorrection;
    private final Vector4f[] colors;

    public Gradient(Pattern pattern, float angle, Vector4f[] colors, boolean gammaCorrection) {
        this.pattern = pattern;
        this.angle = angle;
        this.gammaCorrection = gammaCorrection;
        this.colors = colors;
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public float getAngle() {
        return this.angle;
    }

    public Vector4f[] getColors() {
        return this.colors;
    }

    public boolean hasGammaCorrection() {
        return this.gammaCorrection;
    }

    public enum Pattern {
        LINEAR(0),
        RADIAL(1),
        ANGULAR(2);

        final int type;
        Pattern(int type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }
    }
}
