package api.skittles.core;

import api.skittles.gameplay.GameInstance;

import javax.naming.OperationNotSupportedException;
import java.util.function.Supplier;

public class Application
{
    private static Application instance;

    public static <T extends GameInstance> Integer open(Supplier<T> gameInstanceSupplier) throws OperationNotSupportedException
    {
        if(instance != null)
        {
            throw new OperationNotSupportedException("Cannot open more than one application at any given time.");
        }

        instance = new Application(gameInstanceSupplier.get());
        Integer val = instance.run();

        instance = null;
        return val;
    }

    private final Window window;
    private final GameInstance gameInstance;

    private Application(GameInstance gameInstance)
    {
        Engine.init();
        this.window = new Window();
        this.gameInstance = gameInstance;
    }

    private Integer run()
    {
        try
        {
            this.window.open();
        }
        catch (Exception e)
        {
            return -1;
        }

        this.gameInstance.init();

        while(!this.window.shouldClose())
        {
            gameInstance.tick(0);

            this.window.beginFrame();

            gameInstance.render();

            this.window.endFrame();
        }

        this.gameInstance.shutdown();
        this.window.close();

        return 0;
    }
}
