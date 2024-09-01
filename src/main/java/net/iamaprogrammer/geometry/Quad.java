package net.iamaprogrammer.geometry;

import net.iamaprogrammer.math.Matrix4f;
import net.iamaprogrammer.math.Vector2f;
import net.iamaprogrammer.math.Vector3f;
import net.iamaprogrammer.math.Vector4f;
import net.iamaprogrammer.texture.Texture;
import net.iamaprogrammer.util.MatrixUtil;

public class Quad extends Geometry {
    public static Vertex[] VERTICES = new Vertex[]{
            new Vertex(new Vector3f(-1,-1, 0), new Vector2f(0, 0)),  // Bottom Left
            new Vertex(new Vector3f(-1, 1, 0), new Vector2f(0, 1)),  // Top Left
            new Vertex(new Vector3f( 1, 1, 0), new Vector2f(1, 1)),  // Top Right
            new Vertex(new Vector3f( 1,-1, 0), new Vector2f(1, 0))   // Bottom Right
    };

    public static int[] INDICES = {
            0, 1, 2,
            0, 2, 3
    };

    public Quad(Vector2f position, int width, int height, Vector4f color) {
        super(
            VERTICES,
            null,
            new GeometryInstanceData(
                MatrixUtil.modelMatrix2D(position, width, height).transpose(),
                color
            )
        );
    }

    public Quad(Vector2f position, int width, int height, Texture texture) {
        super(
            VERTICES,
            texture,
            new GeometryInstanceData(
                MatrixUtil.modelMatrix2D(position, width, height).transpose(),
                new Vector4f(0.0f, 0.0f, 0.0f, 1.0f)
            )
        );
    }


    public Quad(Vector2f position, int width, int height, Texture texture, String shaderProgramName) {
        super(
            VERTICES,
            texture,
            new GeometryInstanceData(
                MatrixUtil.modelMatrix2D(position, width, height).transpose(),
                    new Vector4f(0.0f, 0.0f, 0.0f, 1.0f)
            ),
            shaderProgramName
        );
    }

    @Override
    public int[] getIndices() {
        return INDICES;
    }
}
