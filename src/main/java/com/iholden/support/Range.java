package com.iholden.support;

import com.iholden.Line;

import java.util.HashSet;
import java.util.Optional;

public class Range
{
    private final long min;
    private final long max;

    // Prevent external instantiation; prefer static access
    private Range(long min, long max)
    {
        this.min = min;
        this.max = max;
    }

    public static Range of(long min, long max)
    {
        return new Range(min, max);
    }

    public static Range of(Line line)
    {
        return switch (line.getOrientation())
        {
            case VERTICAL -> Range.of(line.getPointA().getYCoordinate(), line.getPointB().getYCoordinate());
            case HORIZONTAL -> Range.of(line.getPointA().getXCoordinate(), line.getPointB().getXCoordinate());
        };
    }

    public boolean containsInclusive(long numberToCheck)
    {
        return numberToCheck >= min && numberToCheck <= max;
    }

    public boolean containsExclusive(long numberToCheck)
    {
        return numberToCheck > min && numberToCheck < max;
    }

    // TODO - document EXCLUSIVE here
    public boolean containsAllExclusive(Range otherRange)
    {
        return otherRange.getMin() > min && otherRange.getMax() < max;
    }

    public Optional<Range> getRangeOfOverlappingValues(Range otherRange)
    {
        var overlappingValues = new HashSet<Long>();

        for (long i = otherRange.getMin(); i <= otherRange.getMax(); i++)
        {
            if (this.containsInclusive(i))
            {
                overlappingValues.add(i);
            }
        }

        return overlappingValues.stream()
                .min(Long::compare)
                .map(minValue -> Range.of(minValue, overlappingValues.size() == 1 ? minValue : minValue + overlappingValues.size()));
    }

    public long getDistance()
    {
        return max - min;
    }

    public long getMin()
    {
        return min;
    }

    public long getMax()
    {
        return max;
    }
}
