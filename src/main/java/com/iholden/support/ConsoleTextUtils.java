package com.iholden.support;

import com.iholden.entities.Point;
import com.iholden.entities.Rectangle;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class holding constants and other static support methods for the application's console IO
 */
public class ConsoleTextUtils
{
    public static final List<String> RECTANGLE_LABELS = List.of("A", "B");

    public static final List<String> YES_NO_INPUT_ACCEPTED_VALUES = List.of("Y", "N");

    public static final String INITIAL_MESSAGE = "Use this application to calculate data about the relationships between two rectangles.";

    public static final String RECTANGLE_INPUT_LABEL_PROMPT = "Enter a name for this Rectangle: ";

    public static final String RECTANGLE_INPUT_LENGTH_PROMPT_TEMPLATE = "Enter Rectangle %s LENGTH: ";

    public static final String RECTANGLE_INPUT_WIDTH_PROMPT_TEMPLATE = "Enter Rectangle %s WIDTH: ";

    public static final String RECTANGLE_LENGTH_WIDTH_INPUT_INVALID = "Invalid input; LENGTH/WIDTH must be a positive, whole number. Please try again...";

    public static final String RECTANGLE_INPUT_BOTTOM_LEFT_X_COORDINATE_TEMPLATE = "Enter Rectangle %s BOTTOM-LEFT X-COORDINATE: ";

    public static final String RECTANGLE_INPUT_BOTTOM_LEFT_Y_COORDINATE_TEMPLATE = "Enter Rectangle %s BOTTOM-LEFT Y-COORDINATE: ";

    public static final String RECTANGLE_COORDINATE_INPUT_INVALID = "Invalid input; X/Y COORDINATE must be a valid, whole number";

    public static final String CHECK_ADDITIONAL_RECTANGLES_PROMPT = "Check additional rectangles? (Y or N): ";

    public static final String YES_NO_QUESTION_INPUT_INVALID = "Invalid input; Enter \"Y\" for \"Yes\" and \"N\" for \"No\". Please try again...";

    public static final String RECTANGLE_COMPARISON_RESULTS_TEMPLATE =
            """
            ===================================================================
            | Results                                                         |
            ===================================================================
              Rectangle %s Area: %d
              Rectangle %s Area: %d
              Rectangles are Identical: %b
              Rectangle %s CONTAINS Rectangle %s: %b
              Rectangle %s INTERSECTS with Rectangle %s at points: %s
              Rectangle Adjacency status: %s
            ===================================================================
            """;

    public static String getRectangleComparisonResults(Map<String, Rectangle> rectanglesByLabel)
    {
        Rectangle rectangleA = rectanglesByLabel.get(RECTANGLE_LABELS.get(0));
        Rectangle rectangleB = rectanglesByLabel.get(RECTANGLE_LABELS.get(1));

        return getRectangleComparisonResults(rectangleA, rectangleB);
    }

    private static String getRectangleComparisonResults(Rectangle rectangleA, Rectangle rectangleB)
    {
        return RECTANGLE_COMPARISON_RESULTS_TEMPLATE.formatted(
                "A", rectangleA.getArea(),
                "B", rectangleB.getArea(),
                rectangleA.equals(rectangleB),
                "A", "B", rectangleA.contains(rectangleB),
                "A", "B", rectangleA.findIntersectionPointsWith(rectangleB).stream().map(Point::prettyPrint).collect(Collectors.joining(", ")),
                rectangleA.determineAdjacencyWith(rectangleB));
    }

    // Discourage Instantiation
    private ConsoleTextUtils(){};
}
