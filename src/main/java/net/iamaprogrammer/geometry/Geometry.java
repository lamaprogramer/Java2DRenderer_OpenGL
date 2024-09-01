package net.iamaprogrammer.geometry;

import net.iamaprogrammer.math.Matrix4f;
import net.iamaprogrammer.math.Vector4f;
import net.iamaprogrammer.renderer.Gradient;
import net.iamaprogrammer.texture.Texture;
import net.iamaprogrammer.texture.TextureStorageType;

import java.util.Optional;

public class Geometry {
    private final Vertex[] vertices;
    private final String shaderProgramName;
    private final Optional<Texture> texture;
    private final GeometryInstanceData instanceData;

    public Geometry(Vertex[] vertices, Texture texture, GeometryInstanceData data) {
        this(vertices, texture, data, "default_program");
    }

    public Geometry(Vertex[] vertices, Texture texture, GeometryInstanceData data, String shaderProgramName) {
        this.vertices = vertices;
        this.texture = Optional.ofNullable(texture);
        this.shaderProgramName = shaderProgramName;
        this.instanceData = data;
    }

    public String getShaderProgramName() {
        return this.shaderProgramName;
    }

    public Vertex[] getVertices() {
        return this.vertices;
    }

    public int[] getIndices() {
        return null;
    }

    public Optional<Texture> getTexture() {
        return texture;
    }

    public GeometryInstanceData getInstanceData() {
        return instanceData;
    }

    public boolean canBatchWith(Geometry geometry) {
        return  this.instanceDataSizeEquals(geometry) &&
                this.textureMatches(geometry)         &&
                this.vertexDataSizeEquals(geometry)   &&
                this.shaderProgramEquals(geometry);
    }

    private boolean instanceDataSizeEquals(Geometry geometry) {
        return this.instanceData.size() == geometry.getInstanceData().size();
    }

    private boolean vertexDataSizeEquals(Geometry geometry) {
        return this.vertices.length == geometry.vertices.length;
    }

    private boolean shaderProgramEquals(Geometry geometry) {
        return this.vertices.length == geometry.vertices.length;
    }

    private boolean textureMatches(Geometry geometry) {
        if (this.texture.isPresent() != geometry.texture.isPresent()) {
            return false;
        }

        return this.texture.isEmpty() || this.texture.get().isEqualTo(geometry.texture.get());
    }

    public static class GeometryInstanceData {
        public Matrix4f modelMatrix;
        public Vector4f color;

        public GeometryInstanceData(Matrix4f modelMatrix, Vector4f color) {
            this.modelMatrix = modelMatrix;
            this.color = color;
        }

        public int size() {
            return 4 + 16;
        }

        public int memSize() {
            return (4 * Float.BYTES) + (16 * Float.BYTES);
        }

        public void toArray(int offset, float[] array) {
            this.modelMatrix.toArray(offset, array);
            this.color.toArray(offset+16, array);
        }
    }
}
