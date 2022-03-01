package com.iholden.exceptions;

import com.iholden.Line;

public class DiagonalLineException extends RuntimeException
{
    static final String DETAIL_MESSAGE_TEMPLATE = "Expected vertical/horizontal line; got diagonal line: %s";

    private final Line line;

    public DiagonalLineException(Line line)
    {
        super(buildDetailMessage(line));
        this.line = line;
    }

    private static String buildDetailMessage(Line line)
    {
        return DETAIL_MESSAGE_TEMPLATE.formatted(line);
    }

    public Line getLine()
    {
        return line;
    }
}
