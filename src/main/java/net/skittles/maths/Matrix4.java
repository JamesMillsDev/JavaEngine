package net.skittles.maths;

public class Matrix4
{
    public static Matrix4 identity()
    {
        return new Matrix4(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        );
    }

    public static Matrix4 zero()
    {
        return new Matrix4(
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
        );
    }

    public static Matrix4 translation(float tx, float ty, float tz)
    {
        return new Matrix4(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                tx, ty, tz, 1
        );
    }

    public static Matrix4 rotationX(float radians)
    {
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        return new Matrix4(
                1,    0,   0, 0,
                0,  cos, sin, 0,
                0, -sin, cos, 0,
                0,    0,   0, 1
        );
    }

    public static Matrix4 rotationY(float radians)
    {
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        return new Matrix4(
                cos, 0, -sin, 0,
                0, 1,    0, 0,
                sin, 0,  cos, 0,
                0, 0,    0, 1
        );
    }

    public static Matrix4 rotationZ(float radians)
    {
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        return new Matrix4(
                cos, sin, 0, 0,
                -sin, cos, 0, 0,
                0,   0, 1, 0,
                0,   0, 0, 1
        );
    }

    public static Matrix4 scale(float sx, float sy, float sz)
    {
        return new Matrix4(
                sx,  0,  0, 0,
                0, sy,  0, 0,
                0,  0, sz, 0,
                0,  0,  0, 1
        );
    }

    public static Matrix4 perspective(float fovY, float aspect, float near, float far)
    {
        float tanHalfFov = (float) Math.tan(fovY / 2.0F);
        float rangeInv = 1.0F / (near - far);
        return new Matrix4(
                1.0F / (aspect * tanHalfFov), 0,                    0,                              0,
                0,                            1.0F / tanHalfFov,    0,                              0,
                0,                            0,                    (near + far) * rangeInv,        -1,
                0,                            0,                    2.0F * near * far * rangeInv,   0
        );
    }

    public static Matrix4 orthographic(float left, float right, float bottom, float top, float near, float far)
    {
        return new Matrix4(
                2.0F / (right - left),               0,                                   0,                                0,
                0,                                   2.0F / (top - bottom),               0,                                0,
                0,                                   0,                                   -2.0F / (far - near),             0,
                -(right + left) / (right - left),    -(top + bottom) / (top - bottom),    -(far + near) / (far - near),     1
        );
    }

    public static Matrix4 lookAt(Vector3 eye, Vector3 center, Vector3 up)
    {
        Vector3 f = center.subtract(eye).normalised();
        Vector3 r = f.cross(up).normalised();
        Vector3 u = r.cross(f);

        return new Matrix4(
                r.x(),  u.x(), -f.x(), 0,
                r.y(),  u.y(), -f.y(), 0,
                r.z(),  u.z(), -f.z(), 0,
                -r.dot(eye), -u.dot(eye), f.dot(eye), 1
        );
    }

    private Vector4 col1;
    private Vector4 col2;
    private Vector4 col3;
    private Vector4 col4;

    public Matrix4(Vector4 col1, Vector4 col2, Vector4 col3, Vector4 col4)
    {
        this.col1 = new Vector4(col1.x(), col1.y(), col1.z(), col1.w());
        this.col2 = new Vector4(col2.x(), col2.y(), col2.z(), col2.w());
        this.col3 = new Vector4(col3.x(), col3.y(), col3.z(), col3.w());
        this.col4 = new Vector4(col4.x(), col4.y(), col4.z(), col4.w());
    }

    public Matrix4(float m00, float m01, float m02, float m03,
                   float m10, float m11, float m12, float m13,
                   float m20, float m21, float m22, float m23,
                   float m30, float m31, float m32, float m33)
    {
        this.col1 = new Vector4(m00, m01, m02, m03);
        this.col2 = new Vector4(m10, m11, m12, m13);
        this.col3 = new Vector4(m20, m21, m22, m23);
        this.col4 = new Vector4(m30, m31, m32, m33);
    }

    public void setRotationX(float radians)
    {
        float cos     = (float) Math.cos(radians);
        float sin     = (float) Math.sin(radians);
        Vector3 scale = getScale();
        this.col2 = new Vector4(0.0F,  cos * scale.y(), sin * scale.y(), 0.0F);
        this.col3 = new Vector4(0.0F, -sin * scale.z(), cos * scale.z(), 0.0F);
    }

    public void setRotationY(float radians)
    {
        float cos     = (float) Math.cos(radians);
        float sin     = (float) Math.sin(radians);
        Vector3 scale = getScale();
        this.col1 = new Vector4( cos * scale.x(), 0.0F, -sin * scale.x(), 0.0F);
        this.col3 = new Vector4( sin * scale.z(), 0.0F,  cos * scale.z(), 0.0F);
    }

    public void setRotationZ(float radians)
    {
        float cos     = (float) Math.cos(radians);
        float sin     = (float) Math.sin(radians);
        Vector3 scale = getScale();
        this.col1 = new Vector4(cos * scale.x(),  sin * scale.x(), 0.0F, 0.0F);
        this.col2 = new Vector4(-sin * scale.y(), cos * scale.y(), 0.0F, 0.0F);
    }

    public void setScale(float sx, float sy, float sz)
    {
        float lenX = new Vector3(this.col1.x(), this.col1.y(), this.col1.z()).magnitude();
        float lenY = new Vector3(this.col2.x(), this.col2.y(), this.col2.z()).magnitude();
        float lenZ = new Vector3(this.col3.x(), this.col3.y(), this.col3.z()).magnitude();
        if (lenX != 0.0F) { this.col1 = new Vector4(this.col1.x() / lenX * sx, this.col1.y() / lenX * sx, this.col1.z() / lenX * sx, 0.0F); }
        if (lenY != 0.0F) { this.col2 = new Vector4(this.col2.x() / lenY * sy, this.col2.y() / lenY * sy, this.col2.z() / lenY * sy, 0.0F); }
        if (lenZ != 0.0F) { this.col3 = new Vector4(this.col3.x() / lenZ * sz, this.col3.y() / lenZ * sz, this.col3.z() / lenZ * sz, 0.0F); }
    }

    public void setTranslation(float tx, float ty, float tz)
    {
        this.col4 = new Vector4(tx, ty, tz, 1.0F);
    }

    public Vector3 getTranslation()
    {
        return new Vector3(this.col4.x(), this.col4.y(), this.col4.z());
    }

    public Vector3 getScale()
    {
        return new Vector3(
                new Vector3(this.col1.x(), this.col1.y(), this.col1.z()).magnitude(),
                new Vector3(this.col2.x(), this.col2.y(), this.col2.z()).magnitude(),
                new Vector3(this.col3.x(), this.col3.y(), this.col3.z()).magnitude()
        );
    }

    public Vector4 getColumn(int index)
    {
        return switch (index)
        {
            case 0 -> new Vector4(this.col1.x(), this.col1.y(), this.col1.z(), this.col1.w());
            case 1 -> new Vector4(this.col2.x(), this.col2.y(), this.col2.z(), this.col2.w());
            case 2 -> new Vector4(this.col3.x(), this.col3.y(), this.col3.z(), this.col3.w());
            case 3 -> new Vector4(this.col4.x(), this.col4.y(), this.col4.z(), this.col4.w());
            default -> throw new IndexOutOfBoundsException("Column index out of bounds: " + index);
        };
    }

    public void setColumn(int index, Vector4 column)
    {
        switch (index)
        {
            case 0 -> this.col1 = new Vector4(column.x(), column.y(), column.z(), column.w());
            case 1 -> this.col2 = new Vector4(column.x(), column.y(), column.z(), column.w());
            case 2 -> this.col3 = new Vector4(column.x(), column.y(), column.z(), column.w());
            case 3 -> this.col4 = new Vector4(column.x(), column.y(), column.z(), column.w());
            default -> throw new IndexOutOfBoundsException("Column index out of bounds: " + index);
        }
    }

    public Vector4 getRow(int index)
    {
        return switch (index)
        {
            case 0 -> new Vector4(this.col1.x(), this.col2.x(), this.col3.x(), this.col4.x());
            case 1 -> new Vector4(this.col1.y(), this.col2.y(), this.col3.y(), this.col4.y());
            case 2 -> new Vector4(this.col1.z(), this.col2.z(), this.col3.z(), this.col4.z());
            case 3 -> new Vector4(this.col1.w(), this.col2.w(), this.col3.w(), this.col4.w());
            default -> throw new IndexOutOfBoundsException("Row index out of bounds: " + index);
        };
    }

    public float get(int row, int col)
    {
        Vector4 column = switch (col)
        {
            case 0 -> this.col1;
            case 1 -> this.col2;
            case 2 -> this.col3;
            case 3 -> this.col4;
            default -> throw new IndexOutOfBoundsException("Index out of bounds: (" + row + ", " + col + ")");
        };

        return switch (row)
        {
            case 0 -> column.x();
            case 1 -> column.y();
            case 2 -> column.z();
            case 3 -> column.w();
            default -> throw new IndexOutOfBoundsException("Index out of bounds: (" + row + ", " + col + ")");
        };
    }

    public void set(int row, int col, float value)
    {
        Vector4 column = switch (col)
        {
            case 0 -> this.col1;
            case 1 -> this.col2;
            case 2 -> this.col3;
            case 3 -> this.col4;
            default -> throw new IndexOutOfBoundsException("Index out of bounds: (" + row + ", " + col + ")");
        };

        switch (row)
        {
            case 0 -> column.setX(value);
            case 1 -> column.setY(value);
            case 2 -> column.setZ(value);
            case 3 -> column.setW(value);
            default -> throw new IndexOutOfBoundsException("Index out of bounds: (" + row + ", " + col + ")");
        }
    }

    public Matrix4 add(Matrix4 other)
    {
        return new Matrix4(
                this.col1.add(other.col1),
                this.col2.add(other.col2),
                this.col3.add(other.col3),
                this.col4.add(other.col4)
        );
    }

    public Matrix4 subtract(Matrix4 other)
    {
        return new Matrix4(
                this.col1.subtract(other.col1),
                this.col2.subtract(other.col2),
                this.col3.subtract(other.col3),
                this.col4.subtract(other.col4)
        );
    }

    public Matrix4 multiply(float scalar)
    {
        return new Matrix4(
                this.col1.multiply(scalar),
                this.col2.multiply(scalar),
                this.col3.multiply(scalar),
                this.col4.multiply(scalar)
        );
    }

    public Vector4 multiply(Vector4 v)
    {
        return new Vector4(
                this.col1.x() * v.x() + this.col2.x() * v.y() + this.col3.x() * v.z() + this.col4.x() * v.w(),
                this.col1.y() * v.x() + this.col2.y() * v.y() + this.col3.y() * v.z() + this.col4.y() * v.w(),
                this.col1.z() * v.x() + this.col2.z() * v.y() + this.col3.z() * v.z() + this.col4.z() * v.w(),
                this.col1.w() * v.x() + this.col2.w() * v.y() + this.col3.w() * v.z() + this.col4.w() * v.w()
        );
    }

    public Matrix4 multiply(Matrix4 other)
    {
        return new Matrix4(
                this.col1.x() * other.col1.x() + this.col2.x() * other.col1.y() + this.col3.x() * other.col1.z() + this.col4.x() * other.col1.w(),
                this.col1.y() * other.col1.x() + this.col2.y() * other.col1.y() + this.col3.y() * other.col1.z() + this.col4.y() * other.col1.w(),
                this.col1.z() * other.col1.x() + this.col2.z() * other.col1.y() + this.col3.z() * other.col1.z() + this.col4.z() * other.col1.w(),
                this.col1.w() * other.col1.x() + this.col2.w() * other.col1.y() + this.col3.w() * other.col1.z() + this.col4.w() * other.col1.w(),
                this.col1.x() * other.col2.x() + this.col2.x() * other.col2.y() + this.col3.x() * other.col2.z() + this.col4.x() * other.col2.w(),
                this.col1.y() * other.col2.x() + this.col2.y() * other.col2.y() + this.col3.y() * other.col2.z() + this.col4.y() * other.col2.w(),
                this.col1.z() * other.col2.x() + this.col2.z() * other.col2.y() + this.col3.z() * other.col2.z() + this.col4.z() * other.col2.w(),
                this.col1.w() * other.col2.x() + this.col2.w() * other.col2.y() + this.col3.w() * other.col2.z() + this.col4.w() * other.col2.w(),
                this.col1.x() * other.col3.x() + this.col2.x() * other.col3.y() + this.col3.x() * other.col3.z() + this.col4.x() * other.col3.w(),
                this.col1.y() * other.col3.x() + this.col2.y() * other.col3.y() + this.col3.y() * other.col3.z() + this.col4.y() * other.col3.w(),
                this.col1.z() * other.col3.x() + this.col2.z() * other.col3.y() + this.col3.z() * other.col3.z() + this.col4.z() * other.col3.w(),
                this.col1.w() * other.col3.x() + this.col2.w() * other.col3.y() + this.col3.w() * other.col3.z() + this.col4.w() * other.col3.w(),
                this.col1.x() * other.col4.x() + this.col2.x() * other.col4.y() + this.col3.x() * other.col4.z() + this.col4.x() * other.col4.w(),
                this.col1.y() * other.col4.x() + this.col2.y() * other.col4.y() + this.col3.y() * other.col4.z() + this.col4.y() * other.col4.w(),
                this.col1.z() * other.col4.x() + this.col2.z() * other.col4.y() + this.col3.z() * other.col4.z() + this.col4.z() * other.col4.w(),
                this.col1.w() * other.col4.x() + this.col2.w() * other.col4.y() + this.col3.w() * other.col4.z() + this.col4.w() * other.col4.w()
        );
    }

    public Matrix4 transpose()
    {
        return new Matrix4(
                this.col1.x(), this.col2.x(), this.col3.x(), this.col4.x(),
                this.col1.y(), this.col2.y(), this.col3.y(), this.col4.y(),
                this.col1.z(), this.col2.z(), this.col3.z(), this.col4.z(),
                this.col1.w(), this.col2.w(), this.col3.w(), this.col4.w()
        );
    }

    public float determinant()
    {
        float a = this.col1.x(), b = this.col1.y(), c = this.col1.z(), d = this.col1.w();
        float e = this.col2.x(), f = this.col2.y(), g = this.col2.z(), h = this.col2.w();
        float i = this.col3.x(), j = this.col3.y(), k = this.col3.z(), l = this.col3.w();
        float m = this.col4.x(), n = this.col4.y(), o = this.col4.z(), p = this.col4.w();

        return a * (f * (k * p - o * l) - j * (g * p - o * h) + n * (g * l - k * h))
                - e * (b * (k * p - o * l) - j * (c * p - o * d) + n * (c * l - k * d))
                + i * (b * (g * p - o * h) - f * (c * p - o * d) + n * (c * h - g * d))
                - m * (b * (g * l - k * h) - f * (c * l - k * d) + j * (c * h - g * d));
    }

    public Matrix4 inverse()
    {
        float a = this.col1.x(), b = this.col1.y(), c = this.col1.z(), d = this.col1.w();
        float e = this.col2.x(), f = this.col2.y(), g = this.col2.z(), h = this.col2.w();
        float i = this.col3.x(), j = this.col3.y(), k = this.col3.z(), l = this.col3.w();
        float m = this.col4.x(), n = this.col4.y(), o = this.col4.z(), p = this.col4.w();

        float det = this.determinant();
        if (det == 0.0F)
        {
            throw new ArithmeticException("Matrix4 is not invertible (determinant is zero)");
        }
        float invDet = 1.0F / det;

        return new Matrix4(
                (f * (k * p - o * l) - j * (g * p - o * h) + n * (g * l - k * h)) * invDet,
                -(b * (k * p - o * l) - j * (c * p - o * d) + n * (c * l - k * d)) * invDet,
                (b * (g * p - o * h) - f * (c * p - o * d) + n * (c * h - g * d)) * invDet,
                -(b * (g * l - k * h) - f * (c * l - k * d) + j * (c * h - g * d)) * invDet,
                -(e * (k * p - o * l) - i * (g * p - o * h) + m * (g * l - k * h)) * invDet,
                (a * (k * p - o * l) - i * (c * p - o * d) + m * (c * l - k * d)) * invDet,
                -(a * (g * p - o * h) - e * (c * p - o * d) + m * (c * h - g * d)) * invDet,
                (a * (g * l - k * h) - e * (c * l - k * d) + i * (c * h - g * d)) * invDet,
                (e * (j * p - n * l) - i * (f * p - n * h) + m * (f * l - j * h)) * invDet,
                -(a * (j * p - n * l) - i * (b * p - n * d) + m * (b * l - j * d)) * invDet,
                (a * (f * p - n * h) - e * (b * p - n * d) + m * (b * h - f * d)) * invDet,
                -(a * (f * l - j * h) - e * (b * l - j * d) + i * (b * h - f * d)) * invDet,
                -(e * (j * o - n * k) - i * (f * o - n * g) + m * (f * k - j * g)) * invDet,
                (a * (j * o - n * k) - i * (b * o - n * c) + m * (b * k - j * c)) * invDet,
                -(a * (f * o - n * g) - e * (b * o - n * c) + m * (b * g - f * c)) * invDet,
                (a * (f * k - j * g) - e * (b * k - j * c) + i * (b * g - f * c)) * invDet
        );
    }

    @Override
    public String toString()
    {
        return "[" + this.col1.x() + ", " + this.col2.x() + ", " + this.col3.x() + ", " + this.col4.x() + "]\n"
                + "[" + this.col1.y() + ", " + this.col2.y() + ", " + this.col3.y() + ", " + this.col4.y() + "]\n"
                + "[" + this.col1.z() + ", " + this.col2.z() + ", " + this.col3.z() + ", " + this.col4.z() + "]\n"
                + "[" + this.col1.w() + ", " + this.col2.w() + ", " + this.col3.w() + ", " + this.col4.w() + "]";
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

        Matrix4 m = (Matrix4) obj;
        return this.col1.equals(m.col1) && this.col2.equals(m.col2) && this.col3.equals(m.col3) && this.col4.equals(m.col4);
    }

    @Override
    public int hashCode()
    {
        int result = col1.hashCode();
        result = 31 * result + col2.hashCode();
        result = 31 * result + col3.hashCode();
        result = 31 * result + col4.hashCode();
        return result;
    }
}