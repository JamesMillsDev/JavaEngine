package net.skittles.gameplay.levels;

import api.skittles.gameplay.levels.Level;
import net.skittles.gameplay.actors.PlayerActor;

public class TestLevel extends Level
{
    @Override
    public void onLoaded()
    {
        PlayerActor player = makeActor(PlayerActor.class);
    }

    @Override
    public String id()
    {
        return "Test";
    }
}
