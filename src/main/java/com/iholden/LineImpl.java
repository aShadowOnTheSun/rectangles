package com.iholden;

import java.util.List;
import java.util.Objects;

public class LineImpl implements Line
{
    private final Point pointA;
    private final Point pointB;

    public LineImpl(Point pointA, Point pointB)
    {
        this.pointA = pointA;
        this.pointB = pointB;
    }

    @Override
    public Point getPointA()
    {
        return pointA;
    }

    @Override
    public Point getPointB()
    {
        return pointB;
    }

    @Override
    public List<Point> getPoints()
    {
        return List.of(pointA, pointB);
    }

    @Override
    public String toString()
    {
        return "LineImpl{" +
                "pointA=" + pointA +
                ", pointB=" + pointB +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        LineImpl line = (LineImpl) o;

        return pointA.equals(line.pointA) && pointB.equals(line.pointB);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(pointA, pointB);
    }
}
