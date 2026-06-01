package api.skittles.gameplay;

import api.skittles.gameplay.levels.LevelManager;

public abstract class GameInstance
{
    protected final LevelManager levelManager;

    public GameInstance()
    {
        levelManager = new LevelManager();
    }

    public abstract void init();

    public abstract void tick(float dt);

    public abstract void render();

    public abstract void shutdown();

    public LevelManager getLevelManager()
    {
        return this.levelManager;
    }
}
