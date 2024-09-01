package net.iamaprogrammer.geometry;

import net.iamaprogrammer.math.Matrix4f;
import net.iamaprogrammer.math.Vector2f;
import net.iamaprogrammer.math.Vector3f;
import net.iamaprogrammer.math.Vector4f;
import net.iamaprogrammer.texture.Texture;

public class Circle extends Ellipse {
    public Circle(Vector2f position, int r, Texture texture) {
        super(position, r, r, texture);
    }
    public Circle(Vector2f position, int r, Texture texture, String shaderProgram) {
        super(position, r, r, texture, shaderProgram);
    }
}
