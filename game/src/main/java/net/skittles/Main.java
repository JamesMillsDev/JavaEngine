package net.skittles;

import api.skittles.core.Application;
import net.skittles.gameplay.TestGameInstance;

public class Main
{
    public static void main(String[] args)
    {
        Integer retVal = 0;

        try
        {
            retVal = Application.open(TestGameInstance::new);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.exit(retVal);
    }
}
