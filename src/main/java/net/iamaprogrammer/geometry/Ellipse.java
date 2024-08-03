package net.iamaprogrammer.geometry;

import net.iamaprogrammer.math.Matrix4f;
import net.iamaprogrammer.math.Vector2f;
import net.iamaprogrammer.math.Vector3f;

public class Ellipse extends Quad {
    public Ellipse(Vector2f position, int rx, int ry, Vector3f color) {
        super(position, rx, ry, color);
    }
}
