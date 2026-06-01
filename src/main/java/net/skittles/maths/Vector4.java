package net.skittles.maths;

public class Vector4
{
    public static final Vector4 ZERO = new Vector4(0F, 0F, 0F, 0F);
    public static final Vector4 HALF = new Vector4(.5F, .5F, .5F, .5F);
    public static final Vector4 ONE = new Vector4(1F, 1F, 1F, 1F);

    private float x;
    private float y;
    private float z;
    private float w;

    public Vector4()
    {
        this.x = 0.0F;
        this.y = 0.0F;
        this.z = 0.0F;
        this.w = 0.0F;
    }

    public Vector4(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
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

    public float w()
    {
        return this.w;
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

    public void setW(float w)
    {
        this.w = w;
    }

    public Vector4 cross(Vector4 other)
    {
        return new Vector4(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x,
                this.w
        );
    }

    public float dot(Vector4 other)
    {
        return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
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

    public Vector4 normalised()
    {
        float magnitude = this.magnitude();
        return magnitude != 0.0F ? new Vector4(this.x / magnitude, this.y / magnitude, this.z / magnitude, this.w) : new Vector4(0.0F, 0.0F, 0.0F, 0.0F);
    }

    public float magnitude()
    {
        return (float) Math.sqrt(this.dot(this));
    }

    public float magnitudeSqr()
    {
        return this.dot(this);
    }

    public Vector4 add(Vector4 other)
    {
        return new Vector4(this.x + other.x, this.y + other.y, this.z + other.z, this.w + other.w);
    }

    public Vector4 subtract(Vector4 other)
    {
        return new Vector4(this.x - other.x, this.y - other.y, this.z - other.z, this.w - other.w);
    }

    public Vector4 multiply(float other)
    {
        return new Vector4(this.x * other, this.y * other, this.z * other, this.w * other);
    }

    public Vector4 multiply(Vector4 other)
    {
        return new Vector4(this.x * other.x, this.y * other.y, this.z * other.z, this.w * other.w);
    }

    public Vector4 divide(float other)
    {
        return new Vector4(this.x / other, this.y / other, this.z / other, this.w / other);
    }

    public Vector4 divide(Vector4 other)
    {
        return new Vector4(this.x / other.x, this.y / other.y, this.z / other.z, this.w / other.w);
    }

    @Override
    public String toString()
    {
        return "(" + this.x + "," + this.y + "," + this.z + "," + this.w + ")";
    }

    @Override
    public int hashCode()
    {
        int result = Float.hashCode(x);
        result = 31 * result + Float.hashCode(y);
        result = 31 * result + Float.hashCode(z);
        result = 31 * result + Float.hashCode(w);
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

        Vector4 v = (Vector4) obj;
        return Math.abs(this.x - v.x) < 1e-6f && Math.abs(this.y - v.y) < 1e-6f && Math.abs(this.z - v.z) < 1e-6f && Math.abs(this.w - v.w) < 1e-6f;
    }
}
