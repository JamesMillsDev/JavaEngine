package api.skittles.core;

import api.skittles.utility.Config;
import api.skittles.utility.Version;

import java.util.logging.Logger;

public class Engine
{
    public static Engine instance;

    static void init()
    {
        instance = new Engine();
    }

    public final Config config;
    public final Logger logger;
    public final Version version;

    private Engine()
    {
        this.config = new Config("Engine");
        this.logger = Logger.getLogger(config.getValue("Application.Title", String.class));
        this.version = Version.from(config.getList("Application.Version", Integer.class));
    }
}
