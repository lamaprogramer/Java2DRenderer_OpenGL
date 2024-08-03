package net.iamaprogrammer;

import net.iamaprogrammer.geometry.Geometry;
import net.iamaprogrammer.geometry.Quad;
import net.iamaprogrammer.math.Matrix4f;
import net.iamaprogrammer.math.Vector2f;
import net.iamaprogrammer.math.Vector3f;
import net.iamaprogrammer.shader.Shader;
import net.iamaprogrammer.shader.ShaderManager;
import net.iamaprogrammer.shader.ShaderProgram;
import net.iamaprogrammer.util.ImageOutputUtil;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;

public class Main extends GLFWApplication {
    private final Matrix4f orthoView = Matrix4f.orthographic(
            0.0f,
            (float) this.getWidth(),
            (float) this.getHeight(),
            0.0f,
            -1f,
            1f
    );
    //private final Quad quad = new Quad(new Vector2f(100, 100), 100, 100, new Vector3f(1.0f, 1.0f, 0.0f));
    //private final Quad quad2 = new Quad(new Vector2f(200, 200), 100, 100, new Vector3f(1.0f, 0.0f, 0.0f));

    private final List<Geometry> geometry = new ArrayList<>();

    private Batch[] batches;

    public Main(String title, int width, int height) {
        super(title, width, height);
    }

    @Override
    protected void initShaders(ShaderManager shaderManager) {
        shaderManager.addShader("default_vertex",   Shader.load(GL_VERTEX_SHADER, Main.class.getResourceAsStream("/shaders/default.vert")));
        shaderManager.addShader("default_fragment", Shader.load(GL_FRAGMENT_SHADER, Main.class.getResourceAsStream("/shaders/default.frag")));
        //System.out.println("Created shaders.");
    }

    @Override
    protected void bindKeys(long windowHandle, int width, int height) {
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_E && action == GLFW_RELEASE)
                ImageOutputUtil.exportGLFrame(width, height);
        });
    }

    @Override
    protected void initGraphics(ShaderManager shaderManager) {

        ShaderProgram basicProgram = ShaderProgram.builder()
                .attachShader(shaderManager.getShader("default_vertex"))
                .attachShader(shaderManager.getShader("default_fragment"))
                .link();

        shaderManager.addProgram("default_program", basicProgram);

        this.geometry.add(new Quad(new Vector2f(100, 100), 100, 100, new Vector3f(1.0f, 0.0f, 0.0f)));
        this.geometry.add(new Quad(new Vector2f(150, 150), 100, 100, new Vector3f(0.0f, 1.0f, 0.0f)));
        this.geometry.add(new Quad(new Vector2f(200, 200), 100, 100, new Vector3f(0.0f, 0.0f, 1.0f)));

        this.batches = Batch.batchGeometry(geometry);
        for (Batch batch : this.batches) {
            batch.setup();
        }
    }

    @Override
    protected void frame(ShaderManager shaderManager) {
        for (Batch batch : this.batches) {
            batch.draw(shaderManager, this.orthoView);
        }
    }

    public static void main(String[] args) {
        new Main("Image Viewer", 960, 540).run();
    }
}