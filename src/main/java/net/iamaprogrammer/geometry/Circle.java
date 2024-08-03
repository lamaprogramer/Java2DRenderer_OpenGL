package net.iamaprogrammer.geometry;

import net.iamaprogrammer.math.Matrix4f;
import net.iamaprogrammer.math.Vector2f;
import net.iamaprogrammer.math.Vector3f;

public class Circle extends Ellipse {
    public Circle(Vector2f position, int r, Vector3f color) {
        super(position, r, r, color);
    }
}
