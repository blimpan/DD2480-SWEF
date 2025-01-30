# The Launch Interceptor

![Generated by Adobe Firefly](./Resources/Cover.jpg)

Generated by Adobe Firefly

## The Mission of the Project

The Launch Interceptor Project implements a function called `DECIDE()` that determines whether an interceptor should be launched based on input radar tracking information. It evaluates a set of conditions known as Launch Interceptor Conditions (LICs) against radar data points to generate a boolean signal that indicates whether to issue a launch-unlock signal.

## Why This Project is a Game Changer

This project is crucial for anti-ballistic missile systems, providing a systematic approach to decision-making based on real-time radar data. By evaluating multiple conditions for interceptor launch, it enhances the reliability and safety of missile defense operations, ensuring that launches occur only when necessary conditions are met.

## Your Launch Pad: Getting Started

To create your own Launch Interceptor, you will need to prepare the following attributes:

1. **NUMPOINTS**: The number of planar data points (ranging from 2 to 100).
2. **POINTS**: An array containing the coordinates of the data points, formatted as pairs of X and Y coordinates.
3. **PARAMETERS**: A struct holding various parameters for the LICs, including:
    - `LENGTH1`: Length threshold for distance evaluations.
    - `RADIUS1`: Radius for circular containment checks.
    - `EPSILON`: Epsilon threshold for angle evaluations.
    - `AREA1`: Area threshold for triangle evaluations.
    - `Q_PTS`: Number of points to consider in quadrant evaluations.
    - `QUADS`: Number of quadrants threshold.
    - Additional parameters for other LICs.
4. **LCM**: A Logical Connector Matrix defining how individual LICs should be combined (AND, OR, or NOTUSED).
5. **PUV**: A Preliminary Unlocking Vector indicating which LICs are relevant for the launch decision.

## Need Assistance? Mission Control is Here!

If you encounter any issues or have questions about the project, please feel free to file an issue in the project's repository. Your feedback and inquiries are welcome and will help improve the project!

## Wonder How the Code Works?

If you would like to understand how the code works, please check out [Code Documentation](./CodeDocumentation.md)

## Statement of Contributions

### Project Title: Launch Interceptor

### Contributors
- **Henrik Pendersén (**[https://github.com/WatermelonGodz](https://github.com/WatermelonGodz))
    - **Contributions**:
        - Designed and Implemented the LIC00, LIC01, LIC02 methods.
        - Designed and Implemented the Helper Function `containedInCircle()`
        - Created unit tests for LIC00, LIC01, LIC02 methods, and the above helper function.
- **Linus Bälter (**[https://github.com/blimpan](https://github.com/blimpan))
    - **Contributions**:
        - Designed the code infrastructure and created the skeleton.
        - Designed the skeleton of the [`LaunchInterceptorTEST.java`](http://LaunchInterceptorTEST.java)
        - Designed and Implemented the LIC03, LIC04, LIC05 methods.
        - Created unit tests for LIC03, LIC04, LIC05 methods.
- **Kristin Rosen (**[https://github.com/KristinRosen](https://github.com/KristinRosen))
    - **Contributions**:
        - Designed and Implemented the LIC06, LIC07, LIC08 methods.
        - Designed and Implemented the Helper Function `pointLineDistance()` and `pointsDistance()`
        - Created unit tests for LIC06, LIC07, LIC08 methods, and the above helper function.
- **Cheang Weng Io, Yoyo (**[https://github.com/beginner003](https://github.com/beginner003))
    - **Contributions**:
        - Authored and maintained project documentation, including `README.md` and`CodeDocumentation.md`.
        - Designed and Implemented the LIC09, LIC10, LIC11 methods.
        - Designed and Implemented the Helper Functions `computeAngle()` and `computeTriangleArea()`
        - Created unit tests for LIC09, LIC10, LIC11 methods, and the above helper functions.
- **Edgar Wolff (**[https://github.com/edgarwolff](https://github.com/edgarwolff))
    - **Contributions**:
        - Refactored the code infrastructure for easier implementations of LIC methods.
        - Designed and Implemented the LIC12, LIC13, LIC14, `decide()` methods.
        - Created unit tests for Constructors, LIC12, LIC13 LIC14, `decide()` methods.
        - Designed and Implemented the the `main()` function.

### Way of working (according to Essence)

We would describe our group as being in the earliest stages of the Essence Way-of-working scale. Because of limited time to complete the assignment, it was necessary to
begin working before properly establishing principles and foundation. Therefore we would say we are in state 1: *Principles Established*, or part-way throught state 2: *Foundation Established*. 
To move forward, we need to have a proper converstaion about key practices and the way-of-working. We must try and apply what we've learned from this assignment and discuss what we did wrong this time, 
so everyone is on the same page reagrding how we should work in the next one.


### Acknowledgments

We would like to thank all contributors for their efforts and dedication to making this project successful. Your hard work and collaboration have been invaluable.


