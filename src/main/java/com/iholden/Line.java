package com.iholden;

import static java.util.stream.Collectors.toSet;

import com.iholden.constants.Adjacency;
import com.iholden.constants.Orientation;
import com.iholden.exceptions.DiagonalLineException;
import com.iholden.support.Range;

import java.util.List;
import java.util.Optional;
import java.util.Set;

// TODO - add javadoc
// REQUIRES equals() and hashCode() implementations in subclasses
public interface Line
{
    Point getPointA();
    Point getPointB();
    List<Point> getPoints();

    default Long getLength()
    {
        return switch (getOrientation())
        {
            case VERTICAL -> getPointB().getYCoordinate() - getPointA().getYCoordinate();
            case HORIZONTAL -> getPointB().getXCoordinate() - getPointA().getXCoordinate();
        };
    }

    default Set<Long> getDistinctXCoordinates()
    {
        return getPoints().stream()
                .map(Point::getXCoordinate)
                .collect(toSet());
    }

    default Set<Long> getDistinctYCoordinates()
    {
        return getPoints().stream()
                .map(Point::getYCoordinate)
                .collect(toSet());
    }

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

        Range thisYCoordinateRange = Range.of(this);
        Optional<Range> overlappingYCoordinateRangeOptional = thisYCoordinateRange.getRangeOfOverlappingValues(Range.of(otherLine));

        // Empty Optional here indicates that there no overlapping Y coordinates
        if (overlappingYCoordinateRangeOptional.isEmpty())
        {
            return Adjacency.NONE;
        }

        Range overlappingYCoordinateRange = overlappingYCoordinateRangeOptional.get();

        // Since we've already checked for SINGLE_POINT matches, this is indicative of lines that touch, but are not adjacent
        if (overlappingYCoordinateRange.getDistance() == 0)
        {
            return Adjacency.NONE;
        }

        /*
         * Since we KNOW at this point that there is adjacency, and we've already ruled out PROPER and SINGLE_POINT,
         * we can reasonably assume that if the other line is longer than this one, or does not contain the entire
         * range of overlapping coordinates, that it is PARTIAL
         */
        if (otherLine.getLength() < this.getLength() && thisYCoordinateRange.containsAllExclusive(overlappingYCoordinateRange))
        {
            return Adjacency.SUB_LINE;
        }

        return Adjacency.PARTIAL;
    }

    default Adjacency determineHorizontalAdjacencyWith(Line otherLine)
    {
        // Horizontal lines must overlap on the Y-axis to be adjacent
        if (this.getDistinctYCoordinates().stream().noneMatch(thisY -> otherLine.getDistinctYCoordinates().contains(thisY)))
        {
            return Adjacency.NONE;
        }

        Range thisXCoordinateRange = Range.of(this);
        Optional<Range> overlappingXCoordinateRangeOptional = thisXCoordinateRange.getRangeOfOverlappingValues(Range.of(otherLine));

        // Empty Optional here indicates that there no overlapping Y coordinates
        if (overlappingXCoordinateRangeOptional.isEmpty())
        {
            return Adjacency.NONE;
        }

        Range overlappingXCoordinateRange = overlappingXCoordinateRangeOptional.get();

        // Since we've already checked for SINGLE_POINT matches, this is indicative of lines that touch, but are not adjacent
        if (overlappingXCoordinateRange.getDistance() == 0)
        {
            return Adjacency.NONE;
        }

        /*
         * Since we KNOW at this point that there is adjacency, and we've already ruled out PROPER and SINGLE_POINT,
         * we can reasonably assume that if the other line is longer than this one, or does not contain the entire
         * range of overlapping coordinates, that it is PARTIAL
         */
        if (otherLine.getLength() < this.getLength() && thisXCoordinateRange.containsAllExclusive(overlappingXCoordinateRange))
        {
            return Adjacency.SUB_LINE;
        }

        return Adjacency.PARTIAL;
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
}
