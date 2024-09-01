package net.iamaprogrammer.shader;

import net.iamaprogrammer.math.*;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class ShaderProgram {
    private final int id;

    private ShaderProgram(int id) {
        this.id = id;
    }

    public void use() {
        glUseProgram(this.id);
    }

    public void delete() {
        glDeleteProgram(this.id);
    }

    public int getId() {
        return this.id;
    }

    public void setUniform(String name, int uniformValue) {
        int uniform = glGetUniformLocation(this.id, name);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer buffer = stack.mallocInt(1);
            buffer.put(uniformValue);
            buffer.flip();
            glUniform1iv(uniform, buffer);
        }
    }

    public void setUniform(String name, float uniformValue) {
        int uniform = glGetUniformLocation(this.id, name);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(1);
            buffer.put(uniformValue);
            buffer.flip();
            glUniform1fv(uniform, buffer);
        }
    }

    public void setUniform(String name, Vector2f... uniformValue) {
        int uniform = glGetUniformLocation(this.id, name);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(2 * uniformValue.length);
            for (Vector2f value : uniformValue) {
                value.toBuffer(buffer, false);
            }
            buffer.flip();
            glUniform2fv(uniform, buffer);
        }
    }

    public void setUniform(String name, Vector3f... uniformValue) {
        int uniform = glGetUniformLocation(this.id, name);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(3 * uniformValue.length);
            for (Vector3f value : uniformValue) {
                value.toBuffer(buffer, false);
            }
            buffer.flip();
            glUniform3fv(uniform, buffer);
        }
    }

    public void setUniform(String name, Vector4f... uniformValue) {
        int uniform = glGetUniformLocation(this.id, name);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(4 * uniformValue.length);
            for (Vector4f value : uniformValue) {
                value.toBuffer(buffer, false);
            }
            buffer.flip();
            glUniform4fv(uniform, buffer);
        }
    }

    public void setUniform(String name, Matrix2f uniformValue) {
        int uniform = glGetUniformLocation(this.id, name);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(2*2);
            uniformValue.toBuffer(buffer);
            glUniformMatrix2fv(uniform, false, buffer);
        }
    }

    public void setUniform(String name, Matrix3f uniformValue) {
        int uniform = glGetUniformLocation(this.id, name);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(3*3);
            uniformValue.toBuffer(buffer);
            glUniformMatrix3fv(uniform, false, buffer);
        }
    }

    public void setUniform(String name, Matrix4f uniformValue) {
        int uniform = glGetUniformLocation(this.id, name);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(4*4);
            uniformValue.toBuffer(buffer);
            glUniformMatrix4fv(uniform, false, buffer);
        }
    }

    public static ShaderProgramBuilder builder() {
        return new ShaderProgramBuilder();
    }

    public static class ShaderProgramBuilder {
        private int id;

        private ShaderProgramBuilder() {
            this.id = glCreateProgram();
        }

        public ShaderProgramBuilder attachShader(Shader shader) {
            glAttachShader(this.id, shader.getId());
            return this;
        }

        public ShaderProgram link() {
            glLinkProgram(this.id);
            this.checkStatus();
            return new ShaderProgram(this.id);
        }

        private void checkStatus() {
            int status = glGetProgrami(id, GL_LINK_STATUS);
            if (status != GL_TRUE) {
                throw new RuntimeException(glGetProgramInfoLog(id));
            }
        }
    }
}
