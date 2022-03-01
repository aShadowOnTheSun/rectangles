package com.iholden;

import com.iholden.exceptions.DiagonalLineException;

import java.util.List;

public interface Line
{
    Point getPointA();
    Point getPointB();
    List<Point> getPoints();

    default Orientation getOrientation()
    {
        if (getPointA().getXCoordinate() == getPointB().getXCoordinate())
        {
            return Orientation.HORIZONTAL;
        }
        else if (getPointA().getYCoordinate() == getPointB().getYCoordinate())
        {
            return Orientation.VERTICAL;
        }

        throw new DiagonalLineException(this);
    }
}
