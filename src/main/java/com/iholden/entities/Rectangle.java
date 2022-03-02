package com.iholden.entities;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.iholden.constants.Adjacency;
import com.iholden.constants.Orientation;
import com.iholden.entities.impl.PointImpl;
import com.iholden.support.Range;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * <p>Interface representing a Rectangle on a 2-dimensional plane.</p>
 * <p>Provides default implementations of several types of comparison between {@link Rectangle}s,
 *    including containment, intersection, and adjacency.</p>
 * <p><b>All implementations are REQUIRED to provide valid {@link #equals(Object)} and {@link #hashCode()} implementations.</b></p>
 * @see com.iholden.entities.impl.RectangleImpl
 */
public interface Rectangle
{
    /**
     * Convenience method for retrieving the area of the Rectangle. Equivalent to {@code getLength() * getWidth()}
     * @return the area of the Rectangle
     */
    default long getArea()
    {
        return getLength() * getWidth();
    }

    /**
     * <p>Determines whether this rectangle is adjacent with {@code otherRectangle}.</p>
     * <p>This method considers two Rectangles to be adjacent when they are not equal,
     *    do not intersect at any point, do not contain one another, and have lines
     *    that occupy one or more of the same points on the same axis.</p>
     * <p>See README for additional classification details/definitions.</p>
     * @param otherRectangle other Rectangle to compare with this one
     * @return the Adjacency classification of the rectangles
     * @see Adjacency
     */
    default Adjacency determineAdjacencyWith(Rectangle otherRectangle)
    {
        if (this.equals(otherRectangle) || this.contains(otherRectangle) || !this.findIntersectionPointsWith(otherRectangle).isEmpty())
        {
            return Adjacency.NONE;
        }

        if (areRectanglesCornerAdjacent(this, otherRectangle))
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

    /**
     * <p>Determines if this Rectangle contains {@code otherRectangle}</p>
     * <p>This method considers a rectangle as being contained within another
     *    rectangle when all if its corner points exist entirely within the confines
     *    of the other rectangle.</p>
     * @param otherRectangle other Rectangle to compare with this one
     * @return true if this Rectangle contains {@code otherRectangle}; otherwise false
     */
    default boolean contains(Rectangle otherRectangle)
    {
        // The rectangles are identical - no containment
        if (this.equals(otherRectangle))
        {
            return false;
        }

        // Rule out obvious case of the other rectangle being larger than this one
        if (otherRectangle.getArea() > this.getArea())
        {
            return false;
        }

        return otherRectangle.getCornerPoints().stream().allMatch(otherPoint ->
                otherPoint.getXCoordinate() >= this.getLowerLeft().getXCoordinate() &&
                otherPoint.getXCoordinate() <= this.getLowerRight().getXCoordinate() &&
                otherPoint.getYCoordinate() >= this.getLowerLeft().getYCoordinate() &&
                otherPoint.getYCoordinate() <= this.getTopRight().getYCoordinate());
    }

    /**
     * <p>Finds and returns any Points at which the Rectangles intersect.</p>
     * <p>The returned Set is <b>immutable</b></p>
     * @param otherRectangle other rectangle to compare with this one
     * @return a Set of all Points where this Rectangle intersects with {@code otherRectangle}, or an empty set if no intersections
     */
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

    /**
     * Convenience method for querying the rectangle lines matching the provided Orientation
     * @param orientation line orientation type to query
     * @return a list of the Rectangle's lines that match the provided orientation type
     */
    default List<Line> getLinesHavingOrientationOf(Orientation orientation)
    {
        return getLines().stream()
                .filter(line -> orientation.equals(line.getOrientation()))
                .collect(toList());
    }

    /**
     * <p>Finds any intersecting points between the provided rectangles.</p>
     * <p>In order to locate all intersecting points, the method must be called twice, reversing the order of the
     *    provided Rectangle arguments for the second call.</p>
     * <p>The returned Set is mutable.</p>
     * @param rectangleA first Rectangle to compare
     * @param rectangleB second Rectangle to compare
     * @return a Set of Points at which the two rectangles intersect, or an empty Set if there are no intersections
     */
    private static Set<Point> internalFindIntersectionPoints(Rectangle rectangleA, Rectangle rectangleB)
    {
        var intersectingPoints = new HashSet<Point>();

        Set<Long> overlappingXCoordinates = Stream.of(rectangleA.getRangeX().getMin(), rectangleA.getRangeX().getMax())
                .filter(x -> rectangleB.getRangeX().containsExclusive(x))
                .collect(toSet());

        Set<Long> overlappingYCoordinates = Stream.of(rectangleB.getRangeY().getMin(), rectangleB.getRangeY().getMax())
                .filter(y -> rectangleA.getRangeY().containsExclusive(y))
                .collect(toSet());

        if (overlappingXCoordinates.isEmpty() || overlappingYCoordinates.isEmpty())
        {
            return intersectingPoints;
        }

        overlappingXCoordinates.forEach(x -> overlappingYCoordinates.stream()
                .map(y -> new PointImpl(x, y))
                .forEach(intersectingPoints::add));

        return intersectingPoints;
    }

    /**
     * <p>Determines if the provided rectangles are adjacent at a single, corner point, e.g. {@link Adjacency#SINGLE_POINT}</p>
     * @param rectangleA first Rectangle to compare
     * @param rectangleB second Rectangle to compare
     * @return true if the rectangles are corner adjacent
     */
    private static boolean areRectanglesCornerAdjacent(Rectangle rectangleA, Rectangle rectangleB)
    {
        return rectangleA.getLowerLeft().equals(rectangleB.getTopRight())
                || rectangleA.getLowerRight().equals(rectangleB.getTopLeft())
                || rectangleA.getTopRight().equals(rectangleB.getLowerLeft())
                || rectangleA.getTopLeft().equals(rectangleB.getLowerRight());
    }

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
}
