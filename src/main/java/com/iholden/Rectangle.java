package com.iholden;

import com.iholden.support.Range;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Rectangle
{
    long getLength();
    long getWidth();

    Point getLowerLeft();
    Point getLowerRight();
    Point getTopLeft();
    Point getTopRight();

    List<Point> getCornerPoints();
    List<Line> getLines();
    Range getRangeX();
    Range getRangeY();

    default long getArea()
    {
        return getLength() * getWidth();
    }

    default boolean contains(Rectangle otherRectangle)
    {
        // Rule out obvious case of the other rectangle being larger than this one
        if (otherRectangle.getArea() >= this.getArea())
        {
            return false;
        }

        return otherRectangle.getCornerPoints().stream().allMatch(otherPoint ->
                otherPoint.getXCoordinate() > this.getLowerLeft().getXCoordinate() &&
                otherPoint.getXCoordinate() < this.getLowerRight().getXCoordinate() &&
                otherPoint.getYCoordinate() > this.getLowerLeft().getYCoordinate() &&
                otherPoint.getYCoordinate() < this.getTopRight().getYCoordinate());
    }

    default Set<Point> findIntersectionPointsWith(Rectangle otherRectangle)
    {
        // If other rectangle is contained entirely within this one, intersection is impossible
        if (this.contains(otherRectangle))
        {
            return Set.of();
        }

        Set<Point> intersectingPoints = internalFindIntersectionPoints(this, otherRectangle);
        intersectingPoints.addAll(internalFindIntersectionPoints(otherRectangle, this));

        return intersectingPoints;
    }

    private static Set<Point> internalFindIntersectionPoints(Rectangle rectangleA, Rectangle rectangleB)
    {
        Set<Long> overlappingXCoordinates = Stream.of(rectangleA.getRangeX().getMinInclusive(), rectangleA.getRangeX().getMaxInclusive())
                .filter(x -> rectangleB.getRangeX().contains(x))
                .collect(Collectors.toSet());

        Set<Long> overlappingYCoordinates = Stream.of(rectangleB.getRangeY().getMinInclusive(), rectangleB.getRangeY().getMaxInclusive())
                .filter(y -> rectangleA.getRangeY().contains(y))
                .collect(Collectors.toSet());

        var intersectingPoints = new HashSet<Point>();

        if (overlappingXCoordinates.isEmpty() || overlappingYCoordinates.isEmpty())
        {
            return intersectingPoints;
        }

        overlappingXCoordinates.forEach(x -> overlappingYCoordinates.stream()
                .map(y -> new PointImpl(x, y))
                .forEach(intersectingPoints::add));

        return intersectingPoints;
    }
}
