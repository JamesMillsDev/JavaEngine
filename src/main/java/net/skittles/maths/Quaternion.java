package net.skittles.maths;

public class Quaternion
{
    public static Quaternion identity()
    {
        return new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
    }

    public static Quaternion fromAxisAngle(Vector3 axis, float radians)
    {
        Vector3 normalised = axis.normalised();
        float halfAngle = radians / 2.0F;
        float sin = (float) Math.sin(halfAngle);
        float cos = (float) Math.cos(halfAngle);
        return new Quaternion(normalised.x() * sin, normalised.y() * sin, normalised.z() * sin, cos);
    }

    public static Quaternion fromEuler(float pitch, float yaw, float roll)
    {
        float halfPitch = pitch / 2.0F;
        float halfYaw = yaw / 2.0F;
        float halfRoll = roll / 2.0F;

        float cp = (float) Math.cos(halfPitch);
        float sp = (float) Math.sin(halfPitch);
        float cy = (float) Math.cos(halfYaw);
        float sy = (float) Math.sin(halfYaw);
        float cr = (float) Math.cos(halfRoll);
        float sr = (float) Math.sin(halfRoll);

        return new Quaternion(
                cr * sp * cy + sr * cp * sy,
                cr * cp * sy - sr * sp * cy,
                sr * cp * cy - cr * sp * sy,
                cr * cp * cy + sr * sp * sy
        );
    }

    public static Quaternion fromMatrix3(Matrix3 m)
    {
        float trace = m.get(0, 0) + m.get(1, 1) + m.get(2, 2);

        if (trace > 0.0F)
        {
            float s = 0.5F / (float) Math.sqrt(trace + 1.0F);
            return new Quaternion(
                    (m.get(2, 1) - m.get(1, 2)) * s,
                    (m.get(0, 2) - m.get(2, 0)) * s,
                    (m.get(1, 0) - m.get(0, 1)) * s,
                    0.25F / s
            );
        }
        else if (m.get(0, 0) > m.get(1, 1) && m.get(0, 0) > m.get(2, 2))
        {
            float s = 2.0F * (float) Math.sqrt(1.0F + m.get(0, 0) - m.get(1, 1) - m.get(2, 2));
            return new Quaternion(
                    0.25F * s,
                    (m.get(0, 1) + m.get(1, 0)) / s,
                    (m.get(0, 2) + m.get(2, 0)) / s,
                    (m.get(2, 1) - m.get(1, 2)) / s
            );
        }
        else if (m.get(1, 1) > m.get(2, 2))
        {
            float s = 2.0F * (float) Math.sqrt(1.0F + m.get(1, 1) - m.get(0, 0) - m.get(2, 2));
            return new Quaternion(
                    (m.get(0, 1) + m.get(1, 0)) / s,
                    0.25F * s,
                    (m.get(1, 2) + m.get(2, 1)) / s,
                    (m.get(0, 2) - m.get(2, 0)) / s
            );
        }
        else
        {
            float s = 2.0F * (float) Math.sqrt(1.0F + m.get(2, 2) - m.get(0, 0) - m.get(1, 1));
            return new Quaternion(
                    (m.get(0, 2) + m.get(2, 0)) / s,
                    (m.get(1, 2) + m.get(2, 1)) / s,
                    0.25F * s,
                    (m.get(1, 0) - m.get(0, 1)) / s
            );
        }
    }

    public static Quaternion fromMatrix4(Matrix4 m)
    {
        float trace = m.get(0, 0) + m.get(1, 1) + m.get(2, 2);

        if (trace > 0.0F)
        {
            float s = 0.5F / (float) Math.sqrt(trace + 1.0F);
            return new Quaternion(
                    (m.get(2, 1) - m.get(1, 2)) * s,
                    (m.get(0, 2) - m.get(2, 0)) * s,
                    (m.get(1, 0) - m.get(0, 1)) * s,
                    0.25F / s
            );
        }
        else if (m.get(0, 0) > m.get(1, 1) && m.get(0, 0) > m.get(2, 2))
        {
            float s = 2.0F * (float) Math.sqrt(1.0F + m.get(0, 0) - m.get(1, 1) - m.get(2, 2));
            return new Quaternion(
                    0.25F * s,
                    (m.get(0, 1) + m.get(1, 0)) / s,
                    (m.get(0, 2) + m.get(2, 0)) / s,
                    (m.get(2, 1) - m.get(1, 2)) / s
            );
        }
        else if (m.get(1, 1) > m.get(2, 2))
        {
            float s = 2.0F * (float) Math.sqrt(1.0F + m.get(1, 1) - m.get(0, 0) - m.get(2, 2));
            return new Quaternion(
                    (m.get(0, 1) + m.get(1, 0)) / s,
                    0.25F * s,
                    (m.get(1, 2) + m.get(2, 1)) / s,
                    (m.get(0, 2) - m.get(2, 0)) / s
            );
        }
        else
        {
            float s = 2.0F * (float) Math.sqrt(1.0F + m.get(2, 2) - m.get(0, 0) - m.get(1, 1));
            return new Quaternion(
                    (m.get(0, 2) + m.get(2, 0)) / s,
                    (m.get(1, 2) + m.get(2, 1)) / s,
                    0.25F * s,
                    (m.get(1, 0) - m.get(0, 1)) / s
            );
        }
    }

    public static Quaternion slerp(Quaternion a, Quaternion b, float t)
    {
        float dot = a.dot(b);

        // If dot is negative, negate one quaternion to take the shorter arc
        if (dot < 0.0F)
        {
            b = b.negate();
            dot = -dot;
        }

        // If quaternions are very close, fall back to linear interpolation
        if (dot > 0.9995F)
        {
            return new Quaternion(
                    a.x + t * (b.x - a.x),
                    a.y + t * (b.y - a.y),
                    a.z + t * (b.z - a.z),
                    a.w + t * (b.w - a.w)
            ).normalised();
        }

        float theta0 = (float) Math.acos(dot);
        float theta = theta0 * t;
        float sinTheta = (float) Math.sin(theta);
        float sinTheta0 = (float) Math.sin(theta0);

        float s0 = (float) Math.cos(theta) - dot * sinTheta / sinTheta0;
        float s1 = sinTheta / sinTheta0;

        return new Quaternion(
                s0 * a.x + s1 * b.x,
                s0 * a.y + s1 * b.y,
                s0 * a.z + s1 * b.z,
                s0 * a.w + s1 * b.w
        );
    }

    private float x;
    private float y;
    private float z;
    private float w;

    public Quaternion(float x, float y, float z, float w)
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

    public float dot(Quaternion other)
    {
        return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
    }

    public float magnitude()
    {
        return (float) Math.sqrt(this.dot(this));
    }

    public float magnitudeSqr()
    {
        return this.dot(this);
    }

    public Quaternion normalised()
    {
        float magnitude = this.magnitude();
        return magnitude != 0.0F
                ? new Quaternion(this.x / magnitude, this.y / magnitude, this.z / magnitude, this.w / magnitude)
                : new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
    }

    public void normalise()
    {
        float magnitude = this.magnitude();
        if (magnitude != 0.0F)
        {
            this.x /= magnitude;
            this.y /= magnitude;
            this.z /= magnitude;
            this.w /= magnitude;
        }
        else
        {
            this.x = 0.0F;
            this.y = 0.0F;
            this.z = 0.0F;
            this.w = 1.0F;
        }
    }

    public Quaternion conjugate()
    {
        return new Quaternion(-this.x, -this.y, -this.z, this.w);
    }

    public Quaternion inverse()
    {
        float magSqr = this.magnitudeSqr();
        if (magSqr == 0.0F)
        {
            throw new ArithmeticException("Quaternion is not invertible (magnitude is zero)");
        }
        float invMagSqr = 1.0F / magSqr;
        return new Quaternion(-this.x * invMagSqr, -this.y * invMagSqr, -this.z * invMagSqr, this.w * invMagSqr);
    }

    public Quaternion negate()
    {
        return new Quaternion(-this.x, -this.y, -this.z, -this.w);
    }

    public Quaternion multiply(Quaternion other)
    {
        return new Quaternion(
                this.w * other.x + this.x * other.w + this.y * other.z - this.z * other.y,
                this.w * other.y - this.x * other.z + this.y * other.w + this.z * other.x,
                this.w * other.z + this.x * other.y - this.y * other.x + this.z * other.w,
                this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z
        );
    }

    public Vector3 rotate(Vector3 v)
    {
        // Optimised: p' = q * p * q^-1 expanded without full quaternion multiply
        Vector3 u = new Vector3(this.x, this.y, this.z);
        float s = this.w;
        float d = u.dot(v);
        float d2 = u.dot(u);

        return u.multiply(2.0F * d)
                .add(v.multiply(s * s - d2))
                .add(u.cross(v).multiply(2.0F * s));
    }

    public Matrix3 toMatrix3()
    {
        float xx = this.x * this.x;
        float yy = this.y * this.y;
        float zz = this.z * this.z;
        float xy = this.x * this.y;
        float xz = this.x * this.z;
        float yz = this.y * this.z;
        float wx = this.w * this.x;
        float wy = this.w * this.y;
        float wz = this.w * this.z;

        return new Matrix3(
                1 - 2 * (yy + zz), 2 * (xy + wz), 2 * (xz - wy),
                2 * (xy - wz), 1 - 2 * (xx + zz), 2 * (yz + wx),
                2 * (xz + wy), 2 * (yz - wx), 1 - 2 * (xx + yy)
        );
    }

    public Matrix4 toMatrix4()
    {
        float xx = this.x * this.x;
        float yy = this.y * this.y;
        float zz = this.z * this.z;
        float xy = this.x * this.y;
        float xz = this.x * this.z;
        float yz = this.y * this.z;
        float wx = this.w * this.x;
        float wy = this.w * this.y;
        float wz = this.w * this.z;

        return new Matrix4(
                1 - 2 * (yy + zz), 2 * (xy + wz), 2 * (xz - wy), 0,
                2 * (xy - wz), 1 - 2 * (xx + zz), 2 * (yz + wx), 0,
                2 * (xz + wy), 2 * (yz - wx), 1 - 2 * (xx + yy), 0,
                0, 0, 0, 1
        );
    }

    public Vector3 toEuler()
    {
        // Pitch (x-axis)
        float sinPitch = 2.0F * (this.w * this.x + this.y * this.z);
        float cosPitch = 1.0F - 2.0F * (this.x * this.x + this.y * this.y);
        float pitch = (float) Math.atan2(sinPitch, cosPitch);

        // Yaw (y-axis) — clamp to handle numerical drift
        float sinYaw = 2.0F * (this.w * this.y - this.z * this.x);
        sinYaw = Math.max(-1.0F, Math.min(1.0F, sinYaw));
        float yaw = (float) Math.asin(sinYaw);

        // Roll (z-axis)
        float sinRoll = 2.0F * (this.w * this.z + this.x * this.y);
        float cosRoll = 1.0F - 2.0F * (this.y * this.y + this.z * this.z);
        float roll = (float) Math.atan2(sinRoll, cosRoll);

        return new Vector3(pitch, yaw, roll);
    }

    @Override
    public String toString()
    {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";
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

        Quaternion q = (Quaternion) obj;
        return this.x == q.x && this.y == q.y && this.z == q.z && this.w == q.w;
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
}