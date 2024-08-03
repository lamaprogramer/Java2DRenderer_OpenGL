package net.iamaprogrammer.shader;

import net.iamaprogrammer.util.FileUtil;

import java.io.InputStream;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private final int id;

    public Shader(int shaderType) {
        this.id = glCreateShader(shaderType);
    }

    public void source(String source) {
        glShaderSource(this.id, source);
    }

    public void compile() {
        glCompileShader(this.id);
        this.compileStatus();
    }

    public void delete(){
        glDeleteShader(this.id);
    }

    public int getId() {
        return this.id;
    }

    private void compileStatus() {
        int success = glGetShaderi(this.id, GL_COMPILE_STATUS);
        if(success != GL_TRUE) {
            throw new RuntimeException(glGetShaderInfoLog(this.id));
        }
    }

    public static Shader create(int type, String source) {
        Shader shader = new Shader(type);
        shader.source(source);
        shader.compile();

        return shader;
    }

    public static Shader load(int type, InputStream stream) {
        String source = FileUtil.readFromStream(stream);
        return create(type, source);
    }
}
