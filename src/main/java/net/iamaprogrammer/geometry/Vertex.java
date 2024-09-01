package net.iamaprogrammer.geometry;

import net.iamaprogrammer.math.Vector2f;
import net.iamaprogrammer.math.Vector3f;
import net.iamaprogrammer.math.Vector4f;

public class Vertex {
    private static final int SIZE = 8;

    private final float x;
    private final float y;
    private final float z;
    private final float w;

    private final float u;
    private final float v;

    public Vertex(Vector3f position, Vector2f uv) {
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
        this.w = 1.0f;

        this.u = uv.x;
        this.v = uv.y;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }

    public static float[] asBuffer(Vertex[] vertices) {
        int bufferSize = Vertex.size() * vertices.length;
        float[] vertexBuffer = new float[bufferSize];

        for (int i = 0; i < vertices.length; i++) {
            Vertex vertex = vertices[i];
            int row = Vertex.size() * i;

            vertexBuffer[row]     = vertex.getX();
            vertexBuffer[row + 1] = vertex.getY();
            vertexBuffer[row + 2] = vertex.getZ();
            vertexBuffer[row + 3] = vertex.w;

            vertexBuffer[row + 4] = vertex.getU();
            vertexBuffer[row + 5] = vertex.getV();
            vertexBuffer[row + 6] = 0.0f;
            vertexBuffer[row + 7] = 0.0f;
        }

        return vertexBuffer;
    };

    public static int size() {
        return SIZE;
    }

    public static int memSize() {
        return Float.BYTES * SIZE;
    }
}
