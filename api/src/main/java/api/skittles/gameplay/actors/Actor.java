package api.skittles.gameplay.actors;

public class Actor
{
    public final ActorTransform transform;

    public Actor()
    {
        this.transform = new ActorTransform(this);
    }

    public void tick(float dt)
    {
        
    }
}
