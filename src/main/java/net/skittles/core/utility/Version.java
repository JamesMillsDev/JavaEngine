package net.skittles.core.utility;

import java.util.ArrayList;

public record Version(int major, int minor, int patch)
{
    public static Version from(ArrayList<Integer> values)
    {
        return new Version(values.get(0), values.get(1), values.get(2));
    }

    @Override
    public int hashCode()
    {
        return Integer.hashCode(major) ^ Integer.hashCode(minor) ^ Integer.hashCode(patch);
    }

    @Override
    public String toString()
    {
        return this.major + "." + this.minor + "." + this.patch;
    }
}
