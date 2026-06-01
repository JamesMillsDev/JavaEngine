package api.skittles.gameplay.levels;

import api.skittles.gameplay.actors.Actor;
import api.skittles.utility.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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

    public void load(Level level, String id)
    {
        levels.put(id, level);
    }

    public void tick(float dt)
    {
        if(this.activeLevel == null)
        {
            return;
        }

        for(Pair<Actor, Consumer<Actor>> destructions : this.activeLevel.pendingActions)
        {
            destructions.second.accept(destructions.first);
        }

        this.activeLevel.pendingActions.clear();
        this.activeLevel.root.tick(dt);
    }

    public void render()
    {
        this.activeLevel.render();
    }
}
