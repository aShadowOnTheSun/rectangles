package com.iholden.support;

public class Range
{
    private final long minInclusive;
    private final long maxInclusive;

    public Range(long minInclusive, long maxInclusive)
    {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    public static Range of(long minInclusive, long maxInclusive)
    {
        return new Range(minInclusive, maxInclusive);
    }

    public boolean contains(long numberToCheck)
    {
        return numberToCheck >= minInclusive && numberToCheck <= maxInclusive;
    }

    public long getMinInclusive()
    {
        return minInclusive;
    }

    public long getMaxInclusive()
    {
        return maxInclusive;
    }
}
