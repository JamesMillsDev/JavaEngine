package api.skittles.gameplay.levels;

import api.skittles.core.Engine;
import api.skittles.gameplay.actors.Actor;
import api.skittles.gameplay.actors.ActorTransform;
import api.skittles.utility.Pair;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class Level
{
    final Actor root;

    final ArrayList<Pair<Actor, Consumer<Actor>>> pendingActorChanges = new ArrayList<>();
    private final ArrayList<Actor> actors = new ArrayList<>();

    public Level()
    {
        this.root = new Actor();
    }

    public final <T extends Actor> T makeActor(Class<T> actorClass)
    {
        T actor;

        try
        {
            actor = actorClass.getDeclaredConstructor().newInstance();
        }
        catch (Exception e)
        {
            Engine.instance.logger.severe(e.getMessage());
            return null;
        }

        this.pendingActorChanges.add(new Pair<>(
                actor,
                (a) ->
                {
                    this.actors.add(a);
                    a.transform.setParent(this.root.transform);
                    a.transform.applyChildListChanges();
                }
        ));

        return actor;
    }

    public final void destroyActor(Actor actor)
    {
        this.pendingActorChanges.add(new Pair<>(
                actor,
                this.actors::remove
        ));
    }

    public void onLoaded()
    {

    }

    public void onUnloaded()
    {

    }

    public void tick(float dt)
    {

    }

    public void render()
    {

    }

    public abstract String id();

    void tickActors(float dt)
    {
        tickActor(dt, this.root);
    }

    void renderActors()
    {
        renderActor(this.root);
    }

    private void tickActor(float dt, Actor actor)
    {
        actor.tick(dt);

        for(ActorTransform child : actor.transform)
        {
            tickActor(dt, child.owner());
        }
    }

    private void renderActor(Actor actor)
    {
        actor.render();

        for(ActorTransform child : actor.transform)
        {
            renderActor(child.owner());
        }
    }
}
