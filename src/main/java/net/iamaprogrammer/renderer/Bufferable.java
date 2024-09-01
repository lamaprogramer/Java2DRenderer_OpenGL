package net.iamaprogrammer.renderer;

public interface Bufferable extends Sizable {
    void toArray(int offset, float[] array);
}
