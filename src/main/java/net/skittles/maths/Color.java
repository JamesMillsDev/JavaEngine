package net.skittles.maths;

import java.util.ArrayList;
import java.util.List;

public record Color(float r, float g, float b, float a)
{
    public static Color from(ArrayList<Double> values)
    {
        return new Color(
                values.get(0).floatValue(), values.get(1).floatValue(),
                values.get(2).floatValue(), values.get(3).floatValue()
        );
    }

    public ArrayList<Float> toOpenGl()
    {
        return new ArrayList<>(List.of(r / 255.f, g / 255.f, b / 255.f, a / 255.f));
    }
}
