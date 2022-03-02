# README

## Rectangles
This application allows for analyzing and comparing features among rectangles. The interactive CLI will 
request information about 2 rectangles, and then output a result that describes the shared characteristics 
of the provided rectangles.

Unless otherwise specified, all numeric input is expected to be in the form of whole-number, `Long` precision.

### Input
* Length - the horizontal length of the rectangle
* Width - the vertical width of the rectangle
* Lower-left Point - the x and y coordinates of the lower-left corner of the Rectangle

### Output
* Area - the area of each respective rectangle
* Classification Data
  * Are the rectangles are IDENTICAL
  * Does Rectangle A CONTAIN Rectangle B
  * Do the rectangles INTERSECT
    * Points of INTERSECTION, represented as `(x, y)`
  * Are the rectangles ADJACENT
    * Type of adjacency

#### Classifications
Each classification of rectangle-relationship data is considered to be _mutually exclusive_ for the purposes 
of this application; only a single classification will apply to the provided rectangle pair. 

A pair of rectangles that are IDENTICAL cannot INTERSECT, a pair of rectangles that are ADJACENT cannot 
contain each other, etc.

* **IDENTICAL** - the rectangles are exactly the same; they are of the same length/width and their corner points are the same
* **CONTAINMENT** - Rectangle A contains Rectangle B in its entirety. The rectangles may share one or more line segments, but they may not fully intersect
* **INTERSECTION** - the rectangles have lines that _fully_ pass through each other on opposing axes
* **ADJACENCY** - the rectangles have lines/line-segments that share one or more points in common on the same axis; Rectangle A _must not contain_ Rectangle B, and the rectangles _must not intersect_ at any point.
  * **PROPER** - the rectangles share an adjacent line that is identical, e.g. it contains the exact same points
  * **SUB-LINE** - the rectangles share an adjacent line such that Rectangle B's side exists entirely within the confines of Rectangle A's side
  * **PARTIAL** - the rectangles share an adjacent line that is not PROPER or SUB-LINE; the shared side of Rectangle B exceeds the boundary of Rectangle A's side
  * **CORNER-POINT** - the rectangles share only a single, corner point in common

**Note**: IDENTICAL and CORNER-POINT ADJACENCY classifications are extensions of the original specification.

## Running the Application
The application can be run via the Gradle wrapper via the Gradle application plugin, as follows:

### Linux
```shell
./gradlew run --console=plain
```

### Windows
```shell
.\gradlew run --console=plain
```

**Note**: be sure to include `--console=plain` when running via Gradle wrapper; otherwise, Gradle's enhanced
console output will interfere with the console output of this application, making it difficult to read.

## Running the Test Suite
```shell
./gradlew test
```

## Requirements
* Java 15
* Gradle 7.4

## Dependencies
* JUnit Jupiter 5.8.2