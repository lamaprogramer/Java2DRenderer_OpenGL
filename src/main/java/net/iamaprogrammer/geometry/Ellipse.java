package net.iamaprogrammer.geometry;

import net.iamaprogrammer.math.Matrix4f;
import net.iamaprogrammer.math.Vector2f;
import net.iamaprogrammer.math.Vector3f;
import net.iamaprogrammer.math.Vector4f;
import net.iamaprogrammer.texture.Texture;

public class Ellipse extends Quad {
    public Ellipse(Vector2f position, int rx, int ry, Texture texture) {
        super(position, rx, ry, texture, "circle_program");
    }

    public Ellipse(Vector2f position, int rx, int ry, Texture texture, String shaderProgram) {
        super(position, rx, ry, texture, shaderProgram);
    }
}
