package net.skittles;

import api.skittles.core.Application;
import api.skittles.gameplay.GameInstance;

public class Main
{
    public static class TestGame extends GameInstance
    {

    }

    public static void main(String[] args)
    {
        Integer retVal = 0;

        try
        {
            retVal = Application.open(TestGame::new);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.exit(retVal);
    }
}
