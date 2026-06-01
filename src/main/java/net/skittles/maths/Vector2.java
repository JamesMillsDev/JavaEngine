package net.skittles.maths;

public class Vector2
{
    public static final Vector2 ZERO = new Vector2(0F, 0F);
    public static final Vector2 HALF = new Vector2(.5F, .5F);
    public static final Vector2 ONE = new Vector2(1F, 1F);
    public static final Vector2 UP = new Vector2(0F, 1F);
    public static final Vector2 DOWN = new Vector2(0F, -1F);
    public static final Vector2 RIGHT = new Vector2(1F, 0F);
    public static final Vector2 LEFT = new Vector2(-1F, 0F);

    private float x;
    private float y;

    public Vector2()
    {
        this.x = 0.0F;
        this.y = 0.0F;
    }

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public float x()
    {
        return this.x;
    }

    public float y()
    {
        return this.y;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float dot(Vector2 other)
    {
        return this.x * other.x + this.y * other.y;
    }

    public void normalise()
    {
        float magnitude = this.magnitude();
        if (magnitude != 0.0F)
        {
            this.x /= magnitude;
            this.y /= magnitude;
        }
        else
        {
            this.x = 0.0F;
            this.y = 0.0F;
        }
    }

    public Vector2 normalised()
    {
        float magnitude = this.magnitude();
        return magnitude != 0.0F ? new Vector2(this.x / magnitude, this.y / magnitude) : new Vector2(0.0F, 0.0F);
    }

    public float magnitude()
    {
        return (float) Math.sqrt(this.dot(this));
    }

    public float magnitudeSqr()
    {
        return this.dot(this);
    }

    public Vector2 add(Vector2 other)
    {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    public Vector2 subtract(Vector2 other)
    {
        return new Vector2(this.x - other.x, this.y - other.y);
    }

    public Vector2 multiply(float other)
    {
        return new Vector2(this.x * other, this.y * other);
    }

    public Vector2 multiply(Vector2 other)
    {
        return new Vector2(this.x * other.x, this.y * other.y);
    }

    public Vector2 divide(float other)
    {
        return new Vector2(this.x / other, this.y / other);
    }

    public Vector2 divide(Vector2 other)
    {
        return new Vector2(this.x / other.x, this.y / other.y);
    }

    @Override
    public String toString()
    {
        return "(" + this.x + "," + this.y + ")";
    }

    @Override
    public int hashCode()
    {
        int result = Float.hashCode(x);
        result = 31 * result + Float.hashCode(y);
        return result;
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

        Vector2 v = (Vector2) obj;
        return Math.abs(this.x - v.x) < 1e-6f && Math.abs(this.y - v.y) < 1e-6f;
    }
}
