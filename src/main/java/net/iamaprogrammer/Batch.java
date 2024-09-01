package net.iamaprogrammer;

import net.iamaprogrammer.geometry.Geometry;
import net.iamaprogrammer.geometry.Vertex;
import net.iamaprogrammer.math.Matrix4f;
import net.iamaprogrammer.shader.ShaderManager;
import net.iamaprogrammer.shader.ShaderProgram;
import net.iamaprogrammer.texture.Texture;
import net.iamaprogrammer.texture.TextureStorageType;

import java.util.*;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL43C.*;

public class Batch {
    private final float[] vertices;
    private final int[] indices;

    private final float[] instancedData;
    private final int instancedDataSize;

    private final String shaderProgramName;
    private final Optional<Texture> texture;

    private int VBO;
    private int VAO;
    private int EBO;

    private int SSBO;

    private Batch(BatchData batchData, List<Geometry> geometryGroup) {
        this.vertices = new float[batchData.verticesCount];

        this.instancedData = new float[geometryGroup.size() * batchData.geometryDataSize];
        this.instancedDataSize = batchData.geometryDataSize;

        this.indices = new int[batchData.indicesCount];
        this.shaderProgramName = batchData.shaderProgramName;
        this.texture = batchData.texture;

        int verticesOffset = 0;
        int indicesOffset = 0;
        int instancedDataOffset = 0;
        for (Geometry geometry : geometryGroup) {

            // Add indices
            int[] geometryIndices = new int[geometry.getIndices().length];
            int i = 0;
            for (int index : geometry.getIndices()) {
                geometryIndices[i] = index + (verticesOffset / Vertex.size());
                i++;
            }
            System.arraycopy(geometryIndices, 0, this.indices, indicesOffset, geometryIndices.length);
            indicesOffset += geometryIndices.length;

            // Add vertices
            float[] geometryVertices = Vertex.asBuffer(geometry.getVertices());
            System.arraycopy(geometryVertices, 0, this.vertices, verticesOffset, geometryVertices.length);
            verticesOffset += geometryVertices.length;

            // Add instanced data
            geometry.getInstanceData().toArray(instancedDataOffset, this.instancedData);
            instancedDataOffset += geometry.getInstanceData().size();
        }
    }

    public void setup() {
        this.VAO = glGenVertexArrays();
        this.VBO = glGenBuffers();
        this.EBO = glGenBuffers();
        this.SSBO = glGenBuffers();

        glBindVertexArray(this.VAO);

        glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
        glBufferData(GL_ARRAY_BUFFER, this.getVertices(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.getIndices(), GL_STATIC_DRAW);

        glBindBuffer(GL_SHADER_STORAGE_BUFFER, this.SSBO);
        glBufferData(GL_SHADER_STORAGE_BUFFER, this.instancedData, GL_STATIC_DRAW);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, this.SSBO);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, Vertex.memSize(), 0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.memSize(), 4*Float.BYTES);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void draw(ShaderManager shaderManager, Matrix4f perspective) {
        ShaderProgram shaderProgram = shaderManager.getProgram(this.shaderProgramName);
        shaderProgram.use();
        shaderProgram.setUniform("perspective", perspective);
        shaderProgram.setUniform("view", new Matrix4f());
        if (this.texture.isPresent()) {
            Texture tex = this.texture.get();
            if (tex.getStorageType() == TextureStorageType.UNIFORM) {
                tex.handler(shaderProgram);
            }
        }

        glBindVertexArray(this.VAO);
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, this.SSBO);
        glDrawElementsInstanced(GL_TRIANGLES, this.indices.length, GL_UNSIGNED_INT, 0, instancedData.length / this.instancedDataSize);

        glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
        glBindVertexArray(0);
        glUseProgram(0);
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
        for (Geometry geometry : geometries) {
            if (!sortedGeometry.isEmpty()) {
                boolean batched = false;
                for (Map.Entry<BatchData, List<Geometry>> entry : sortedGeometry.entrySet()) {
                    BatchData batchData = entry.getKey();
                    List<Geometry> batchedGeometry = entry.getValue();

                    if (geometry.canBatchWith(batchedGeometry.get(0))) {
                        batchedGeometry.add(geometry);
                        batchData.verticesCount += geometry.getVertices().length * Vertex.size();
                        batchData.indicesCount += geometry.getIndices().length;
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

            BatchData batchData = new BatchData(numberOfBatches, geometry.getShaderProgramName());
            batchData.texture = geometry.getTexture();
            batchData.verticesCount += geometry.getVertices().length * Vertex.size();
            batchData.indicesCount += geometry.getIndices().length;
            batchData.geometryDataSize = geometry.getInstanceData().size();

            sortedGeometry.put(batchData, batchedGeometry);
            numberOfBatches++;
        }

        Batch[] batches = new Batch[sortedGeometry.size()];
        for (Map.Entry<BatchData, List<Geometry>> entry : sortedGeometry.entrySet()) {
            BatchData batchData = entry.getKey();
            List<Geometry> geometryGroup = entry.getValue();

            batches[batchData.index] = new Batch(
                    batchData,
                    geometryGroup
            );
        }

        return batches;
    }

    public static class BatchData {
        public int index;
        public String shaderProgramName;
        public Optional<Texture> texture;
        public int verticesCount = 0;
        public int indicesCount = 0;
        public int geometryDataSize = 0;

        private BatchData(int index, String shaderProgramName) {
            this.index = index;
            this.shaderProgramName = shaderProgramName;
        }
    }
}
