package net.iamaprogrammer;

import net.iamaprogrammer.math.MathUtil;
import net.iamaprogrammer.math.Matrix4f;
import net.iamaprogrammer.math.Vector2f;
import net.iamaprogrammer.math.Vector4f;
import net.iamaprogrammer.renderer.Gradient;
import net.iamaprogrammer.renderer.Renderer2D;
import net.iamaprogrammer.shader.Shader;
import net.iamaprogrammer.shader.ShaderProgram;
import net.iamaprogrammer.texture.GradientTexture;
import net.iamaprogrammer.util.ImageOutputUtil;
import net.iamaprogrammer.util.MatrixUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;

public class Main extends GLFWApplication {

    public Main(String title, int width, int height) {
        super(title, width, height);
    }

    @Override
    protected void bindKeys(long windowHandle, int width, int height) {
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_E && action == GLFW_RELEASE)
                ImageOutputUtil.exportGLFrame(width, height);
        });
    }

    @Override
    protected void onInit() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Renderer2D.SHADER_MANAGER.addShader("solid_color_vertex",   Shader.load(GL_VERTEX_SHADER, Main.class.getResourceAsStream("/shaders/default/solid_color.vert")));
        Renderer2D.SHADER_MANAGER.addShader("solid_color_fragment", Shader.load(GL_FRAGMENT_SHADER, Main.class.getResourceAsStream("/shaders/default/solid_color.frag")));
        Renderer2D.SHADER_MANAGER.addShader("solid_color_circle_fragment",   Shader.load(GL_FRAGMENT_SHADER, Main.class.getResourceAsStream("/shaders/default/solid_color_circle.frag")));

        Renderer2D.SHADER_MANAGER.addShader("gradient_vertex",   Shader.load(GL_VERTEX_SHADER, Main.class.getResourceAsStream("/shaders/gradient/gradient.vert")));
        Renderer2D.SHADER_MANAGER.addShader("gradient_fragment", Shader.load(GL_FRAGMENT_SHADER, Main.class.getResourceAsStream("/shaders/gradient/gradient.frag")));
        Renderer2D.SHADER_MANAGER.addShader("gradient_circle_fragment",   Shader.load(GL_FRAGMENT_SHADER, Main.class.getResourceAsStream("/shaders/gradient/gradient_circle.frag")));

        // Solid Color Renderer
        ShaderProgram basicProgram = ShaderProgram.builder()
                .attachShader(Renderer2D.SHADER_MANAGER.getShader("solid_color_vertex"))
                .attachShader(Renderer2D.SHADER_MANAGER.getShader("solid_color_fragment"))
                .link();
        Renderer2D.SHADER_MANAGER.addProgram("default_program", basicProgram);

        // Render Circles
        ShaderProgram circleProgram = ShaderProgram.builder()
                .attachShader(Renderer2D.SHADER_MANAGER.getShader("solid_color_vertex"))
                .attachShader(Renderer2D.SHADER_MANAGER.getShader("solid_color_circle_fragment"))
                .link();
        Renderer2D.SHADER_MANAGER.addProgram("circle_program", circleProgram);


        // Gradient Renderer
        ShaderProgram gradientProgram = ShaderProgram.builder()
                .attachShader(Renderer2D.SHADER_MANAGER.getShader("gradient_vertex"))
                .attachShader(Renderer2D.SHADER_MANAGER.getShader("gradient_fragment"))
                .link();
        Renderer2D.SHADER_MANAGER.addProgram("gradient_program", gradientProgram);

        // Render Circles
        ShaderProgram gradientCircleProgram = ShaderProgram.builder()
                .attachShader(Renderer2D.SHADER_MANAGER.getShader("gradient_vertex"))
                .attachShader(Renderer2D.SHADER_MANAGER.getShader("gradient_circle_fragment"))
                .link();
        Renderer2D.SHADER_MANAGER.addProgram("gradient_circle_program", gradientCircleProgram);

        Gradient gradient1 = new Gradient(Gradient.Pattern.ANGULAR, 240.0f, new Vector4f[]{
                new Vector4f(MathUtil.normalizeRGB(117), MathUtil.normalizeRGB(251), MathUtil.normalizeRGB(237), 1.0f),
                new Vector4f(MathUtil.normalizeRGB(77), MathUtil.normalizeRGB(204), MathUtil.normalizeRGB(142), 1.0f),
                new Vector4f(MathUtil.normalizeRGB(136), MathUtil.normalizeRGB(23), MathUtil.normalizeRGB(106), 1.0f)
        }, true);

        Gradient gradient2 = new Gradient(Gradient.Pattern.ANGULAR, 240.0f, new Vector4f[]{
                new Vector4f(MathUtil.normalizeRGB(117), MathUtil.normalizeRGB(251), MathUtil.normalizeRGB(237), 1.0f),
                new Vector4f(MathUtil.normalizeRGB(77), MathUtil.normalizeRGB(204), MathUtil.normalizeRGB(142), 1.0f),
                new Vector4f(MathUtil.normalizeRGB(136), MathUtil.normalizeRGB(23), MathUtil.normalizeRGB(106), 1.0f)
        }, false);

        Renderer2D.drawGradientTexturedQuad(
                new Vector2f(0, 0),
                new Vector2f(300, 300),
                new GradientTexture("gradient1", gradient1)
        );

        Renderer2D.drawGradientTexturedQuad(
                new Vector2f(0, 150),
                new Vector2f(300, 300),
                new GradientTexture("gradient2", gradient2)
        );

        Renderer2D.drawQuad(
                new Vector2f(150, 200),
                new Vector2f(300, 300),
                new Vector4f(0.0f, 1.0f, 0.0f, 1.0f)
        );

        Renderer2D.drawQuad(
                new Vector2f(400, 200),
                new Vector2f(300, 300),
                new Vector4f(1.0f, 0.0f, 0.0f, 1.0f)
        );

        Renderer2D.drawGradientTexturedQuad(
                new Vector2f(350, 0),
                new Vector2f(300, 300),
                new GradientTexture("gradient2", gradient2)
        );

        for (int i = 0; i < 10000; i++) {
            int randX = (int) (Math.random() * this.getWidth());
            int randY = (int) (Math.random() * this.getHeight());

            float randR = (float) (Math.random());
            float randG = (float) (Math.random());
            float randB = (float) (Math.random());

            Renderer2D.drawQuad(
                    new Vector2f(randX, randY),
                    new Vector2f(10, 10),
                    new Vector4f(randR, randG, randB, 1.0f)
            );
        }

        Renderer2D.init(this.getWidth(), this.getHeight());
        Renderer2D.clearColor(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
    }

    @Override
    protected void frame() {
        Renderer2D.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Renderer2D.render();
    }

    @Override
    protected void onExit() {
        Renderer2D.close();
    }

    public static void main(String[] args) {
        new Main("Image Viewer", 960, 540).run();
    }
}