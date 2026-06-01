package net.skittles.core;

import net.skittles.core.utility.Config;
import net.skittles.core.utility.Version;

public class Engine
{
    public static final Config config = new Config("Engine");
    public static final EngineLogger logger = new EngineLogger();

    public static final Version version = Version.from(config.getList("Application.Version", Integer.class));
}
