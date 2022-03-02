package com.iholden.entities.impl;

import com.iholden.entities.Point;

import java.util.Objects;

public class PointImpl implements Point
{
    private final long x;
    private final long y;

    public PointImpl(long x, long y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public long getXCoordinate()
    {
        return x;
    }

    @Override
    public long getYCoordinate()
    {
        return y;
    }

    @Override
    public String toString()
    {
        return "PointImpl{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }

        if (other == null || getClass() != other.getClass())
        {
            return false;
        }

        PointImpl point = (PointImpl) other;

        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }
}
