package net.iamaprogrammer.texture;

import net.iamaprogrammer.math.Vector2f;
import net.iamaprogrammer.math.Vector4f;
import net.iamaprogrammer.renderer.Gradient;
import net.iamaprogrammer.shader.ShaderProgram;

import java.util.Objects;

public class GradientTexture implements Texture {
    private final String name;
    private final int type;
    private final float rotation;
    private final boolean gammaCorrection;
    private final Vector4f[] colors;

    public GradientTexture(String name, Gradient gradient) {
        this.name = name;
        this.type = gradient.getPattern().getType();
        this.rotation = gradient.getAngle();
        this.gammaCorrection = gradient.hasGammaCorrection();
        this.colors = gradient.getColors();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public TextureStorageType getStorageType() {
        return TextureStorageType.UNIFORM;
    }

    @Override
    public boolean isEqualTo(Texture texture) {
        return Objects.equals(this.getName(), texture.getName());
    }

    @Override
    public void handler(ShaderProgram program) {
        program.setUniform("type", this.type);
        program.setUniform("gammaCorrection", this.gammaCorrection ? 1 : 0);
        program.setUniform("gradientRotation", this.rotation);
        program.setUniform("numColors", this.colors.length);
        program.setUniform("colors", this.colors);
    }

    @Override
    public int getSize() {
        return this.colors.length * 4;
    }

    @Override
    public int getMemSize() {
        return this.getSize() * Float.BYTES;
    }
}
