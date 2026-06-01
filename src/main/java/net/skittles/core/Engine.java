package net.skittles.core;

import net.skittles.utility.Config;
import net.skittles.utility.Version;

import java.util.logging.Logger;

public class Engine
{
    public static final Config config = new Config("Engine");
    public static final Logger logger = Logger.getLogger(config.getValue("Application.Title", String.class));
    public static final Version version = Version.from(config.getList("Application.Version", Integer.class));
}
