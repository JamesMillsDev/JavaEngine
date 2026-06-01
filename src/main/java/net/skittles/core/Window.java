package net.skittles.core;

import net.skittles.maths.Color;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;
import java.util.Objects;

public class Window
{
    private long window;

    private final Integer width;
    private final Integer height;
    private final String title;
    private final Color clrColor;

    public Window()
    {
        this.width = Engine.config.getValue("Application.Window.Width", Integer.class);
        this.height = Engine.config.getValue("Application.Window.Height", Integer.class);
        this.title = Engine.config.getValue("Application.Title", String.class);
        this.clrColor = Color.fromArraylist(Engine.config.getList("Application.Window.ClearColor", Double.class));
    }

    public void open()
    {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit())
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        this.window = GLFW.glfwCreateWindow(width, height, title + " v" + Engine.version, MemoryUtil.NULL, MemoryUtil.NULL);
        if (this.window == MemoryUtil.NULL)
        {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFW.glfwSetKeyCallback(this.window, (_, key, _, action, _) ->
        {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
            {
                GLFW.glfwSetWindowShouldClose(this.window, true);
            }
        });

        try (MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(this.window, w, h);

            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            if (vidMode != null)
            {
                GLFW.glfwSetWindowPos(
                        this.window,
                        (vidMode.width() - w.get(0)) / 2,
                        (vidMode.height() - h.get(0)) / 2
                );
            }
        }

        GLFW.glfwMakeContextCurrent(this.window);
        GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(this.window);

        GL.createCapabilities();

        ArrayList<Float> clrCol = this.clrColor.toOpenGl();
        GL11.glClearColor(clrCol.get(0), clrCol.get(1), clrCol.get(2), clrCol.get(3));
        GL11.glViewport(0, 0, width, height);

        //noinspection resource
        GLFW.glfwSetFramebufferSizeCallback(this.window, ((_, w, h) -> GL11.glViewport(0, 0, w, h)));
    }

    public void close()
    {
        Callbacks.glfwFreeCallbacks(this.window);
        GLFW.glfwDestroyWindow(this.window);

        GLFW.glfwTerminate();
        Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
    }

    public boolean shouldClose()
    {
        return GLFW.glfwWindowShouldClose(this.window);
    }

    public void beginFrame()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void endFrame()
    {
        GLFW.glfwSwapBuffers(this.window);
        GLFW.glfwPollEvents();
    }
}
