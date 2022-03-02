package com.iholden.entities;

public interface Point
{
    long getXCoordinate();
    long getYCoordinate();

    default String prettyPrint()
    {
        return "(%s, %s)".formatted(getXCoordinate(), getYCoordinate());
    }
}
