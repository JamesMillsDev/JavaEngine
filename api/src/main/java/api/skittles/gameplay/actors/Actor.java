package api.skittles.gameplay.actors;

import api.skittles.utility.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Actor
{
    public final ActorTransform transform;

    private final List<ActorComponent> components;
    private final List<Pair<ActorComponent, Consumer<ActorComponent>>> componentListChanges;

    public Actor()
    {
        this.transform = new ActorTransform(this);
        this.components = new ArrayList<>();

        this.componentListChanges = new ArrayList<>();
    }

    public final void tick(float dt)
    {
        for (Pair<ActorComponent, Consumer<ActorComponent>> change : this.componentListChanges)
        {
            change.second.accept(change.first);
        }

        this.componentListChanges.clear();

        for (ActorComponent component : this.components)
        {
            component.tick(dt);
        }
    }

    public final void render()
    {
        for (ActorComponent component : this.components)
        {
            component.render();
        }
    }

    public final <T extends ActorComponent> T addComponent(Supplier<T> supplier)
    {
        T component = supplier.get();
        component.owner = this;

        this.componentListChanges.add(new Pair<>(
                component,
                (comp) ->
                {
                    comp.beginPlay();
                    this.components.add(comp);
                }
        ));

        return component;
    }

    public final void removeComponent(ActorComponent component)
    {
        this.componentListChanges.add(new Pair<>(
                component,
                (comp) ->
                {
                    comp.endPlay();
                    this.components.remove(comp);
                }
        ));
    }
}
