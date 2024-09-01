package net.iamaprogrammer.texture;

import net.iamaprogrammer.math.Vector4f;

public class SolidColorTexture implements Texture {
    private Vector4f color;

    public SolidColorTexture(Vector4f color) {
        this.color = color;
    }

    @Override
    public String getName() {
        return "solid_color";
    }

    @Override
    public TextureStorageType getStorageType() {
        return TextureStorageType.SSBO;
    }

    @Override
    public boolean isEqualTo(Texture texture) {
        return true;
    }

    @Override
    public void handler(int offset, float[] array) {
        this.color.toArray(offset, array);
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public int getMemSize() {
        return this.getSize() * Float.BYTES;
    }
}
