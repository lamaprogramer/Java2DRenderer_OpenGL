package net.iamaprogrammer.geometry;

import net.iamaprogrammer.math.Matrix4f;
import net.iamaprogrammer.math.Vector2f;
import net.iamaprogrammer.math.Vector3f;
import net.iamaprogrammer.math.Vector4f;

public class Quad extends Geometry {
    public static int[] indices = {
            0, 1, 2,
            0, 2, 3
    };

    public Quad(Vector2f position, int width, int height, Vector3f color) {
        super(new Vertex[]{
                new Vertex(new Vector3f(position.x,          position.y+height, 0), color),  // Bottom Left
                new Vertex(new Vector3f(position.x,             position.y,        0), color),  // Top Left
                new Vertex(new Vector3f(position.x+width,    position.y,        0), color),  // Top Right
                new Vertex(new Vector3f(position.x+width, position.y+height, 0), color)   // Bottom Right
        });
    }

    public Quad(Vector2f position, int width, int height, Vector3f color, String shaderProgramName) {
        super(new Vertex[]{
                new Vertex(new Vector3f(position.x,          position.y+height, 0), color),  // Bottom Left
                new Vertex(new Vector3f(position.x,             position.y,        0), color),  // Top Left
                new Vertex(new Vector3f(position.x+width,    position.y,        0), color),  // Top Right
                new Vertex(new Vector3f(position.x+width, position.y+height, 0), color)   // Bottom Right
        }, shaderProgramName);
    }

    @Override
    public int[] getIndices() {
        return indices;
    }


    //    public Quad(Vector3f color) {
//        super(new Vertex[]{
//            new Vertex(new Vector3f(-1,-1, 0), color),  // Bottom Left
//            new Vertex(new Vector3f(-1, 1, 0), color),  // Top Left
//            new Vertex(new Vector3f( 1, 1, 0), color),  // Top Right
//            new Vertex(new Vector3f( 1,-1, 0), color)   // Bottom Right
//        });
//    }
}
