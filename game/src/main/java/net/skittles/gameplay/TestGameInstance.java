package net.skittles.gameplay;

import api.skittles.gameplay.GameInstance;
import net.skittles.gameplay.actors.PlayerActor;
import net.skittles.gameplay.levels.TestLevel;

public class TestGameInstance extends GameInstance
{
    @Override
    public void init()
    {
       levelManager.load(TestLevel::new, "Test");
       levelManager.open("Test");
    }

    @Override
    public void tick(float dt)
    {

    }

    @Override
    public void render()
    {

    }

    @Override
    public void shutdown()
    {

    }
}
