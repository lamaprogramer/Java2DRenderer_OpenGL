package net.iamaprogrammer.shader;

import net.iamaprogrammer.util.FileUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderManager {
    private final Map<String, Shader> SHADERS = new HashMap<>();
    private final Map<String, ShaderProgram> SHADER_PROGRAMS = new HashMap<>();
    private boolean isClosed = false;

    public ShaderManager() {}

    public void addShader(String identifier, Shader shader) {
        this.throwIfClosed();
        this.SHADERS.put(identifier, shader);
    }

    public void addProgram(String identifier, ShaderProgram shaderProgram) {
        this.throwIfClosed();
        this.SHADER_PROGRAMS.put(identifier, shaderProgram);
    }

    public Shader getShader(String identifier) {
        this.throwIfClosed();
        return this.SHADERS.get(identifier);
    }

    public ShaderProgram getProgram(String identifier) {
        this.throwIfClosed();
        return this.SHADER_PROGRAMS.get(identifier);
    }

    public void close() {
        this.throwIfClosed();
        for (Shader shader : this.SHADERS.values()) {
            shader.delete();
        }

        for (ShaderProgram program : this.SHADER_PROGRAMS.values()) {
            program.delete();
        }
        this.isClosed = true;
    }

    private void throwIfClosed() {
        if (this.isClosed) throw new IllegalStateException("Tried to access and/or modify shader resources after destruction.");
    }
}
