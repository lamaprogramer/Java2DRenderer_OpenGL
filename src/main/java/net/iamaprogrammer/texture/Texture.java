package net.iamaprogrammer.texture;

import net.iamaprogrammer.renderer.Sizable;
import net.iamaprogrammer.shader.ShaderProgram;

public interface Texture extends Sizable {
    String getName();
    TextureStorageType getStorageType();
    boolean isEqualTo(Texture texture);

    default void handler(int offset, float[] array) {
        throw new RuntimeException("Method not implemented.");
    }
    default void handler(ShaderProgram program) {
        throw new RuntimeException("Method not implemented.");
    }
}
