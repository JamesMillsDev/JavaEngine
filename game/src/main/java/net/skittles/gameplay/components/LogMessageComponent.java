package net.skittles.gameplay.components;

import api.skittles.core.Engine;
import api.skittles.gameplay.actors.ActorComponent;

import java.util.logging.Level;

public class LogMessageComponent extends ActorComponent
{
    @Override
    public void beginPlay()
    {
        Engine.instance.logger.log(Level.WARNING, "LogMessageComponent beginPlay");
    }
}
