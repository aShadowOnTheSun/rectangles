package com.iholden;

import java.util.List;

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
}
