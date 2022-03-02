package com.iholden.support;

import com.iholden.entities.Line;

import java.util.HashSet;
import java.util.Optional;

/**
 * <p>Represents a range of numbers between two provided numeric values.</p>
 * <p>Offers utilities for seeing if a number falls within the range, seeing
 *    if one range falls entirely within the other, determining where two
 *    ranges overlap, etc.</p>
 * <p>Use static builders to obtain an instance: {@link Range#of(long, long)} or {@link Range#of(Line)}</p>
 * <p>Exclusive vs. Inclusive operations are both supported via the corresponding methods, e.g.
 *    {@link Range#containsInclusive(long)} for inclusive matching and
 *    {@link Range#containsExclusive(long)} for exclusive matching.</p>
 */
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

    /**
     * Determines whether ALL the values in otherRange fall within this Range, exclusively.
     * @param otherRange another range to compare to this one
     * @return true if all values in the otherRange are contained within this range; otherwise false
     */
    public boolean containsAllExclusive(Range otherRange)
    {
        return otherRange.getMin() > min && otherRange.getMax() < max;
    }

    /**
     * Determines what values--if any--are shared between the two Ranges, and returns the shared values as a Range
     * @param otherRange another range to compare to this one
     * @return an Optional containing a Range of values where these Ranges overlap, or {@code Optional.empty()}
     *         if there were no overlapping values
     */
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

    /**
     * Returns the "distance" covered by the range, e.g. {@code getMax() - getMin()}
     * @return distance covered by the range
     */
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
