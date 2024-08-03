package net.iamaprogrammer;

import net.iamaprogrammer.geometry.Geometry;
import net.iamaprogrammer.geometry.Vertex;
import net.iamaprogrammer.math.Matrix4f;
import net.iamaprogrammer.shader.ShaderManager;
import net.iamaprogrammer.shader.ShaderProgram;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Batch {
    private final float[] vertices;
    private final int[] indices;
    private final String shaderProgramName;

    private int VBO;
    private int VAO;
    private int EBO;

    private Batch(String shaderProgramName, int verticesCount, int indicesCount, List<Geometry> geometryGroup) {
        this.vertices = new float[verticesCount];
        this.indices = new int[indicesCount];
        this.shaderProgramName = shaderProgramName;

        int verticesOffset = 0;
        int indicesOffset = 0;
        for (Geometry geometry : geometryGroup) {
            int[] geometryIndices = new int[geometry.getIndices().length];
            int i = 0;
            for (int index : geometry.getIndices()) {
                geometryIndices[i] = index + (verticesOffset / Vertex.size());
                i++;
            }
            System.arraycopy(geometryIndices, 0, this.indices, indicesOffset, geometryIndices.length);
            indicesOffset += geometryIndices.length;

            float[] geometryVertices = Vertex.asBuffer(geometry.getVertices());
            System.arraycopy(geometryVertices, 0, this.vertices, verticesOffset, geometryVertices.length);
            verticesOffset += geometryVertices.length;
        }
    }

    public void setup() {
        this.VAO = glGenVertexArrays();
        this.VBO = glGenBuffers();
        this.EBO = glGenBuffers();

        glBindVertexArray(this.VAO);

        glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
        glBufferData(GL_ARRAY_BUFFER, this.getVertices(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.getIndices(), GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.memSize(), 0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, Vertex.memSize(), 3*Float.BYTES);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void draw(ShaderManager shaderManager, Matrix4f perspective) {
        ShaderProgram shaderProgram = shaderManager.getProgram(this.shaderProgramName);
        shaderProgram.use();
        shaderProgram.setUniform("perspective", perspective);

        glBindVertexArray(this.VAO);
        glDrawElements(GL_TRIANGLES, this.indices.length, GL_UNSIGNED_INT, 0);
    }

    public float[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public static Batch[] batchGeometry(List<Geometry> geometries) {
        Map<BatchData, List<Geometry>> sortedGeometry = new HashMap<>();

        int numberOfBatches = 0;
        int verticesCount = 0;
        int indicesCount = 0;

        for (Geometry geometry : geometries) {
            verticesCount += geometry.getVertices().length * Vertex.size();
            indicesCount += geometry.getIndices().length;

            if (!sortedGeometry.isEmpty()) {
                boolean batched = false;
                for (List<Geometry> batchedGeometry : sortedGeometry.values()) {
                    if (geometry.canBatchWith(batchedGeometry.get(0))) {
                        batchedGeometry.add(geometry);
                        batched = true;
                        break;
                    }
                }

                if (batched) {
                    continue;
                }
            }

            List<Geometry> batchedGeometry = new ArrayList<>();
            batchedGeometry.add(geometry);

            sortedGeometry.put(new BatchData(
                    numberOfBatches,
                    geometry.getShaderProgramName()
            ), batchedGeometry);
            numberOfBatches++;

        }

        Batch[] batches = new Batch[sortedGeometry.size()];
        for (Map.Entry<BatchData, List<Geometry>> entry : sortedGeometry.entrySet()) {
            BatchData batchData = entry.getKey();
            List<Geometry> geometryGroup = entry.getValue();

            batches[batchData.index] = new Batch(batchData.shaderProgramName, verticesCount, indicesCount, geometryGroup);
        }

        return batches;
    }

    public static class BatchData {
        public int index;
        public String shaderProgramName;

        private BatchData(int index, String shaderProgramName) {
            this.index = index;
            this.shaderProgramName = shaderProgramName;
        }
    }
}
