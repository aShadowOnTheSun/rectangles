package com.iholden;

import static java.util.stream.Collectors.toList;

import com.iholden.constants.Adjacency;
import com.iholden.constants.Orientation;
import com.iholden.support.Range;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * TODO - convert to Javadoc
 *
 * Implementations MUST provide proper equals and hashCode implementations
 */
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
    List<Line> getHorizontalLines();
    List<Line> getVerticalLines();

    default long getArea()
    {
        return getLength() * getWidth();
    }

    default List<Line> getLinesHavingOrientationOf(Orientation orientation)
    {
        return getLines().stream()
                .filter(line -> orientation.equals(line.getOrientation()))
                .collect(toList());
    }

    default boolean isCornerAdjacentWith(Rectangle otherRectangle)
    {
        return this.getLowerLeft().equals(otherRectangle.getTopRight())
                || this.getLowerRight().equals(otherRectangle.getTopLeft())
                || this.getTopRight().equals(otherRectangle.getLowerLeft())
                || this.getTopLeft().equals(otherRectangle.getLowerRight());
    }

    default Adjacency determineAdjacencyWith(Rectangle otherRectangle)
    {
        if (this.contains(otherRectangle) || !this.findIntersectionPointsWith(otherRectangle).isEmpty())
        {
            return Adjacency.NONE;
        }

        if (this.isCornerAdjacentWith(otherRectangle))
        {
            return Adjacency.SINGLE_POINT;
        }

        Adjacency adjacency = Adjacency.NONE;

        for (Line thisLine : this.getHorizontalLines())
        {
            for (Line otherLine : otherRectangle.getHorizontalLines())
            {
                adjacency = thisLine.determineAdjacencyWith(otherLine);

                if (!adjacency.equals(Adjacency.NONE))
                {
                    break;
                }
            }

            if (!adjacency.equals(Adjacency.NONE))
            {
                break;
            }
        }

        if (!adjacency.equals(Adjacency.NONE))
        {
            return adjacency;
        }

        for (Line thisLine : this.getVerticalLines())
        {
            for (Line otherLine : otherRectangle.getVerticalLines())
            {
                adjacency = thisLine.determineAdjacencyWith(otherLine);

                if (!adjacency.equals(Adjacency.NONE))
                {
                    break;
                }
            }

            if (!adjacency.equals(Adjacency.NONE))
            {
                break;
            }
        }

        return adjacency;
    }

    default boolean contains(Rectangle otherRectangle)
    {
        // The rectangles are identical - no containment
        if (this.equals(otherRectangle))
        {
            return false;
        }

        // Rule out obvious case of the other rectangle being larger than this one
        if (otherRectangle.getArea() >= this.getArea())
        {
            return false;
        }

        return otherRectangle.getCornerPoints().stream().allMatch(otherPoint ->
                otherPoint.getXCoordinate() >= this.getLowerLeft().getXCoordinate() &&
                otherPoint.getXCoordinate() <= this.getLowerRight().getXCoordinate() &&
                otherPoint.getYCoordinate() >= this.getLowerLeft().getYCoordinate() &&
                otherPoint.getYCoordinate() <= this.getTopRight().getYCoordinate());
    }

    default Set<Point> findIntersectionPointsWith(Rectangle otherRectangle)
    {
        // The rectangles are identical - no intersection
        if (this.equals(otherRectangle))
        {
            return Set.of();
        }

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
        Set<Long> overlappingXCoordinates = Stream.of(rectangleA.getRangeX().getMin(), rectangleA.getRangeX().getMax())
                .filter(x -> rectangleB.getRangeX().containsExclusive(x))
                .collect(Collectors.toSet());

        Set<Long> overlappingYCoordinates = Stream.of(rectangleB.getRangeY().getMin(), rectangleB.getRangeY().getMax())
                .filter(y -> rectangleA.getRangeY().containsExclusive(y))
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
