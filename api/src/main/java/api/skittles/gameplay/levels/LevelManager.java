package api.skittles.gameplay.levels;

import api.skittles.gameplay.actors.Actor;
import api.skittles.utility.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LevelManager
{
    private Level activeLevel;
    private final Map<String, Level> levels = new HashMap<>();

    public void open(String level)
    {
        if(!levels.containsKey(level))
        {
            return;
        }

        if(activeLevel != null)
        {
            activeLevel.onUnloaded();
        }

        activeLevel = levels.get(level);
        activeLevel.onLoaded();
    }

    public void load(Supplier<Level> level, String id)
    {
        levels.put(id, level.get());
    }

    public void tick(float dt)
    {
        if(this.activeLevel == null)
        {
            return;
        }

        for(Pair<Actor, Consumer<Actor>> actorChange : this.activeLevel.pendingActorChanges)
        {
            actorChange.second.accept(actorChange.first);
        }

        this.activeLevel.pendingActorChanges.clear();
        this.activeLevel.root.tick(dt);
        this.activeLevel.tickActors(dt);
    }

    public void render()
    {
        this.activeLevel.render();
        this.activeLevel.renderActors();
    }
}
