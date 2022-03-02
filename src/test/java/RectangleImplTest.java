import static org.junit.jupiter.api.Assertions.*;

import com.iholden.entities.Point;
import com.iholden.entities.impl.PointImpl;
import com.iholden.entities.Rectangle;
import com.iholden.entities.impl.RectangleImpl;
import com.iholden.constants.Adjacency;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class RectangleImplTest
{
    protected Rectangle rectangleA;
    protected Rectangle rectangleB;
    protected Set<Point> expectedIntersectionPoints;

    @AfterEach
    public void betweenTestCleanup()
    {
        rectangleA = null;
        rectangleB = null;
        expectedIntersectionPoints = null;
    }

    @Test
    public void contains_rectangleAContainsRectangleB_returnsTrue()
    {
        rectangleA = new RectangleImpl(4, 5, new PointImpl(1, 2));
        rectangleB = new RectangleImpl(2, 3, new PointImpl(2, 3));

        assertTrue(rectangleA.contains(rectangleB));
    }

    @Test
    public void contains_rectangleADoesNotContainRectangleB_returnsFalse()
    {
        rectangleA = new RectangleImpl(4, 5, new PointImpl(1, 2));
        rectangleB = new RectangleImpl(5, 8, new PointImpl(7, 1));

        assertFalse(rectangleA.contains(rectangleB));
    }

    @Test
    public void contains_rectanglesAreSameSizeAndCoordinates_returnsFalse()
    {
        rectangleA = new RectangleImpl(4, 5, new PointImpl(1, 2));
        rectangleB = new RectangleImpl(4, 5, new PointImpl(1, 2));

        assertFalse(rectangleA.contains(rectangleB));
    }

    @Test
    public void contains_rectanglesIntersect_returnsFalse()
    {
        rectangleA = new RectangleImpl(4, 5, new PointImpl(1, 2));
        rectangleB = new RectangleImpl(3, 8, new PointImpl(4, 3));

        assertFalse(rectangleA.contains(rectangleB));
    }

    @Test
    public void contains_rectangleAContainsRectangleBWithOneSharedSide_returnsTrue()
    {
        rectangleA = new RectangleImpl(7, 5, new PointImpl(2, 2));
        rectangleB = new RectangleImpl(2, 1, new PointImpl(7, 3));

        assertTrue(rectangleA.contains(rectangleB));
    }

    @Test
    public void contains_rectangleAContainsRectangleBWithTwoSharedSides_returnsTrue()
    {
        rectangleA = new RectangleImpl(7, 5, new PointImpl(2, 2));
        rectangleB = new RectangleImpl(3, 2, new PointImpl(6, 5));

        assertTrue(rectangleA.contains(rectangleB));
    }

    @Test
    public void contains_rectangleAContainsRectangleBWithThreeSharedSides_returnsTrue()
    {
        rectangleA = new RectangleImpl(7, 5, new PointImpl(2, 2));
        rectangleB = new RectangleImpl(7, 2, new PointImpl(2, 5));

        assertTrue(rectangleA.contains(rectangleB));
    }

    @Test
    public void findIntersectionPointsWith_4VerticalIntersectionPoints_returnsCorrectPoints()
    {
        rectangleA = new RectangleImpl(3, 5, new PointImpl(3,2));
        rectangleB = new RectangleImpl(5, 3, new PointImpl(2, 3));
        expectedIntersectionPoints = Set.of(
                new PointImpl(3, 3),
                new PointImpl(3, 6),
                new PointImpl(6, 3),
                new PointImpl(6, 6));

        Set<Point> actualIntersectionPoints = rectangleA.findIntersectionPointsWith(rectangleB);

        assertEquals(expectedIntersectionPoints.size(), actualIntersectionPoints.size());
        expectedIntersectionPoints.forEach(expectedIntersectionPoint -> assertTrue(actualIntersectionPoints.contains(expectedIntersectionPoint)));
    }

    @Test
    public void findIntersectionPointsWith_2VerticalIntersectionPoints_returnsCorrectPoints()
    {
        rectangleA = new RectangleImpl(4, 5, new PointImpl(1, 2));
        rectangleB = new RectangleImpl(4, 2, new PointImpl(4, 3));
        expectedIntersectionPoints = Set.of(
                new PointImpl(5, 3),
                new PointImpl(5, 5));

        Set<Point> actualIntersectionPoints = rectangleA.findIntersectionPointsWith(rectangleB);

        assertEquals(expectedIntersectionPoints.size(), actualIntersectionPoints.size());
        expectedIntersectionPoints.forEach(expectedIntersectionPoint -> assertTrue(actualIntersectionPoints.contains(expectedIntersectionPoint)));
    }

    @Test
    public void findIntersectionPointsWith_intersectionOnMultipleAxes_returnsCorrectPoints()
    {
        rectangleA = new RectangleImpl(6, 5, new PointImpl(1, 1));
        rectangleB = new RectangleImpl(3, 2, new PointImpl(6, 5));
        expectedIntersectionPoints = Set.of(
                new PointImpl(7, 5),
                new PointImpl(6, 6));

        Set<Point> actualIntersectionPoints = rectangleA.findIntersectionPointsWith(rectangleB);

        assertEquals(expectedIntersectionPoints.size(), actualIntersectionPoints.size());
        expectedIntersectionPoints.forEach(expectedIntersectionPoint -> assertTrue(actualIntersectionPoints.contains(expectedIntersectionPoint)));
    }

    @Test
    public void findIntersectionPointsWith_rectangleContainedWithinOtherRectangle_returnsNoPoints()
    {
        rectangleA = new RectangleImpl(4, 5, new PointImpl(1, 2));
        rectangleB = new RectangleImpl(2, 3, new PointImpl(2, 3));

        Set<Point> actualIntersectionPoints = rectangleA.findIntersectionPointsWith(rectangleB);

        assertTrue(actualIntersectionPoints.isEmpty());
    }

    @Test
    public void findIntersectionPointsWith_rectanglesAreAdjacent_returnsNoPoints()
    {
        rectangleA = new RectangleImpl(5, 4, new PointImpl(2, 2));
        rectangleB = new RectangleImpl(7, 4, new PointImpl(7, 2));

        Set<Point> actualIntersectionPoints = rectangleA.findIntersectionPointsWith(rectangleB);

        assertTrue(actualIntersectionPoints.isEmpty());
    }

    @Test
    public void findIntersectionPointsWith_noIntersectionPoints_returnsNoPoints()
    {
        rectangleA = new RectangleImpl(6, 5, new PointImpl(1, 1));
        rectangleB = new RectangleImpl(3, 2, new PointImpl(8, 5));

        Set<Point> actualIntersectionPoints = rectangleA.findIntersectionPointsWith(rectangleB);

        assertTrue(actualIntersectionPoints.isEmpty());
    }

    @Test
    public void determineAdjacencyWith_properlyAdjacentVertical_returnsCorrectValue()
    {
        rectangleA = new RectangleImpl(5, 4, new PointImpl(2, 2));
        rectangleB = new RectangleImpl(7, 4, new PointImpl(7, 2));

        Adjacency actualAdjacency = rectangleA.determineAdjacencyWith(rectangleB);

        assertEquals(Adjacency.PROPER, actualAdjacency);
    }

    @Test
    public void determineAdjacencyWith_properlyAdjacentHorizontal_returnsCorrectValue()
    {
        rectangleA = new RectangleImpl(5, 4, new PointImpl(2, 2));
        rectangleB = new RectangleImpl(5, 1, new PointImpl(2, 1));

        Adjacency actualAdjacency = rectangleA.determineAdjacencyWith(rectangleB);

        assertEquals(Adjacency.PROPER, actualAdjacency);
    }

    @Test
    public void determineAdjacencyWith_subLineAdjacentHorizontal_returnsCorrectValue()
    {
        rectangleA = new RectangleImpl(6, 6, new PointImpl(2, 2));
        rectangleB = new RectangleImpl(2, 1, new PointImpl(4, 8));

        Adjacency actualAdjacency = rectangleA.determineAdjacencyWith(rectangleB);

        assertEquals(Adjacency.SUB_LINE, actualAdjacency);
    }

    @Test
    public void determineAdjacencyWith_subLineAdjacentVertical_returnsCorrectValue()
    {
        rectangleA = new RectangleImpl(6, 6, new PointImpl(2, 2));
        rectangleB = new RectangleImpl(6, 2, new PointImpl(8, 4));

        Adjacency actualAdjacency = rectangleA.determineAdjacencyWith(rectangleB);

        assertEquals(Adjacency.SUB_LINE, actualAdjacency);
    }

    @Test
    public void determineAdjacencyWith_partiallyAdjacentVertical_returnsCorrectValue()
    {
        rectangleA = new RectangleImpl(5, 8, new PointImpl(3, 0));
        rectangleB = new RectangleImpl(5, 5, new PointImpl(8, 5));

        Adjacency actualAdjacency = rectangleA.determineAdjacencyWith(rectangleB);

        assertEquals(Adjacency.PARTIAL, actualAdjacency);
    }

    @Test
    public void determineAdjacencyWith_partiallyAdjacentHorizontal_returnsCorrectValue()
    {
        rectangleA = new RectangleImpl(5, 8, new PointImpl(3, 0));
        rectangleB = new RectangleImpl(5, 2, new PointImpl(0, 8));

        Adjacency actualAdjacency = rectangleA.determineAdjacencyWith(rectangleB);

        assertEquals(Adjacency.PARTIAL, actualAdjacency);
    }

    @Test
    public void determineAdjacencyWith_singlePointCornerAdjacentBottomRight_returnsCorrectValue()
    {
        rectangleA = new RectangleImpl(4, 3, new PointImpl(3, 3));
        rectangleB = new RectangleImpl(3, 1, new PointImpl(7, 2));

        Adjacency actualAdjacency = rectangleA.determineAdjacencyWith(rectangleB);

        assertEquals(Adjacency.SINGLE_POINT, actualAdjacency);
    }

    @Test
    public void determineAdjacencyWith_singlePointCornerAdjacentTopRight_returnsCorrectValue()
    {
        rectangleA = new RectangleImpl(4, 3, new PointImpl(3, 3));
        rectangleB = new RectangleImpl(2, 4, new PointImpl(7, 6));

        Adjacency actualAdjacency = rectangleA.determineAdjacencyWith(rectangleB);

        assertEquals(Adjacency.SINGLE_POINT, actualAdjacency);
    }

    @Test
    public void determineAdjacencyWith_singlePointCornerAdjacentTopLeft_returnsCorrectValue()
    {
        rectangleA = new RectangleImpl(4, 3, new PointImpl(3, 3));
        rectangleB = new RectangleImpl(2, 2, new PointImpl(1, 6));

        Adjacency actualAdjacency = rectangleA.determineAdjacencyWith(rectangleB);

        assertEquals(Adjacency.SINGLE_POINT, actualAdjacency);
    }

    @Test
    public void determineAdjacencyWith_singlePointCornerAdjacentBottomLeft_returnsCorrectValue()
    {
        rectangleA = new RectangleImpl(4, 3, new PointImpl(3, 3));
        rectangleB = new RectangleImpl(1, 1, new PointImpl(2, 2));

        Adjacency actualAdjacency = rectangleA.determineAdjacencyWith(rectangleB);

        assertEquals(Adjacency.SINGLE_POINT, actualAdjacency);
    }
}