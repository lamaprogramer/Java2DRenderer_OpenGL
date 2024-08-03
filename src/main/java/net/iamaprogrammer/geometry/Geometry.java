package net.iamaprogrammer.geometry;

public class Geometry {
    private Vertex[] vertices;
    private String shaderProgramName;

    public Geometry(Vertex[] vertices) {
        this.vertices = vertices;
        this.shaderProgramName = "default_program";
    }

    public Geometry(Vertex[] vertices, String shaderProgramName) {
        this.vertices = vertices;
        this.shaderProgramName = shaderProgramName;
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

    public boolean canBatchWith(Geometry geometry) {
        return this.vertices.length == geometry.vertices.length &&
                this.shaderProgramName.equals(geometry.shaderProgramName);
    }
}
