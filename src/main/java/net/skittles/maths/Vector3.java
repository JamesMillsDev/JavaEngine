package net.skittles.maths;

public class Vector3
{
    public static final Vector3 ZERO = new Vector3(0F, 0F, 0F);
    public static final Vector3 HALF = new Vector3(.5F, .5F, .5F);
    public static final Vector3 ONE = new Vector3(1F, 1F, 1F);
    public static final Vector3 UP = new Vector3(0F, 1F, 0F);
    public static final Vector3 DOWN = new Vector3(0F, -1F, 0F);
    public static final Vector3 RIGHT = new Vector3(1F, 0F, 0F);
    public static final Vector3 LEFT = new Vector3(-1F, 0F, 0F);
    public static final Vector3 FORWARD = new Vector3(0F, 0F, 1F);
    public static final Vector3 BACK = new Vector3(0F, 0F, -1F);

    private float x;
    private float y;
    private float z;

    public Vector3()
    {
        this.x = 0.0F;
        this.y = 0.0F;
        this.z = 0.0F;
    }

    public Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float x()
    {
        return this.x;
    }

    public float y()
    {
        return this.y;
    }

    public float z()
    {
        return this.z;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public void setZ(float z)
    {
        this.z = z;
    }

    public Vector3 cross(Vector3 other)
    {
        return new Vector3(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    public float dot(Vector3 other)
    {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public void normalise()
    {
        float magnitude = this.magnitude();
        if (magnitude != 0.0F)
        {
            this.x /= magnitude;
            this.y /= magnitude;
            this.z /= magnitude;
        }
        else
        {
            this.x = 0.0F;
            this.y = 0.0F;
            this.z = 0.0F;
        }
    }

    public Vector3 normalised()
    {
        float magnitude = this.magnitude();
        return magnitude != 0.0F ? new Vector3(this.x / magnitude, this.y / magnitude, this.z / magnitude) : new Vector3(0.0F, 0.0F, 0.0F);
    }

    public float magnitude()
    {
        return (float) Math.sqrt(this.dot(this));
    }

    public float magnitudeSqr()
    {
        return this.dot(this);
    }

    public Vector3 add(Vector3 other)
    {
        return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3 subtract(Vector3 other)
    {
        return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3 multiply(float other)
    {
        return new Vector3(this.x * other, this.y * other, this.z * other);
    }

    public Vector3 multiply(Vector3 other)
    {
        return new Vector3(this.x * other.x, this.y * other.y, this.z * other.z);
    }

    public Vector3 divide(float other)
    {
        return new Vector3(this.x / other, this.y / other, this.z / other);
    }

    public Vector3 divide(Vector3 other)
    {
        return new Vector3(this.x / other.x, this.y / other.y, this.z / other.z);
    }

    @Override
    public String toString()
    {
        return "(" + this.x + "," + this.y + "," + this.z + ")";
    }

    @Override
    public int hashCode()
    {
        int result = Float.hashCode(x);
        result = 31 * result + Float.hashCode(y);
        result = 31 * result + Float.hashCode(z);
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

        Vector3 v = (Vector3) obj;
        return Math.abs(this.x - v.x) < 1e-6f && Math.abs(this.y - v.y) < 1e-6f && Math.abs(this.z - v.z) < 1e-6f;
    }
}
