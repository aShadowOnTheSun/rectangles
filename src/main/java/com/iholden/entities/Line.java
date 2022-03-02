package com.iholden.entities;

import static java.util.stream.Collectors.toSet;

import com.iholden.constants.Adjacency;
import com.iholden.constants.Orientation;
import com.iholden.exceptions.DiagonalLineException;
import com.iholden.support.Range;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * <p>Interface representing a Line on a 2-dimensional plane, consisting of 2 points.</p>
 * <p>Provides default implementations of comparison between lines.</p>
 * <p><b>All implementations are REQUIRED to provide valid {@link #equals(Object)} and {@link #hashCode()} implementations.</b></p>
 * @see com.iholden.entities.impl.LineImpl
 */
public interface Line
{
    /**
     * Calculates the length of the line, e.g. the distance between its two points
     * @return the length of the line
     */
    default Long getLength()
    {
        return switch (getOrientation())
        {
            case VERTICAL -> getPointB().getYCoordinate() - getPointA().getYCoordinate();
            case HORIZONTAL -> getPointB().getXCoordinate() - getPointA().getXCoordinate();
        };
    }

    /**
     * Gets all the distinct X coordinates that this line's points contain
     * @return an <b>immutable</b> Set of all the distinct X coordinates that this line's points contain
     */
    default Set<Long> getDistinctXCoordinates()
    {
        return getPoints().stream()
                .map(Point::getXCoordinate)
                .collect(toSet());
    }

    /**
     * Gets all the distinct Y coordinates that this line's points contain
     * @return an <b>immutable</b> Set of all the distinct Y coordinates that this line's points contain
     */
    default Set<Long> getDistinctYCoordinates()
    {
        return getPoints().stream()
                .map(Point::getYCoordinate)
                .collect(toSet());
    }

    /**
     * <p>Determines whether this line is adjacent with {@code otherLine}</p>
     * <p>This method considers two lines to be adjacent when they are of
     *    the same orientation, and have one or more overlapping coordinates
     *    on the same axis.</p>
     * <p>Callers are expected to have checked the parent quadrilateral for
     *    adjacency types that are dependent upon broader context
     *    (such as {@link Adjacency#SINGLE_POINT}) prior to calling this method.</p>
     * <p>See README for additional details on Adjacency classifications.</p>
     * @param otherLine other line to compare to
     * @return the Adjacency classification of the Lines
     * @see Adjacency
     */
    default Adjacency determineAdjacencyWith(Line otherLine)
    {
        // Identical lines always indicate PROPER adjacency
        if (this.equals(otherLine))
        {
            return Adjacency.PROPER;
        }

        // Lines of different orientations cannot be adjacent
        if (!this.getOrientation().equals(otherLine.getOrientation()))
        {
            return Adjacency.NONE;
        }

        return switch (this.getOrientation())
        {
            case VERTICAL -> determineVerticalAdjacencyWith(otherLine);
            case HORIZONTAL -> determineHorizontalAdjacencyWith(otherLine);
        };
    }

    default Adjacency determineVerticalAdjacencyWith(Line otherLine)
    {
        // Vertical lines must overlap on the X-axis to be adjacent
        if (this.getDistinctXCoordinates().stream().noneMatch(thisX -> otherLine.getDistinctXCoordinates().contains(thisX)))
        {
            return Adjacency.NONE;
        }

        return internalDetermineAdjacency(this, otherLine);
    }

    default Adjacency determineHorizontalAdjacencyWith(Line otherLine)
    {
        // Horizontal lines must overlap on the Y-axis to be adjacent
        if (this.getDistinctYCoordinates().stream().noneMatch(thisY -> otherLine.getDistinctYCoordinates().contains(thisY)))
        {
            return Adjacency.NONE;
        }

        return internalDetermineAdjacency(this, otherLine);
    }

    default Orientation getOrientation()
    {
        if (getPointA().getXCoordinate() == getPointB().getXCoordinate())
        {
            return Orientation.VERTICAL;
        }
        else if (getPointA().getYCoordinate() == getPointB().getYCoordinate())
        {
            return Orientation.HORIZONTAL;
        }

        throw new DiagonalLineException(this);
    }

    private static Adjacency internalDetermineAdjacency(Line lineA, Line lineB)
    {
        Range thisCoordinateRange = Range.of(lineA);
        Optional<Range> overlappingCoordinateRangeOptional = thisCoordinateRange.getRangeOfOverlappingValues(Range.of(lineB));

        // Empty Optional here indicates that there no overlapping Y coordinates
        if (overlappingCoordinateRangeOptional.isEmpty())
        {
            return Adjacency.NONE;
        }

        Range overlappingCoordinateRange = overlappingCoordinateRangeOptional.get();

        // Since we've already checked for SINGLE_POINT matches, this is indicative of lines that touch, but are not adjacent
        if (overlappingCoordinateRange.getDistance() == 0)
        {
            return Adjacency.NONE;
        }

        /*
         * Since we KNOW at this point that there is adjacency, and we've already ruled out PROPER and SINGLE_POINT,
         * we can reasonably assume that if the other line is longer than this one, or does not contain the entire
         * range of overlapping coordinates, that it is PARTIAL
         */
        if (lineB.getLength() < lineA.getLength() && thisCoordinateRange.containsAllExclusive(overlappingCoordinateRange))
        {
            return Adjacency.SUB_LINE;
        }

        return Adjacency.PARTIAL;
    }

    Point getPointA();
    Point getPointB();
    List<Point> getPoints();
}
