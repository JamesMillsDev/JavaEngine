package net.skittles.actors;

public class Actor
{
    public final ActorTransform transform;

    public Actor()
    {
        this.transform = new ActorTransform(this);
    }
}
