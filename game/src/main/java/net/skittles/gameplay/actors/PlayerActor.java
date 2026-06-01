package net.skittles.gameplay.actors;

import api.skittles.gameplay.actors.Actor;
import net.skittles.gameplay.components.LogMessageComponent;

public class PlayerActor extends Actor
{
    private final LogMessageComponent logMessageComponent;

    public PlayerActor()
    {
        super();

        this.logMessageComponent = addComponent(LogMessageComponent::new);
    }
}
