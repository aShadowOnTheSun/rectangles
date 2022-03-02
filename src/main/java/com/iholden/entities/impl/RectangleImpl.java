package com.iholden.entities.impl;

import com.iholden.constants.Orientation;
import com.iholden.entities.Line;
import com.iholden.entities.Point;
import com.iholden.entities.Rectangle;
import com.iholden.support.Range;

import java.util.List;
import java.util.Objects;

public class RectangleImpl implements Rectangle
{
    private final long length;
    private final long width;
    private final Point lowerLeft;
    private final Point lowerRight;
    private final Point topLeft;
    private final Point topRight;
    private final Line leftSide;
    private final Line rightSide;
    private final Line topSide;
    private final Line bottomSide;

    private final List<Point> points;
    private final List<Line> lines;
    private final Range rangeX;
    private final Range rangeY;

    public RectangleImpl(long length, long width, Point lowerLeft)
    {
        this.length = length;
        this.width = width;
        this.lowerLeft = lowerLeft;
        this.lowerRight = new PointImpl(lowerLeft.getXCoordinate() + length, lowerLeft.getYCoordinate());
        this.topLeft = new PointImpl(lowerLeft.getXCoordinate(), lowerLeft.getYCoordinate() + width);
        this.topRight = new PointImpl(lowerRight.getXCoordinate(), topLeft.getYCoordinate());
        this.leftSide = new LineImpl(lowerLeft, topLeft);
        this.rightSide = new LineImpl(lowerRight, topRight);
        this.topSide = new LineImpl(topLeft, topRight);
        this.bottomSide = new LineImpl(lowerLeft, lowerRight);

        this.points = List.of(lowerLeft, lowerRight, topLeft, topRight);
        this.lines = List.of(leftSide, rightSide, topSide, bottomSide);
        this.rangeX = Range.of(lowerLeft.getXCoordinate(), lowerRight.getXCoordinate());
        this.rangeY = Range.of(lowerLeft.getYCoordinate(), topLeft.getYCoordinate());
    }

    @Override
    public long getLength()
    {
        return length;
    }

    @Override
    public long getWidth()
    {
        return width;
    }

    @Override
    public Point getLowerLeft()
    {
        return lowerLeft;
    }

    @Override
    public Point getLowerRight()
    {
        return lowerRight;
    }

    @Override
    public Point getTopLeft()
    {
        return topLeft;
    }

    @Override
    public Point getTopRight()
    {
        return topRight;
    }

    @Override
    public List<Point> getCornerPoints()
    {
        return points;
    }

    @Override
    public List<Line> getLines()
    {
        return lines;
    }

    @Override
    public Range getRangeX()
    {
        return rangeX;
    }

    @Override
    public Range getRangeY()
    {
        return rangeY;
    }

    @Override
    public List<Line> getHorizontalLines()
    {
        return getLinesHavingOrientationOf(Orientation.HORIZONTAL);
    }

    @Override
    public List<Line> getVerticalLines()
    {
        return getLinesHavingOrientationOf(Orientation.VERTICAL);
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

        RectangleImpl rectangle = (RectangleImpl) o;

        return length == rectangle.length && width == rectangle.width && lowerLeft.equals(rectangle.lowerLeft);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(length, width, lowerLeft);
    }
}
