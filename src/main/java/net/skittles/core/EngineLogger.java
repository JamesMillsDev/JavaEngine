package net.skittles.core;

import java.util.logging.Logger;

public class EngineLogger
{
    private final Logger logger = Logger.getLogger("Java Engine");

    public void print(String message)
    {
        logger.info(message);
    }
}
