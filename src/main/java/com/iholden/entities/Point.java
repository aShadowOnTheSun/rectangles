package com.iholden.entities;

/**
 * <p>Interface representing a point on a 2-dimensional plane, characterized by an X and Y coordinate.</p>
 * <p><b>All implementations are REQUIRED to provide valid {@link #equals(Object)} and {@link #hashCode()} implementations.</b></p>
 * @see com.iholden.entities.impl.PointImpl
 */
public interface Point
{
    long getXCoordinate();
    long getYCoordinate();

    default String prettyPrint()
    {
        return "(%s, %s)".formatted(getXCoordinate(), getYCoordinate());
    }
}
