package net.iamaprogrammer.renderer;

import net.iamaprogrammer.Batch;
import net.iamaprogrammer.geometry.Circle;
import net.iamaprogrammer.geometry.Ellipse;
import net.iamaprogrammer.geometry.Geometry;
import net.iamaprogrammer.geometry.Quad;
import net.iamaprogrammer.math.MathUtil;
import net.iamaprogrammer.math.Matrix4f;
import net.iamaprogrammer.math.Vector2f;
import net.iamaprogrammer.math.Vector4f;
import net.iamaprogrammer.shader.ShaderManager;
import net.iamaprogrammer.texture.GradientTexture;
import net.iamaprogrammer.texture.SolidColorTexture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL43.*;

public class Renderer2D {
    public static final ShaderManager SHADER_MANAGER = new ShaderManager();
    public static final List<Geometry> GEOMETRY = new ArrayList<>();
    public static Batch[] BATCHES;
    public static Matrix4f ORTHO_VIEW;

    public static void init(int width, int height) {
        ORTHO_VIEW = Matrix4f.orthographic(
                0.0f,
                (float) width,
                (float) height,
                0.0f,
                -1f,
                1f
        );

        batchGeometry();
    }

    public static void close() {
        SHADER_MANAGER.close();
    }

    public static void batchGeometry() {
        BATCHES = Batch.batchGeometry(GEOMETRY);
        for (Batch batch : BATCHES) {
            batch.setup();
        }
    }

    public static void render() {
        for (Batch batch : BATCHES) {
            batch.draw(SHADER_MANAGER, ORTHO_VIEW);
        }
    }

    public static void drawQuad(Vector2f position, Vector2f size, Vector4f color) {
        GEOMETRY.add(new Quad(position, (int) size.x, (int) size.y, color));
    }

    public static void drawGradientTexturedQuad(Vector2f position, Vector2f size, GradientTexture gradientTexture) {
        GEOMETRY.add(new Quad(position, (int) size.x, (int) size.y, gradientTexture, "gradient_program"));
    }

    public static void drawEllipse(Vector2f position, Vector2f size, Vector4f color) {
        GEOMETRY.add(new Ellipse(position, (int) size.x, (int) size.y, new SolidColorTexture(color)));
    }

    public static void drawCircle(Vector2f position, int radius, Vector4f color) {
        GEOMETRY.add(new Circle(position, radius, new SolidColorTexture(color)));
    }

    public static void clearColor(Vector4f clearColor) {
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
    }

    public static void clear(int mask) {
        glClear(mask);
    }

}
