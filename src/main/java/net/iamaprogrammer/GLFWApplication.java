package net.iamaprogrammer;

import net.iamaprogrammer.shader.ShaderManager;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLFWApplication {
    private long windowHandle;
    private final ShaderManager shaderManager;

    private final String title;
    private final int width;
    private final int height;

    private int visible = GLFW_FALSE;
    private int resizable = GLFW_TRUE;

    public GLFWApplication(String title, int width, int height) {
        this.shaderManager = new ShaderManager();

        this.title = title;
        this.width = width;
        this.height = height;
    }

    public void run() {
        this.init();
        this.loop();

        glfwFreeCallbacks(this.windowHandle);
        glfwDestroyWindow(this.windowHandle);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    protected void initShaders(ShaderManager shaderManager) {}
    protected void bindKeys(long windowHandle, int width, int height) {}
    protected void initGraphics(ShaderManager shaderManager) {}
    protected void frame(ShaderManager shaderManager) {}

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setVisible(int isVisible) {
        this.visible = isVisible;
    }

    public void setResizable(int isResizable) {
        this.resizable = isResizable;
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) throw new IllegalStateException("Failed to load GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, this.visible); // Set window visible
        glfwWindowHint(GLFW_RESIZABLE, this.resizable); // Set window resizable

        this.windowHandle = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (this.windowHandle == NULL) throw new RuntimeException("Failed to create the GLFW window");

        this.bindKeys(this.windowHandle, this.width, this.height);

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            glfwGetWindowSize(this.windowHandle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(this.windowHandle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        glfwMakeContextCurrent(this.windowHandle);
        glfwSwapInterval(1); // Enable v-sync
    }

    private void loop() {
        GL.createCapabilities();
        this.initShaders(this.shaderManager);
        this.initGraphics(this.shaderManager);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // Make the window visible
        glfwShowWindow(this.windowHandle);

        while (!glfwWindowShouldClose(this.windowHandle)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            this.frame(this.shaderManager);

            glfwSwapBuffers(this.windowHandle); // swap the color buffers
            glfwPollEvents();
        }

        this.shaderManager.close();
    }
}
