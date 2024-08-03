package net.iamaprogrammer.geometry;

import net.iamaprogrammer.math.Vector3f;

public class Vertex {
    private static final int SIZE = 6;

    private final float x;
    private final float y;
    private final float z;

    private final float r;
    private final float g;
    private final float b;

    public Vertex(Vector3f position, Vector3f color) {
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;

        this.r = color.x;
        this.g = color.y;
        this.b = color.z;
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


    public float getR() {
        return this.r;
    }

    public float getG() {
        return this.g;
    }

    public float getB() {
        return this.b;
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

            vertexBuffer[row + 3] = vertex.getR();
            vertexBuffer[row + 4] = vertex.getG();
            vertexBuffer[row + 5] = vertex.getB();
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
