package api.skittles.maths;

public class Matrix3
{
    public static Matrix3 identity()
    {
        return new Matrix3(
                1, 0, 0,
                0, 1, 0,
                0, 0, 1
        );
    }

    public static Matrix3 zero()
    {
        return new Matrix3(
                0, 0, 0,
                0, 0, 0,
                0, 0, 0
        );
    }

    public static Matrix3 rotationX(float radians)
    {
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        return new Matrix3(
                1,    0,   0,
                0,  cos, sin,
                0, -sin, cos
        );
    }

    public static Matrix3 rotationY(float radians)
    {
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        return new Matrix3(
                cos, 0, -sin,
                0, 1,    0,
                sin, 0,  cos
        );
    }

    public static Matrix3 rotationZ(float radians)
    {
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        return new Matrix3(
                cos, sin, 0,
                -sin, cos, 0,
                0,   0, 1
        );
    }

    public static Matrix3 scale(float sx, float sy, float sz)
    {
        return new Matrix3(
                sx,  0,  0,
                0, sy,  0,
                0,  0, sz
        );
    }

    private Vector3 col1;
    private Vector3 col2;
    private Vector3 col3;

    public Matrix3(Vector3 col1, Vector3 col2, Vector3 col3)
    {
        this.col1 = new Vector3(col1.x(), col1.y(), col1.z());
        this.col2 = new Vector3(col2.x(), col2.y(), col2.z());
        this.col3 = new Vector3(col3.x(), col3.y(), col3.z());
    }

    public Matrix3(float m00, float m01, float m02,
                   float m10, float m11, float m12,
                   float m20, float m21, float m22)
    {
        this.col1 = new Vector3(m00, m01, m02);
        this.col2 = new Vector3(m10, m11, m12);
        this.col3 = new Vector3(m20, m21, m22);
    }

    public void setRotationX(float radians)
    {
        float cos    = (float) Math.cos(radians);
        float sin    = (float) Math.sin(radians);
        Vector3 scale = getScale();
        this.col2 = new Vector3(0.0F,  cos * scale.y(), sin * scale.y());
        this.col3 = new Vector3(0.0F, -sin * scale.z(), cos * scale.z());
    }

    public void setRotationY(float radians)
    {
        float cos     = (float) Math.cos(radians);
        float sin     = (float) Math.sin(radians);
        Vector3 scale = getScale();
        this.col1 = new Vector3( cos * scale.x(), 0.0F, -sin * scale.x());
        this.col3 = new Vector3( sin * scale.z(), 0.0F,  cos * scale.z());
    }

    public void setRotationZ(float radians)
    {
        float cos     = (float) Math.cos(radians);
        float sin     = (float) Math.sin(radians);
        Vector3 scale = getScale();
        this.col1 = new Vector3(cos * scale.x(),  sin * scale.x(), 0.0F);
        this.col2 = new Vector3(-sin * scale.y(), cos * scale.y(), 0.0F);
    }

    public void setScale(float sx, float sy, float sz)
    {
        float lenX = new Vector3(this.col1.x(), this.col1.y(), this.col1.z()).magnitude();
        float lenY = new Vector3(this.col2.x(), this.col2.y(), this.col2.z()).magnitude();
        float lenZ = new Vector3(this.col3.x(), this.col3.y(), this.col3.z()).magnitude();
        if (lenX != 0.0F) { this.col1 = new Vector3(this.col1.x() / lenX * sx, this.col1.y() / lenX * sx, this.col1.z() / lenX * sx); }
        if (lenY != 0.0F) { this.col2 = new Vector3(this.col2.x() / lenY * sy, this.col2.y() / lenY * sy, this.col2.z() / lenY * sy); }
        if (lenZ != 0.0F) { this.col3 = new Vector3(this.col3.x() / lenZ * sz, this.col3.y() / lenZ * sz, this.col3.z() / lenZ * sz); }
    }

    public Vector3 getScale()
    {
        return new Vector3(
                new Vector3(this.col1.x(), this.col1.y(), this.col1.z()).magnitude(),
                new Vector3(this.col2.x(), this.col2.y(), this.col2.z()).magnitude(),
                new Vector3(this.col3.x(), this.col3.y(), this.col3.z()).magnitude()
        );
    }

    public Vector3 getColumn(int index)
    {
        return switch (index)
        {
            case 0 -> new Vector3(this.col1.x(), this.col1.y(), this.col1.z());
            case 1 -> new Vector3(this.col2.x(), this.col2.y(), this.col2.z());
            case 2 -> new Vector3(this.col3.x(), this.col3.y(), this.col3.z());
            default -> throw new IndexOutOfBoundsException("Column index out of bounds: " + index);
        };
    }

    public void setColumn(int index, Vector3 column)
    {
        switch (index)
        {
            case 0 -> this.col1 = new Vector3(column.x(), column.y(), column.z());
            case 1 -> this.col2 = new Vector3(column.x(), column.y(), column.z());
            case 2 -> this.col3 = new Vector3(column.x(), column.y(), column.z());
            default -> throw new IndexOutOfBoundsException("Column index out of bounds: " + index);
        }
    }

    public Vector3 getRow(int index)
    {
        return switch (index)
        {
            case 0 -> new Vector3(this.col1.x(), this.col2.x(), this.col3.x());
            case 1 -> new Vector3(this.col1.y(), this.col2.y(), this.col3.y());
            case 2 -> new Vector3(this.col1.z(), this.col2.z(), this.col3.z());
            default -> throw new IndexOutOfBoundsException("Row index out of bounds: " + index);
        };
    }

    public float get(int row, int col)
    {
        return switch (col)
        {
            case 0 -> switch (row)
            {
                case 0 -> this.col1.x();
                case 1 -> this.col1.y();
                case 2 -> this.col1.z();
                default -> throw new IndexOutOfBoundsException("Index out of bounds: (" + row + ", " + col + ")");
            };
            case 1 -> switch (row)
            {
                case 0 -> this.col2.x();
                case 1 -> this.col2.y();
                case 2 -> this.col2.z();
                default -> throw new IndexOutOfBoundsException("Index out of bounds: (" + row + ", " + col + ")");
            };
            case 2 -> switch (row)
            {
                case 0 -> this.col3.x();
                case 1 -> this.col3.y();
                case 2 -> this.col3.z();
                default -> throw new IndexOutOfBoundsException("Index out of bounds: (" + row + ", " + col + ")");
            };
            default -> throw new IndexOutOfBoundsException("Index out of bounds: (" + row + ", " + col + ")");
        };
    }

    public void set(int row, int col, float value)
    {
        switch (col)
        {
            case 0 ->
            {
                if (row == 0)
                {
                    this.col1.setX(value);
                }
                else if (row == 1)
                {
                    this.col1.setY(value);
                }
                else if (row == 2)
                {
                    this.col1.setZ(value);
                }
                else
                {
                    throw new IndexOutOfBoundsException("Index out of bounds: (" + row + ", " + col + ")");
                }
            }
            case 1 ->
            {
                if (row == 0)
                {
                    this.col2.setX(value);
                }
                else if (row == 1)
                {
                    this.col2.setY(value);
                }
                else if (row == 2)
                {
                    this.col2.setZ(value);
                }
                else
                {
                    throw new IndexOutOfBoundsException("Index out of bounds: (" + row + ", " + col + ")");
                }
            }
            case 2 ->
            {
                if (row == 0)
                {
                    this.col3.setX(value);
                }
                else if (row == 1)
                {
                    this.col3.setY(value);
                }
                else if (row == 2)
                {
                    this.col3.setZ(value);
                }
                else
                {
                    throw new IndexOutOfBoundsException("Index out of bounds: (" + row + ", " + col + ")");
                }
            }
            default -> throw new IndexOutOfBoundsException("Index out of bounds: (" + row + ", " + col + ")");
        }
    }

    public Matrix3 add(Matrix3 other)
    {
        return new Matrix3(
                this.col1.add(other.col1),
                this.col2.add(other.col2),
                this.col3.add(other.col3)
        );
    }

    public Matrix3 subtract(Matrix3 other)
    {
        return new Matrix3(
                this.col1.subtract(other.col1),
                this.col2.subtract(other.col2),
                this.col3.subtract(other.col3)
        );
    }

    public Matrix3 multiply(float scalar)
    {
        return new Matrix3(
                this.col1.multiply(scalar),
                this.col2.multiply(scalar),
                this.col3.multiply(scalar)
        );
    }

    public Vector3 multiply(Vector3 v)
    {
        return new Vector3(
                this.col1.x() * v.x() + this.col2.x() * v.y() + this.col3.x() * v.z(),
                this.col1.y() * v.x() + this.col2.y() * v.y() + this.col3.y() * v.z(),
                this.col1.z() * v.x() + this.col2.z() * v.y() + this.col3.z() * v.z()
        );
    }

    public Matrix3 multiply(Matrix3 other)
    {
        return new Matrix3(
                this.col1.x() * other.col1.x() + this.col2.x() * other.col1.y() + this.col3.x() * other.col1.z(),
                this.col1.y() * other.col1.x() + this.col2.y() * other.col1.y() + this.col3.y() * other.col1.z(),
                this.col1.z() * other.col1.x() + this.col2.z() * other.col1.y() + this.col3.z() * other.col1.z(),
                this.col1.x() * other.col2.x() + this.col2.x() * other.col2.y() + this.col3.x() * other.col2.z(),
                this.col1.y() * other.col2.x() + this.col2.y() * other.col2.y() + this.col3.y() * other.col2.z(),
                this.col1.z() * other.col2.x() + this.col2.z() * other.col2.y() + this.col3.z() * other.col2.z(),
                this.col1.x() * other.col3.x() + this.col2.x() * other.col3.y() + this.col3.x() * other.col3.z(),
                this.col1.y() * other.col3.x() + this.col2.y() * other.col3.y() + this.col3.y() * other.col3.z(),
                this.col1.z() * other.col3.x() + this.col2.z() * other.col3.y() + this.col3.z() * other.col3.z()
        );
    }

    public Matrix3 transpose()
    {
        return new Matrix3(
                this.col1.x(), this.col2.x(), this.col3.x(),
                this.col1.y(), this.col2.y(), this.col3.y(),
                this.col1.z(), this.col2.z(), this.col3.z()
        );
    }

    public float determinant()
    {
        return this.col1.x() * (this.col2.y() * this.col3.z() - this.col3.y() * this.col2.z())
                - this.col2.x() * (this.col1.y() * this.col3.z() - this.col3.y() * this.col1.z())
                + this.col3.x() * (this.col1.y() * this.col2.z() - this.col2.y() * this.col1.z());
    }

    public Matrix3 inverse()
    {
        float det = this.determinant();
        if (det == 0.0F)
        {
            throw new ArithmeticException("Matrix3 is not invertible (determinant is zero)");
        }
        float invDet = 1.0F / det;

        return new Matrix3(
                (this.col2.y() * this.col3.z() - this.col3.y() * this.col2.z()) * invDet,
                -(this.col1.y() * this.col3.z() - this.col3.y() * this.col1.z()) * invDet,
                (this.col1.y() * this.col2.z() - this.col2.y() * this.col1.z()) * invDet,
                -(this.col2.x() * this.col3.z() - this.col3.x() * this.col2.z()) * invDet,
                (this.col1.x() * this.col3.z() - this.col3.x() * this.col1.z()) * invDet,
                -(this.col1.x() * this.col2.z() - this.col2.x() * this.col1.z()) * invDet,
                (this.col2.x() * this.col3.y() - this.col3.x() * this.col2.y()) * invDet,
                -(this.col1.x() * this.col3.y() - this.col3.x() * this.col1.y()) * invDet,
                (this.col1.x() * this.col2.y() - this.col2.x() * this.col1.y()) * invDet
        );
    }

    @Override
    public String toString()
    {
        return "[" + this.col1.x() + ", " + this.col2.x() + ", " + this.col3.x() + "]\n"
                + "[" + this.col1.y() + ", " + this.col2.y() + ", " + this.col3.y() + "]\n"
                + "[" + this.col1.z() + ", " + this.col2.z() + ", " + this.col3.z() + "]";
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

        Matrix3 m = (Matrix3) obj;
        return this.col1.equals(m.col1) && this.col2.equals(m.col2) && this.col3.equals(m.col3);
    }

    @Override
    public int hashCode()
    {
        int result = col1.hashCode();
        result = 31 * result + col2.hashCode();
        result = 31 * result + col3.hashCode();
        return result;
    }
}