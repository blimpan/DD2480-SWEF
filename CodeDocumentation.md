# DD2480 Lab1 Code Documentation

# Launch Interceptor Program

## Introduction

The Launch Interceptor Program is designed to evaluate radar tracking information as part of an anti-ballistic missile system. The core function, `DECIDE()`, generates a boolean signal indicating whether an interceptor should be launched based on various Launch Interceptor Conditions (LICs). The program processes up to 100 planar data points to assess the conditions for a potential launch. The system utilizes a Conditions Met Vector (CMV) and a Logical Connector Matrix (LCM) to determine the final launch decision through a Preliminary Unlocking Matrix (PUM) and a Final Unlocking Vector (FUV).

## Code Infrastructure

The project consists of two primary Java files:

1. **LaunchInterceptor.java**: This file contains the implementation of the `LaunchInterceptor` class.
2. **LaunchInterceptorTest.java**: This file includes unit tests for the methods defined in the `LaunchInterceptor` class.

## LaunchInterceptor.java

This file implements the `LaunchInterceptor` class, which encapsulates the logic for evaluating the various LICs based on the input radar data.

### Attributes

- `Parameters parameters`: Holds the launch control parameters.
- `int numPoints`: The number of data points.
- `double[] x`: Array containing the X coordinates of data points.
- `double[] y`: Array containing the Y coordinates of data points.
- `Connectors[][] LCM`: Logical Connector Matrix.
- `boolean[] PUV`: Preliminary Unlocking Vector.
- `boolean processed`: Indicates if the decide() method has been called.
- `boolean[][] PUM`: Preliminary Unlocking Matrix.
- `boolean[] CMV`: Conditions Met Vector.
- `boolean[] FUV`: Final Unlocking Vector.
- `boolean launch`: Indicates the launch decision.

### Code Decription

### 1. Constructor

### `LaunchInterceptor(Parameters parameters, int numPoints, double[][] pointCoords, Connectors[][] LCM, boolean[] PUV)`

### Specifications

- Initializes the `LaunchInterceptor` with provided parameters, number of points, coordinates, Logical Connector Matrix (LCM), and Preliminary Unlocking Vector (PUV).
- Validates input lengths and initializes arrays for X and Y coordinates.

### Used Attributes

- `parameters`: Launch control parameters.
- `numPoints`: Number of data points.
- `x`: X coordinates of data points.
- `y`: Y coordinates of data points.
- `LCM`: Logical Connector Matrix.
- `PUV`: Preliminary Unlocking Vector.

### Output Type

- Constructs a `LaunchInterceptor` object.

---

### 2. Decide Method

### `boolean decide()`

### Specifications

- Decides whether a launch should occur based on the conditions evaluated from the input data.
- Populates the Preliminary Unlocking Matrix (PUM), Conditions Met Vector (CMV), and Final Unlocking Vector (FUV).

### Used Attributes

- `LCM`: Logical Connecting Matrix.
- `PUV`: Preliminary Unlocking Vector
- `PUM`: Preliminary Unlocking Matrix.

### Output Type

- Returns a boolean indicating the launch decision.

---

### 3. LIC Methods

There are a total of 15 LICs to be implemented, each will return `true` or `false` based on the specifications respectively.

### 3.1 LIC 0

### `boolean determineLIC0()`

### Specifications

- Determines if there exists at least one set of two consecutive data points that are greater than `LENGTH1` apart.

### Used Attributes

- `numPoints`: Number of data points.
- `parameters.LENGTH1`: Length threshold.

### Output Type

- Returns `true` or `false`.

---

### 3.2 LIC 1

### `boolean determineLIC1()`

### Specifications

- Determines if there exists at least one set of three consecutive data points that cannot all be contained within a circle of radius `RADIUS1`.

### Used Attributes

- `numPoints`: Number of data points.
- `parameters.RADIUS1`: Radius threshold.

### Output Type

- Returns `true` if the condition is met, otherwise `false`.

---

### 3.3 LIC 2

### `boolean determineLIC2()`

### Specifications

- Determines if there exists at least one set of three consecutive data points that form an angle less than `(PI - EPSILON)` or greater than `(PI + EPSILON)`.

### Used Attributes

- `numPoints`: Number of data points.
- `parameters.EPSILON`: Epsilon threshold.

### Output Type

- Returns `true` if the angle condition is met, otherwise `false`.

---

### 3.4 LIC 3

### `boolean determineLIC3()`

### Specifications

- Determines if there exists at least one set of three consecutive data points that form a triangle with an area greater than `AREA1`.

### Used Attributes

- `numPoints`: Number of data points.
- `parameters.AREA1`: Area threshold.

### Output Type

- Returns `true` if the area condition is met, otherwise `false`.

---

### 3.5 LIC 4

### `boolean determineLIC4()`

### Specifications

- Determines if there exists at least one set of `Q_PTS` points that lie in more than `QUADS` different quadrants.

### Used Attributes

- `numPoints`: Number of data points.
- `parameters.Q_PTS`: Number of points to consider.
- `parameters.QUADS`: Number of quadrants threshold.

### Output Type

- Returns `true` if the condition is met, otherwise `false`.

---

### 3.6 LIC 5

### `boolean determineLIC5()`

### Specifications

- Determines if there exists at least one set of two consecutive data points such that `X[j] - X[i] < 0`.

### Used Attributes

- `numPoints`: Number of data points.

### Output Type

- Returns `true` if the condition is met, otherwise `false`.

---

### 3.7 LIC 6

### `boolean determineLIC6()`

### Specifications

- Determines if there exists at least one set of `N_PTS` consecutive data points such that at least one point lies a distance greater than `DIST` from the line joining the first and last points.

### Used Attributes

- `numPoints`: Number of data points.
- `parameters.N_PTS`: Number of points to consider.
- `parameters.DIST`: Distance threshold.

### Output Type

- Returns `true` if the condition is met, otherwise `false`.

---

### 3.8 LIC 7

### `boolean determineLIC7()`

### Specifications

- Determines if there exists a set of two data points separated by exactly `K_PTS` consecutive intervening points that are a distance greater than `LENGTH1` apart.

### Used Attributes

- `numPoints`: Number of data points.
- `parameters.K_PTS`: Number of intervening points.
- `parameters.LENGTH1`: Length threshold.

### Output Type

- Returns `true` if the condition is met, otherwise `false`.

---
### 3.9 LIC 8

### `boolean determineLIC8()`

###Specification

-Determines if there exists at least one set of three data points separated by exactly A_PTS and B_PTS consecutive intervening points, respectively, that cannot be contained within or on a circle of radius RADIUS1.

### Output Type

- Returns `true` if condition is metor `false` otherwise"
---

### 3.10 LIC 9

### `boolean determineLIC9()`

### Specifications

- Determines if there exists a set of three points (A, B, C) such that:
    - There are `C_PTS` in between A and B.
    - There are `D_PTS` in between B and C.
    - The angle is defined.
    - Angle < (PI - EPSILON) or angle > (PI + EPSILON).
    - `NUMPOINTS` >= 5.
    - `C_PTS` >= 1.
    - `D_PTS` >= 1.
    - `C_PTS + D_PTS ≤ NUMPOINTS - 3`.

### Used Attributes

- `numPoints`: Number of data points.
- `parameters.C_PTS`: Number of points between A and B.
- `parameters.D_PTS`: Number of points between B and C.
- `parameters.EPSILON`: Epsilon threshold.

### Output Type

- Returns `true` if the conditions are met, otherwise `false`.

---

### 3.11 LIC 10

### `boolean determineLIC10()`

### Specifications

- Determines if there exists a set of three data points (A, B, C) such that:
    - There are `E_PTS` in between A and B.
    - There are `F_PTS` in between B and C.
    - The points form a triangle with an area greater than `AREA1`.
    - The condition is not met when `NUMPOINTS < 5`.

### Used Attributes

- `numPoints`: Number of data points.
- `parameters.E_PTS`: Number of points between A and B.
- `parameters.F_PTS`: Number of points between B and C.
- `parameters.AREA1`: Area threshold.

### Output Type

- Returns `true` if the conditions are met, otherwise `false`.

---

### 3.12 LIC 11

### `boolean determineLIC11()`

### Specifications

- Determines if there exists a set of two data points (X[i], Y[i]) and (X[j], Y[j]) separated by exactly `G_PTS` consecutive intervening points such that `X[j] - X[i] < 0`.

### Used Attributes

- `numPoints`: Number of data points.
- `parameters.G_PTS`: Number of intervening points.

### Output Type

- Returns `true` if the condition is met, otherwise `false`.

---

### 3.13 LIC 12

### `boolean determineLIC12()`

### Specifications

- Determines if there exists a set of two data points separated by exactly `K_PTS` consecutive intervening points, one distance greater than `LENGTH1` and the other less than `LENGTH2`.

### Used Attributes

- `numPoints`: Number of data points.
- `parameters.K_PTS`: Number of intervening points.
- `parameters.LENGTH1`: Length threshold.
- `parameters.LENGTH2`: Length threshold.

### Output Type

- Returns `true` if both conditions are met, otherwise `false`.

---

### 3.14 LIC 13

### `boolean determineLIC13()`

### Specifications

- Determines if there exists a set of three data points such that:
    - One set cannot be contained within a circle of radius `RADIUS1`.
    - Another set can be contained within a circle of radius `RADIUS2`.

### Used Attributes

- `numPoints`: Number of data points.
- `parameters.A_PTS`: Number of intervening points.
- `parameters.B_PTS`: Number of intervening points.
- `parameters.RADIUS1`: Radius threshold.
- `parameters.RADIUS2`: Radius threshold.

### Output Type

- Returns `true` if both conditions are met, otherwise `false`.

---

### 3.15 LIC 14

### `boolean determineLIC14()`

### Specifications

- Determines if there exists a set of three data points such that:
    - The area of the triangle formed is greater than `AREA1`.
    - The area of the triangle formed is less than `AREA2`.

### Used Attributes

- `numPoints`: Number of data points.
- `parameters.E_PTS`: Number of intervening points.
- `parameters.F_PTS`: Number of intervening points.
- `parameters.AREA1`: Area threshold.
- `parameters.AREA2`: Area threshold.

### Output Type

- Returns `true` if both conditions are met, otherwise `false`.

---

### 4. Helper Functions

### 4.1 Comparing Doubles

### `CompType doubleCompare(double A, double B)`

### Specifications

- Compares two floating-point numbers to determine their approximate equality.

### Input s

- `double A`
- `double B`

### Output Type

- Returns a `CompType` indicating whether A is less than, equal to, or greater than B.
- the `CompType (GT, EQ, LT)` is enumerated.

---

### 4.2 Method

### `double pointLineDistance(double a, double b, double c, double x, double y)`

### Specifications

- Calculates the distance from a point `(x, y)` to a line defined by the equation `ax + by + c = 0`.

### Inputs

- `double a` : the weight of x of the line equation
- `double b` : the weight of y of the line equation
- `double c` : the constant in the line equation
- `double x` : the x-coordinate of the point
- `double y` : the y-coordinate of the point

### Output Type

- Returns the distance as a `double`.

---

### 4.3  Distance Between Two Points

### `double pointsDistance(double x1, double y1, double x2, double y2)`

### Specifications

- Calculates the distance between two points `(x1, y1)` and `(x2, y2)`.

### Inputs

- `double x1` : the x-coordinate of the first point
- `double y1` : the y-coordinate of the first point
- `double x2` : the x-coordinate of the second point
- `double y2` : the y-coordinate of the second point

### Output Type

- Returns the distance as a `double`.

---

### 4.4 Angle Between Three Points

### `double computeAngle(int aIndex, int bIndex, int cIndex)`

### Specifications

- Calculates the angle formed by three points, with the vertex at Point B. Uses the coordinates of the points identified by the provided indexes.

### Used Attributes

- `x`: Array of X coordinates.
- `y`: Array of Y coordinates.

### Inputs

- `int aIndex` : the index of the first point
- `int bIndex` : the index of the second point
- `int cIndex` : the index of the third point

### Output Type

- Returns the angle in radians as a `double`.

---

### 4.5 Area of Triangle Given Three Points

### `double computeTriangleArea(int aIndex, int bIndex, int cIndex)`

### Specifications

- Calculates the area of a triangle formed by three points using the SHOELACE formula.

### Used Attributes

- `x`: Array of X coordinates.
- `y`: Array of Y coordinates.

### Inputs

- `int aIndex`: The index of the first point (Point A).
- `int bIndex`: The index of the second point (Point B).
- `int cIndex`: The index of the third point (Point C).

### Output Type

- Returns the area as a `double`.

---

### 4.6 Circle Containing Three Given Points

### `boolean containedInCircle(double x1, double y1, double x2, double y2, double x3, double y3, double radius, boolean mode)`

### Specifications

- Determine if all three points can be contained in a circle or if there is no circle that contains all three points.

### Used Attributes

- `pointsX`: Array of X coordinates.
- `pointsY`: Array of Y coordinates.

### Inputs

- `double x1`: X coordinate of the first point.
- `double y1`: Y coordinate of the first point.
- `double x2`: X coordinate of the second point.
- `double y2`: Y coordinate of the second point.
- `double x3`: X coordinate of the third point.
- `double y3`: Y coordinate of the third point.
- `double radius`: The radius of the circle to check against.
- `boolean mode`: When `false`, checks if all points can be contained within the circle; when `true`, checks if no circle of that radius can contain all three points.

### Output Type

- Returns `true` or `false` based on whether the points can be contained within or excluded from the circle.

---

### LaunchInterceptorTest.java

This file contains unit tests for the `LaunchInterceptor` class. It verifies the functionality of the implemented methods, ensuring accurate evaluations of the LIC conditions and launch decisions based on input parameters and data points. The tests cover:

- **Constructor Testing**:
    - Valid parameter initialization and launchInterceptor instantiation.
- **Getters Testing**
    - Valid the getter methods for the launchInterceptor class.
- **Decide Method Testing**
    - Valid the output of the decide method based on hardcoded inputs.
- **LIC Method Testing**:
    - General cases where the condition is satisfied.
    - Boundary cases with the minimum number of points.
    - Invalid parameters (e.g., negative `LENGTH1`).
- **Helper Function Testing**
    - Invalid Input
    - General cases
    - Complex computations Cases
---

## Contributions
Written by Cheang Weng Io
