package net.iamaprogrammer.util;

import net.iamaprogrammer.math.Matrix4f;
import net.iamaprogrammer.math.Vector2f;
import net.iamaprogrammer.math.Vector3f;
import net.iamaprogrammer.math.Vector4f;

public class MatrixUtil {
    public static Matrix4f modelMatrix(Vector3f position, float angle, Vector3f rotation, Vector3f scale) {
        return Matrix4f.translate(position.x, position.y, position.z).multiply(
                Matrix4f.rotate(angle, rotation.x, rotation.y, rotation.z).multiply(
                        Matrix4f.scale(scale.x, scale.y, scale.z)
                )
        );
    }

    public static Matrix4f modelMatrix(Vector3f position, Vector3f scale) {
        return modelMatrix(position, 0.0f, new Vector3f(1.0f, 1.0f, 1.0f), scale);
    }

    // Moves origin to the top-left corner of the geometry.
    public static Matrix4f modelMatrix2D(Vector2f position, int width, int height) {
        return modelMatrix(new Vector3f(position.x+(width/2.0f), position.y+(height/2.0f), 0.0f), new Vector3f(width/2.0f, height/2.0f, 0.0f));
    }
}
