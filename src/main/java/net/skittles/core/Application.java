package net.skittles.core;

public class Application
{
    private final Window window;

    public Application()
    {
        this.window = new Window();
    }

    public void run()
    {
        this.window.open();

        while(!this.window.shouldClose())
        {
            this.window.beginFrame();

            this.window.endFrame();
        }

        this.window.close();
    }
}
