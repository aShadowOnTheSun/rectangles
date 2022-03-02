package com.iholden;

import com.iholden.entities.Rectangle;
import com.iholden.entities.impl.PointImpl;
import com.iholden.entities.impl.RectangleImpl;
import com.iholden.support.ConsoleTextUtils;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Scanner;

public class RectangleApplication
{
    static Scanner scanner;
    static PrintStream out;

    public static void main(String[] args)
    {
        scanner = new Scanner(System.in).useDelimiter(System.lineSeparator());
        out = System.out;

        out.println(ConsoleTextUtils.INITIAL_MESSAGE);

        boolean continueRunning;

        do
        {
            continueRunning = executeMainLoop();
        }
        while (continueRunning);
    }

    private static boolean executeMainLoop()
    {
        var rectanglesByLabel = new HashMap<String, Rectangle>(2);
        long length, width, lowerLeftX, lowerLeftY;

        for (String label : ConsoleTextUtils.RECTANGLE_LABELS)
        {
            length = getRectangleLength(label);
            width = getRectangleWidth(label);
            lowerLeftX = getRectangleLowerLeftX(label);
            lowerLeftY = getRectangleLowerLeftY(label);

            rectanglesByLabel.put(label, new RectangleImpl(length, width, new PointImpl(lowerLeftX, lowerLeftY)));
        }

        out.println(ConsoleTextUtils.getRectangleComparisonResults(rectanglesByLabel));

        return getShouldAppRunAgain();
    }

    private static long getRectangleLength(String rectangleLabel)
    {
        out.printf(ConsoleTextUtils.RECTANGLE_INPUT_LENGTH_PROMPT_TEMPLATE, rectangleLabel);
        OptionalLong lengthOptional = tryParseLengthOrWidth(scanner.next());

        while (lengthOptional.isEmpty())
        {
            out.println(ConsoleTextUtils.RECTANGLE_LENGTH_WIDTH_INPUT_INVALID);
            out.printf(ConsoleTextUtils.RECTANGLE_INPUT_LENGTH_PROMPT_TEMPLATE, rectangleLabel);
            lengthOptional = tryParseLengthOrWidth(scanner.next());
        }

        return lengthOptional.getAsLong();
    }

    private static long getRectangleWidth(String rectangleLabel)
    {
        out.printf(ConsoleTextUtils.RECTANGLE_INPUT_WIDTH_PROMPT_TEMPLATE, rectangleLabel);
        OptionalLong widthOptional = tryParseLengthOrWidth(scanner.next());

        while (widthOptional.isEmpty())
        {
            out.println(ConsoleTextUtils.RECTANGLE_LENGTH_WIDTH_INPUT_INVALID);
            out.printf(ConsoleTextUtils.RECTANGLE_INPUT_WIDTH_PROMPT_TEMPLATE, rectangleLabel);
            widthOptional = tryParseLengthOrWidth(scanner.next());
        }

        return widthOptional.getAsLong();
    }

    private static long getRectangleLowerLeftX(String rectangleLabel)
    {
        out.printf(ConsoleTextUtils.RECTANGLE_INPUT_BOTTOM_LEFT_X_COORDINATE_TEMPLATE, rectangleLabel);
        OptionalLong lowerLeftXOptional = tryParseCoordinate(scanner.next());

        while (lowerLeftXOptional.isEmpty())
        {
            out.println(ConsoleTextUtils.RECTANGLE_COORDINATE_INPUT_INVALID);
            out.printf(ConsoleTextUtils.RECTANGLE_INPUT_BOTTOM_LEFT_X_COORDINATE_TEMPLATE, rectangleLabel);
            lowerLeftXOptional = tryParseLengthOrWidth(scanner.next());
        }

        return lowerLeftXOptional.getAsLong();
    }

    private static long getRectangleLowerLeftY(String rectangleLabel)
    {
        out.printf(ConsoleTextUtils.RECTANGLE_INPUT_BOTTOM_LEFT_Y_COORDINATE_TEMPLATE, rectangleLabel);
        OptionalLong lowerLeftYOptional = tryParseCoordinate(scanner.next());

        while (lowerLeftYOptional.isEmpty())
        {
            out.println(ConsoleTextUtils.RECTANGLE_COORDINATE_INPUT_INVALID);
            out.printf(ConsoleTextUtils.RECTANGLE_INPUT_BOTTOM_LEFT_Y_COORDINATE_TEMPLATE, rectangleLabel);
            lowerLeftYOptional = tryParseLengthOrWidth(scanner.next());
        }

        return lowerLeftYOptional.getAsLong();
    }

    private static boolean getShouldAppRunAgain()
    {
        out.println(ConsoleTextUtils.CHECK_ADDITIONAL_RECTANGLES_PROMPT);
        Optional<Boolean> runAgainOptional = tryParseYesNoInput(scanner.next());

        while (runAgainOptional.isEmpty())
        {
            out.println(ConsoleTextUtils.YES_NO_QUESTION_INPUT_INVALID);
            out.println(ConsoleTextUtils.CHECK_ADDITIONAL_RECTANGLES_PROMPT);
            runAgainOptional = tryParseYesNoInput(scanner.next());
        }

        return runAgainOptional.get();
    }

    private static OptionalLong tryParseCoordinate(String input)
    {
        return tryParseNumericConsoleInput(input, true);
    }

    private static OptionalLong tryParseLengthOrWidth(String input)
    {
        return tryParseNumericConsoleInput(input, false);
    }

    private static OptionalLong tryParseNumericConsoleInput(String lengthOrWidth, boolean allowNegative)
    {
        try
        {
            if (allowNegative)
            {
                return OptionalLong.of(Long.parseLong(lengthOrWidth));
            }

            return OptionalLong.of(Long.parseUnsignedLong(lengthOrWidth));
        }
        catch (NumberFormatException e)
        {
            return OptionalLong.empty();
        }
    }

    private static Optional<Boolean> tryParseYesNoInput(String input)
    {
        if (input == null || input.length() > 1 || !ConsoleTextUtils.YES_NO_INPUT_ACCEPTED_VALUES.contains(input.toUpperCase()))
        {
            return Optional.empty();
        }

        return Optional.of("Y".equalsIgnoreCase(input));
    }
}
