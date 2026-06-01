package api.skittles.gameplay.levels;

import api.skittles.gameplay.actors.Actor;
import api.skittles.utility.Pair;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Level
{
    final Actor root;

    final ArrayList<Pair<Actor, Consumer<Actor>>> pendingActions = new ArrayList<>();
    private final ArrayList<Actor> actors = new ArrayList<>();

    public Level()
    {
        this.root = new Actor();
    }

    public final Actor makeActor()
    {
        Actor actor = new Actor();
        this.pendingActions.add(new Pair<>(
                actor,
                this.actors::add
        ));

        return actor;
    }

    public final void destroyActor(Actor actor)
    {
        this.pendingActions.add(new Pair<>(
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
}
