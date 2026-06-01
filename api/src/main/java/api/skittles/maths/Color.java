package api.skittles.maths;

import java.util.ArrayList;
import java.util.List;

public class Color
{
    public static final Color WHITE = new Color(255, 255, 255, 255);
    public static final Color BLACK = new Color(0, 0, 0, 255);
    public static final Color RED = new Color(255, 0, 0, 255);
    public static final Color GREEN = new Color(0, 255, 0, 255);
    public static final Color BLUE = new Color(0, 0, 255, 255);
    public static final Color YELLOW = new Color(255, 255, 0, 255);
    public static final Color CYAN = new Color(0, 255, 255, 255);
    public static final Color MAGENTA = new Color(255, 0, 255, 255);
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public static Color fromNormalised(float r, float g, float b, float a)
    {
        return new Color(r * 255.0F, g * 255.0F, b * 255.0F, a * 255.0F);
    }

    public static Color fromHex(int hex)
    {
        float r = ((hex >> 16) & 0xFF);
        float g = ((hex >> 8) & 0xFF);
        float b = ((hex) & 0xFF);
        return new Color(r, g, b, 255);
    }

    public static Color fromHex(int hex, float alpha)
    {
        float r = ((hex >> 16) & 0xFF);
        float g = ((hex >> 8) & 0xFF);
        float b = ((hex) & 0xFF);
        return new Color(r, g, b, alpha);
    }

    public static Color fromHSV(float h, float s, float v, float a)
    {
        // h in [0, 360), s and v in [0, 1]
        if (s == 0.0F)
        {
            float grey = v * 255.0F;
            return new Color(grey, grey, grey, a);
        }

        float hSector = h / 60.0F;
        int i = (int) hSector;
        float f = hSector - i;
        float p = v * (1.0F - s);
        float q = v * (1.0F - s * f);
        float t = v * (1.0F - s * (1.0F - f));

        float r, g, b;
        switch (i % 6)
        {
            case 0 ->
            {
                r = v;
                g = t;
                b = p;
            }
            case 1 ->
            {
                r = q;
                g = v;
                b = p;
            }
            case 2 ->
            {
                r = p;
                g = v;
                b = t;
            }
            case 3 ->
            {
                r = p;
                g = q;
                b = v;
            }
            case 4 ->
            {
                r = t;
                g = p;
                b = v;
            }
            default ->
            {
                r = v;
                g = p;
                b = q;
            }
        }

        return new Color(r * 255.0F, g * 255.0F, b * 255.0F, a);
    }

    public static Color fromArraylist(ArrayList<Double> values)
    {
        return new Color(
                values.get(0).floatValue(), values.get(1).floatValue(),
                values.get(2).floatValue(), values.get(3).floatValue()
        );
    }

    private float r;
    private float g;
    private float b;
    private float a;

    public Color(float r, float g, float b)
    {
        this(r, g, b, 255);
    }

    public Color(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public float r()
    {
        return this.r;
    }

    public float g()
    {
        return this.g;
    }

    public float b()
    {
        return this.b;
    }

    public float a()
    {
        return this.a;
    }

    public void setR(float r)
    {
        this.r = r;
    }

    public void setG(float g)
    {
        this.g = g;
    }

    public void setB(float b)
    {
        this.b = b;
    }

    public void setA(float a)
    {
        this.a = a;
    }

    // -------------------------------------------------------------------------
    // Conversion
    // -------------------------------------------------------------------------

    public ArrayList<Float> toOpenGl()
    {
        return new ArrayList<>(List.of(r / 255.0F, g / 255.0F, b / 255.0F, a / 255.0F));
    }

    public int toHex()
    {
        return ((int) this.r << 16) | ((int) this.g << 8) | (int) this.b;
    }

    public ArrayList<Float> toHSV()
    {
        float rn = this.r / 255.0F;
        float gn = this.g / 255.0F;
        float bn = this.b / 255.0F;

        float max = Math.max(rn, Math.max(gn, bn));
        float min = Math.min(rn, Math.min(gn, bn));
        float delta = max - min;

        float h, s, v;
        v = max;
        s = max == 0.0F ? 0.0F : delta / max;

        if (delta == 0.0F)
        {
            h = 0.0F;
        }
        else if (max == rn)
        {
            h = 60.0F * (((gn - bn) / delta) % 6.0F);
        }
        else if (max == gn)
        {
            h = 60.0F * (((bn - rn) / delta) + 2.0F);
        }
        else
        {
            h = 60.0F * (((rn - gn) / delta) + 4.0F);
        }

        if (h < 0.0F)
        {
            h += 360.0F;
        }

        return new ArrayList<>(List.of(h, s, v));
    }

    // -------------------------------------------------------------------------
    // Greyscale & Luminosity
    // -------------------------------------------------------------------------

    public float luminosity()
    {
        // Rec. 709 perceptual weights
        return 0.2126F * (this.r / 255.0F)
                + 0.7152F * (this.g / 255.0F)
                + 0.0722F * (this.b / 255.0F);
    }

    public Color toGreyscale()
    {
        float grey = luminosity() * 255.0F;
        return new Color(grey, grey, grey, this.a);
    }

    public Color toGreyscaleAverage()
    {
        float grey = (this.r + this.g + this.b) / 3.0F;
        return new Color(grey, grey, grey, this.a);
    }

    // -------------------------------------------------------------------------
    // Mixing & Blending
    // -------------------------------------------------------------------------

    public Color mix(Color other, float t)
    {
        t = Math.max(0.0F, Math.min(1.0F, t));
        return new Color(
                this.r + t * (other.r - this.r),
                this.g + t * (other.g - this.g),
                this.b + t * (other.b - this.b),
                this.a + t * (other.a - this.a)
        );
    }

    public Color mixHSV(Color other, float t)
    {
        t = Math.max(0.0F, Math.min(1.0F, t));
        ArrayList<Float> hsv1 = this.toHSV();
        ArrayList<Float> hsv2 = other.toHSV();

        float hDiff = hsv2.get(0) - hsv1.get(0);
        if (hDiff > 180.0F)
        {
            hDiff -= 360.0F;
        }
        else if (hDiff < -180.0F)
        {
            hDiff += 360.0F;
        }

        float h = (hsv1.get(0) + t * hDiff + 360.0F) % 360.0F;
        float s = hsv1.get(1) + t * (hsv2.get(1) - hsv1.get(1));
        float v = hsv1.get(2) + t * (hsv2.get(2) - hsv1.get(2));
        float a = this.a + t * (other.a() - this.a);

        return fromHSV(h, s, v, a);
    }

    public Color add(Color other)
    {
        return new Color(
                Math.min(this.r + other.r, 255.0F),
                Math.min(this.g + other.g, 255.0F),
                Math.min(this.b + other.b, 255.0F),
                Math.min(this.a + other.a, 255.0F)
        );
    }

    public Color multiply(Color other)
    {
        return new Color(
                (this.r / 255.0F) * (other.r / 255.0F) * 255.0F,
                (this.g / 255.0F) * (other.g / 255.0F) * 255.0F,
                (this.b / 255.0F) * (other.b / 255.0F) * 255.0F,
                (this.a / 255.0F) * (other.a / 255.0F) * 255.0F
        );
    }

    public Color withRed(float r)
    {
        return new Color(r, this.g, this.b, this.a);
    }

    public Color withGreen(float g)
    {
        return new Color(this.r, g, this.b, this.a);
    }

    public Color withBlue(float b)
    {
        return new Color(this.r, this.g, b, this.a);
    }

    public Color withAlpha(float a)
    {
        return new Color(this.r, this.g, this.b, a);
    }

    public Color brighten(float amount)
    {
        return new Color(
                Math.min(this.r + amount * 255.0F, 255.0F),
                Math.min(this.g + amount * 255.0F, 255.0F),
                Math.min(this.b + amount * 255.0F, 255.0F),
                this.a
        );
    }

    public Color darken(float amount)
    {
        return brighten(-amount);
    }

    public Color saturate(float amount)
    {
        ArrayList<Float> hsv = this.toHSV();
        float s = Math.max(0.0F, Math.min(1.0F, hsv.get(1) + amount));
        return fromHSV(hsv.get(0), s, hsv.get(2), this.a);
    }

    public Color desaturate(float amount)
    {
        return saturate(-amount);
    }

    // -------------------------------------------------------------------------
    // Object
    // -------------------------------------------------------------------------

    @Override
    public String toString()
    {
        return "(" + this.r + ", " + this.g + ", " + this.b + ", " + this.a + ")";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass())
        {
            return false;
        }

        Color c = (Color) obj;
        return this.r == c.r && this.g == c.g && this.b == c.b && this.a == c.a;
    }

    @Override
    public int hashCode()
    {
        int result = Float.hashCode(r);
        result = 31 * result + Float.hashCode(g);
        result = 31 * result + Float.hashCode(b);
        result = 31 * result + Float.hashCode(a);
        return result;
    }
}