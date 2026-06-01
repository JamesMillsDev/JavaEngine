package net.skittles.maths;

public class Matrix2
{
    public static Matrix2 identity()
    {
        return new Matrix2(1, 0, 0, 1);
    }

    public static Matrix2 zero()
    {
        return new Matrix2(0, 0, 0, 0);
    }

    public static Matrix2 rotation(float radians)
    {
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        return new Matrix2(cos, sin, -sin, cos);
    }

    public static Matrix2 scale(float sx, float sy)
    {
        return new Matrix2(sx, 0, 0, sy);
    }

    private Vector2 col1;
    private Vector2 col2;

    public Matrix2(Vector2 col1, Vector2 col2)
    {
        this.col1 = new Vector2(col1.x(), col1.y());
        this.col2 = new Vector2(col2.x(), col2.y());
    }

    public Matrix2(float m00, float m01, float m10, float m11)
    {
        this.col1 = new Vector2(m00, m01);
        this.col2 = new Vector2(m10, m11);
    }

    public Vector2 getColumn(int index)
    {
        return switch (index)
        {
            case 0 -> new Vector2(this.col1.x(), this.col1.y());
            case 1 -> new Vector2(this.col2.x(), this.col2.y());
            default -> throw new IndexOutOfBoundsException("Column index out of bounds: " + index);
        };
    }

    public void setColumn(int index, Vector2 column)
    {
        switch (index)
        {
            case 0 -> this.col1 = new Vector2(column.x(), column.y());
            case 1 -> this.col2 = new Vector2(column.x(), column.y());
            default -> throw new IndexOutOfBoundsException("Column index out of bounds: " + index);
        }
    }

    public Vector2 getRow(int index)
    {
        return switch (index)
        {
            case 0 -> new Vector2(this.col1.x(), this.col2.x());
            case 1 -> new Vector2(this.col1.y(), this.col2.y());
            default -> throw new IndexOutOfBoundsException("Row index out of bounds: " + index);
        };
    }

    public float get(int row, int col)
    {
        return switch (col)
        {
            case 0 -> row == 0 ? this.col1.x() : this.col1.y();
            case 1 -> row == 0 ? this.col2.x() : this.col2.y();
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
                else
                {
                    throw new IndexOutOfBoundsException();
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
                else
                {
                    throw new IndexOutOfBoundsException();
                }
            }
            default -> throw new IndexOutOfBoundsException("Index out of bounds: (" + row + ", " + col + ")");
        }
    }

    public Matrix2 add(Matrix2 other)
    {
        return new Matrix2(
                this.col1.add(other.col1),
                this.col2.add(other.col2)
        );
    }

    public Matrix2 subtract(Matrix2 other)
    {
        return new Matrix2(
                this.col1.subtract(other.col1),
                this.col2.subtract(other.col2)
        );
    }

    public Matrix2 multiply(float scalar)
    {
        return new Matrix2(
                this.col1.multiply(scalar),
                this.col2.multiply(scalar)
        );
    }

    public Vector2 multiply(Vector2 v)
    {
        return new Vector2(
                this.col1.x() * v.x() + this.col2.x() * v.y(),
                this.col1.y() * v.x() + this.col2.y() * v.y()
        );
    }

    public Matrix2 multiply(Matrix2 other)
    {
        return new Matrix2(
                this.col1.x() * other.col1.x() + this.col2.x() * other.col1.y(),
                this.col1.y() * other.col1.x() + this.col2.y() * other.col1.y(),
                this.col1.x() * other.col2.x() + this.col2.x() * other.col2.y(),
                this.col1.y() * other.col2.x() + this.col2.y() * other.col2.y()
        );
    }

    public Matrix2 transpose()
    {
        return new Matrix2(
                this.col1.x(), this.col2.x(),
                this.col1.y(), this.col2.y()
        );
    }

    public float determinant()
    {
        return this.col1.x() * this.col2.y() - this.col2.x() * this.col1.y();
    }

    public Matrix2 inverse()
    {
        float det = this.determinant();
        if (det == 0.0F)
        {
            throw new ArithmeticException("Matrix2 is not invertible (determinant is zero)");
        }
        float invDet = 1.0F / det;
        return new Matrix2(
                this.col2.y() * invDet,
                -this.col1.y() * invDet,
                -this.col2.x() * invDet,
                this.col1.x() * invDet
        );
    }

    @Override
    public String toString()
    {
        return "[" + this.col1.x() + ", " + this.col2.x() + "]\n"
                + "[" + this.col1.y() + ", " + this.col2.y() + "]";
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

        Matrix2 m = (Matrix2) obj;
        return this.col1.equals(m.col1) && this.col2.equals(m.col2);
    }

    @Override
    public int hashCode()
    {
        int result = col1.hashCode();
        result = 31 * result + col2.hashCode();
        return result;
    }
}