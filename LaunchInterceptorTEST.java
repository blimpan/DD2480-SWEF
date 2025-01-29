import org.junit.*;

import static org.junit.Assert.fail;

public class LaunchInterceptorTEST {

    // Variables for minimum test input
    private final int minNumPoints = 2;
    private final double[][] minPoints = {{0, 0}, {1, 1}};

    private final LaunchInterceptor.Parameters minParameters =
            new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5,
                    2.0, 0.05, 0.5, 3, 2, 1, 1,
                    1, 1, 1, 1, 1, 1, 1);

    private final LaunchInterceptor.Connectors[][] minLCM = new LaunchInterceptor.Connectors[15][15];
    private final boolean[] minPUV =
            {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true};

    @Before
    public void setUp() {

        // Populate the matrix
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (i == j) {
                    minLCM[i][j] = LaunchInterceptor.Connectors.NOTUSED; // Diagonal elements are NOTUSED
                } else if ((i + j) % 2 == 0) {
                    minLCM[i][j] = LaunchInterceptor.Connectors.ANDD; // Alternate with ANDD
                } else {
                    minLCM[i][j] = LaunchInterceptor.Connectors.ORR;  // Alternate with ORR
                }
            }
        }
    }

    @Test
    public void testConstructorConditions() {
        LaunchInterceptor interceptor = new LaunchInterceptor(minParameters, minNumPoints, minPoints, minLCM, minPUV);

        Assert.assertThrows(IllegalArgumentException.class,
                () -> new LaunchInterceptor(minParameters, minNumPoints, new double[4][5], minLCM, minPUV)
        );
        Assert.assertThrows(IllegalArgumentException.class,
                () -> new LaunchInterceptor(minParameters, minNumPoints, minPoints,
                        new LaunchInterceptor.Connectors[3][4], minPUV)
        );
        Assert.assertThrows(IllegalArgumentException.class,
                () -> new LaunchInterceptor(minParameters, minNumPoints, minPoints, minLCM, new boolean[3])
        );
    }

    @Test
    public void testConstructedLInterceptorGetters() {
        LaunchInterceptor interceptor = new LaunchInterceptor(minParameters, minNumPoints, minPoints, minLCM, minPUV);

        Assert.assertThrows(IllegalAccessError.class, interceptor::getPUM);
        Assert.assertThrows(IllegalAccessError.class, interceptor::getFUV);
        Assert.assertThrows(IllegalAccessError.class, interceptor::getCMV);
        Assert.assertThrows(IllegalAccessError.class, interceptor::isLaunch);

        Assert.assertEquals(minNumPoints, interceptor.getX().length);
        Assert.assertEquals(minNumPoints, interceptor.getY().length);
    }

    @Test
    public void testDecideValidInput() {
        var param = new LaunchInterceptor.Parameters(1, 1, 1, 1, 10,
                10, 10, 1, 3, 1, 2, 1, 1, 1,
                1, 1, 1, 1, 1);

        boolean[] decidePUV = {true, true, false, true, false, true, false, true,
                true, false, true, true, true, true, true};

        // Arbitrary but coherent values
        var decideLCM = fromIntArray(new int[][]{
                {0, 1, 2, 1, 2, 1, 0, 1, 1, 2, 1, 0, 0, 1, 1},
                {1, 0, 2, 1, 2, 1, 0, 1, 2, 2, 2, 0, 0, 2, 2},
                {2, 2, 0, 2, 0, 2, 0, 2, 0, 0, 2, 0, 0, 0, 0},
                {1, 1, 2, 0, 2, 1, 0, 1, 2, 0, 2, 0, 0, 2, 2},
                {2, 2, 0, 2, 0, 0, 0, 2, 0, 0, 2, 0, 0, 2, 0},
                {1, 1, 2, 1, 0, 0, 0, 2, 1, 2, 2, 0, 0, 2, 2},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0},
                {1, 1, 2, 1, 2, 2, 0, 0, 2, 0, 2, 0, 0, 2, 2},
                {1, 2, 0, 2, 0, 1, 0, 2, 0, 2, 2, 0, 0, 1, 0},
                {2, 2, 0, 0, 0, 2, 0, 0, 2, 0, 2, 0, 0, 2, 2},
                {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0},
                {1, 2, 0, 2, 2, 2, 2, 2, 1, 2, 2, 1, 2, 0, 2},
                {1, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0}
        });
        var pointCoords = new double[][]{
                {0, 0}, {5, 10}, {1, 1}, {-6, 15}, {0, 1}, {-15, -2}, {1, 0}, {14, -20}, {-1, 0},
                {0, -1}, {0, 0}, {-1, -1}, {2, 1}, {5, 16}, {-20, 5}, {-5, -10}, {12, -7}
        };
        var lInterceptor = new LaunchInterceptor(param, pointCoords.length, pointCoords, decideLCM, decidePUV);

        Assert.assertTrue(null, lInterceptor.decide());
        Assert.assertTrue(null, lInterceptor.isLaunch());
        Assert.assertArrayEquals(new Boolean[]{true, true, true, true, true, true,
                true, true, true, true, true, true, true, true, true},
                lInterceptor.getFUV()
        );

        assertNoExceptionIsThrown(lInterceptor::getPUM);
        assertNoExceptionIsThrown(lInterceptor::getFUV);
        assertNoExceptionIsThrown(lInterceptor::getCMV);
        assertNoExceptionIsThrown(lInterceptor::isLaunch);
    }

    @Test
    public void testLIC0FartherThanL1() {
        var param = new LaunchInterceptor.Parameters(5, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0);
        var pointCoords = new double[][]{{0, 0}, {0, 1}, {2, 1}, {5, 1}, {10, 3}, {-1, 7}, {7, -1}, {0, 1}};
        var lInterceptor = new LaunchInterceptor(param, 8, pointCoords, minLCM, minPUV);
        Assert.assertTrue(null, lInterceptor.determineLIC0());
    }

    @Test
    public void testLIC0CloserThanL1() {
        var param = new LaunchInterceptor.Parameters(50, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0);
        var pointCoords = new double[][]{{0, 0}, {0, 1}, {2, 1}, {5, 1}, {10, 3}, {-1, 7}, {7, -1}, {0, 1}};
        var lInterceptor = new LaunchInterceptor(param, 8, pointCoords, minLCM, minPUV);
        Assert.assertFalse(null, lInterceptor.determineLIC0());
    }

    @Test
    public void testLIC0InvalidParameters() {
        var param = new LaunchInterceptor.Parameters(-2, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 2, 0, 0, 0,
                0, 0, 0, 0);
        var pointCoords = new double[][]{{0, 0}, {0, 1}, {2, 1}, {5, 1}, {10, 3}, {-1, 7}, {7, -1}, {0, 1}};
        var lInterceptor = new LaunchInterceptor(param, 8, pointCoords, minLCM, minPUV);
        Assert.assertThrows(IllegalArgumentException.class, lInterceptor::determineLIC0); // Negative length
    }

    @Test
    public void testLIC1LargerThanL1() {
        var param = new LaunchInterceptor.Parameters(0, 5, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0);
        var pointCoords = new double[][]{{0, 0}, {0, 1}, {2, 1}, {5, 1}, {10, 3}, {-1, 7}, {7, -1}, {0, 1}};
        var lInterceptor = new LaunchInterceptor(param, 8, pointCoords, minLCM, minPUV);
        Assert.assertTrue(null, lInterceptor.determineLIC1());
    }

    @Test
    public void testLIC1SmallerThanL1() {
        var param = new LaunchInterceptor.Parameters(0, 50, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0);
        var pointCoords = new double[][]{{0, 0}, {0, 1}, {2, 1}, {5, 1}, {10, 3}, {-1, 7}, {7, -1}, {0, 1}};
        var lInterceptor = new LaunchInterceptor(param, 8, pointCoords, minLCM, minPUV);
        Assert.assertFalse(null, lInterceptor.determineLIC1());
    }

    @Test
    public void testLIC1InvalidParameters() {
        var param = new LaunchInterceptor.Parameters(0, -10, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 2, 0, 0, 0,
                0, 0, 0, 0);
        var pointCoords = new double[][]{{0, 0}, {0, 1}, {2, 1}, {5, 1}, {10, 3}, {-1, 7}, {7, -1}, {0, 1}};
        var lInterceptor = new LaunchInterceptor(param, 8, pointCoords, minLCM, minPUV);
        Assert.assertThrows(IllegalArgumentException.class, lInterceptor::determineLIC1); // Negative length
    }

    @Test
    public void testLIC1InsufficientPoints() {
        var param = new LaunchInterceptor.Parameters(0, 10, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0);
        var lInterceptor = new LaunchInterceptor(param, 2, new double[][]{{0, 0}, {1, 1}}, minLCM, minPUV);
        Assert.assertFalse(lInterceptor.determineLIC1());
    }

    @Test
    public void testLIC2LargerThanL1() {
        var param = new LaunchInterceptor.Parameters(0, 0, 1, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0);
        var pointCoords = new double[][]{{0, 0}, {0, 1}, {2, 1}, {5, 1}, {10, 3}, {-1, 7}, {7, -1}, {0, 1}};
        var lInterceptor = new LaunchInterceptor(param, 8, pointCoords, minLCM, minPUV);
        Assert.assertTrue(null, lInterceptor.determineLIC2());
    }

    @Test
    public void testLIC2SmallerThanL1() {
        var param = new LaunchInterceptor.Parameters(0, 0, 3.1, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0);
        var pointCoords = new double[][]{{0, 0}, {0, 1}, {2, 1}, {5, 1}, {10, 3}, {-1, 7}, {7, -1}, {0, 1}};
        var lInterceptor = new LaunchInterceptor(param, 8, pointCoords, minLCM, minPUV);
        Assert.assertTrue(null, lInterceptor.determineLIC2());
    }

    @Test
    public void testLIC2InsufficientPoints() {
        var param = new LaunchInterceptor.Parameters(0, 0, 1, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0);
        var lInterceptor = new LaunchInterceptor(param, 2, new double[][]{{0, 0}, {1, 1}}, minLCM, minPUV);
        Assert.assertFalse(lInterceptor.determineLIC2());
    }

    @Test
    public void LIC3Test() {
        /*
         * Should test the following...
         * (1) Number of points < 3 --> false (not enough points)
         * (2) AREA1 < 0 --> false (invalid AREA1)
         * (3) Valid points with valid AREA1 --> true (triangle area greater than AREA1)
         * (4) Valid points with valid AREA1 --> false (triangle area less than AREA1)
         */

        double[][] twoPoints = {{0, 0}, {1, 1}};
        LaunchInterceptor interceptor = new LaunchInterceptor(minParameters, 2, twoPoints, minLCM, minPUV);
        Assert.assertFalse(null, interceptor.determineLIC3());

        LaunchInterceptor.Parameters invalidAreaParameters = new LaunchInterceptor.Parameters(1, 1, 0.1, -0.1, 0.5, 2.0, 0.05, 0.5, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        double[][] threePoints = {{0, 0}, {1, 0}, {0, 1}};
        interceptor = new LaunchInterceptor(invalidAreaParameters, 3, threePoints, minLCM, minPUV);
        Assert.assertFalse(null, interceptor.determineLIC3());

        interceptor = new LaunchInterceptor(minParameters, 3, threePoints, minLCM, minPUV);
        Assert.assertTrue(null, interceptor.determineLIC3());

        LaunchInterceptor.Parameters largeAreaParameters = new LaunchInterceptor.Parameters(1, 1, 0.1, 5, 0.5, 2.0, 0.05, 0.5, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        interceptor = new LaunchInterceptor(largeAreaParameters, 3, threePoints, minLCM, minPUV);
        Assert.assertFalse(null, interceptor.determineLIC3());
    }

    @Test

    public void LIC09TestInvalidParameters() {
        // Define the invalid parameters
        double[][] points = {
                {1.0, 2.0},  // Point A
                {2.0, 3.0},  // Point B
                {3.0, 1.0},  // Point C
                {4.0, 4.0},  // Point D
                {5.0, 0.0},  // Point E
                {6.0, 5.0},  // Point F
                {7.0, 3.0},  // Point G
                {8.0, 2.5},  // Point H
                {9.0, 1.5},  // Point I
                {10.0, 4.0}  // Point J
        };

        int numPointsValid = 10;

        LaunchInterceptor.Parameters invalidParCPTS =
                new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5,
                        3, 2, 1, 1, 1, 0, 0, 1, 1, 1, 1);
        // Test with invalid C_PTS
        LaunchInterceptor interceptorInvalidCPTS = new LaunchInterceptor(invalidParCPTS, numPointsValid, points, minLCM, minPUV);
        Assert.assertFalse(interceptorInvalidCPTS.determineLIC9());

        // Define invalid D_PTS
        LaunchInterceptor.Parameters invalidParDPTS =
                new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5,
                        3, 2, 1, 1, 1, 1, 0, 1, 1, 1, 1);

        // Test with invalid D_PTS
        LaunchInterceptor interceptorInvalidDPTS = new LaunchInterceptor(invalidParDPTS, numPointsValid, points, minLCM, minPUV);
        Assert.assertFalse(interceptorInvalidDPTS.determineLIC9());

        // Define parameters with invalid C_PTS and D_PTS combined
        LaunchInterceptor.Parameters invalidParCPTSDPTS =
                new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5,
                        3, 2, 1, 1, 1, 10, 10, 1, 1, 1, 1); // C_PTS + D_PTS too large

        // Test with invalid combined parameters
        LaunchInterceptor interceptorInvalidCPTSDPTS = new LaunchInterceptor(invalidParCPTSDPTS, numPointsValid, points, minLCM, minPUV);
        Assert.assertFalse(interceptorInvalidCPTSDPTS.determineLIC9());
    }

    @Test
    public void LIC09AngleTest() {
        // Define the points, including cases for 0 degrees (undefined) and negative coordinates
        double[][] points = {
                {1.0, 1.0},    // Point 0
                {0.0, 0.0},    // Point 1
                {1.0, 0.0},    // Point 2
                {-1.0, -1.0},  // Point 3
                {0.0, -1.0},   // Point 4
                {-1.0, 0.0},   // Point 5
                {2.0, 0.0},    // Point 6
                {7.0, 3.0},    // Point 7
                {8.0, 2.5},    // Point 8
                {9.0, 1.5}     // Point 9
        };

        // Define parameters
        LaunchInterceptor.Parameters parameters = new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0,
                0.05, 0.5, 3, 2, 1, 1, 1, 0, 1, 1, 1, 1, 1);

        // Create the interceptor
        LaunchInterceptor interceptorAngle = new LaunchInterceptor(parameters, 10, points, minLCM, minPUV);
        // Check for 45 degrees
        double expectedAngle45 = Math.PI / 4; // 0 degrees
        double actualAngle45 = interceptorAngle.computeAngle(0, 1, 2);
        Assert.assertEquals(expectedAngle45, actualAngle45, 1e-6);

        // Check for 0 degrees (undefined angle)
        double expectedAngle0 = 0.0; // 0 degrees
        double actualAngle0 = interceptorAngle.computeAngle(2, 1, 6);
        Assert.assertEquals(expectedAngle0, actualAngle0, 1e-6);

        // Check for negative coordinates (180 degrees)
        double expectedAngle180 = Math.PI; // 180 degrees
        double actualAngle180 = interceptorAngle.computeAngle(5, 1, 2);
        Assert.assertEquals(expectedAngle180, actualAngle180, 1e-6);

        // Check for 270 degrees (-90 degrees)
        double expectedAngle270 = Math.PI / 2; // 270 degrees
        double actualAngle270 = interceptorAngle.computeAngle(4, 1, 2);
        Assert.assertEquals(expectedAngle270, actualAngle270, 1e-6);
    }


    @Test
    public void LIC09TestGeneral() {
        // Test case 1: Should return true
        double[][] pointsTrue = {
                {0, 0},   // Point A
                {1, 1},   // Point B
                {2, 0},   // Point C
                {3, 3},   // Additional points
                {4, 4},
                {5, 5}
        };

        LaunchInterceptor.Parameters parametersTrue = new LaunchInterceptor.Parameters(2, 1, 0.1, 0.1, 0.5, 2.0,
                0.05, 0.5, 3, 2, 1, 1, 1, 0, 1, 1, 1, 1, 1);
        LaunchInterceptor interceptorTrue = new LaunchInterceptor(parametersTrue, pointsTrue.length, pointsTrue, minLCM, minPUV);

        Assert.assertTrue(interceptorTrue.determineLIC9());

        // Test case 2: Should return false
        double[][] pointsFalse = {
                {0, 0},   // Point A
                {1, 1},   // Point B
                {2, 2},   // Point C (collinear with A and B)
                {3, 3},   // Additional points
                {4, 4},
                {5, 5},
                {6, 6}
        };

        LaunchInterceptor.Parameters parametersFalse = new LaunchInterceptor.Parameters(2, 1, 0.1, 0.1, 0.5, 2.0,
                0.05, 0.5, 3, 2, 1, 1, 1, 0, 1, 1, 1, 1, 1);
        LaunchInterceptor interceptorFalse = new LaunchInterceptor(parametersFalse, pointsFalse.length, pointsFalse, minLCM, minPUV);

        Assert.assertFalse(interceptorFalse.determineLIC9());
    }

    public void LIC4Test() {
        /*
         * Should test the following...
         * (1) Q_PTS < 2 --> false (invalid value)
         * (2) Number of points < Q_PTS --> false (numpoints must be larger than Q_PTS)
         * (3) QUADS < 1 --> false (invalid value)
         * (4) Valid input --> true (condition met)
         * (5) Valid input --> false (condition not met)
         */

        LaunchInterceptor.Parameters invalidQPTSParams = new LaunchInterceptor.Parameters(1, 1, 0.1, -0.1, 0.5, 2.0, 0.05, 0.5, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        double[][] threePoints = {{1, 1}, {-1, 1}, {-1, -1}};
        LaunchInterceptor interceptor = new LaunchInterceptor(invalidQPTSParams, 3, threePoints, minLCM, minPUV);
        Assert.assertFalse(null, interceptor.determineLIC4());

        double[][] twoPoints = {{0, 0}, {1, 1}};
        LaunchInterceptor.Parameters invalidNumPointsParams = new LaunchInterceptor.Parameters(1, 1, 0.1, -0.1, 0.5, 2.0, 0.05, 0.5, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        interceptor = new LaunchInterceptor(invalidNumPointsParams, 2, twoPoints, minLCM, minPUV);
        Assert.assertFalse(null, interceptor.determineLIC4());

        LaunchInterceptor.Parameters invalidQUADSParams = new LaunchInterceptor.Parameters(1, 1, 0.1, -0.1, 0.5, 2.0, 0.05, 0.5, 2, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        interceptor = new LaunchInterceptor(invalidQUADSParams, 3, threePoints, minLCM, minPUV);
        Assert.assertFalse(null, interceptor.determineLIC4());

        LaunchInterceptor.Parameters validParams = new LaunchInterceptor.Parameters(1, 1, 0.1, -0.1, 0.5, 2.0, 0.05, 0.5, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        // Q_PTS = 3, QUADS = 2
        interceptor = new LaunchInterceptor(validParams, 3, threePoints, minLCM, minPUV);
        Assert.assertTrue(null, interceptor.determineLIC4());

        double[][] threeClusteredPoints = {{0, 0}, {1, 0}, {0, 1}};
        interceptor = new LaunchInterceptor(validParams, 3, threeClusteredPoints, minLCM, minPUV);
        Assert.assertFalse(null, interceptor.determineLIC4());
    }

    @Test
    public void LIC5Test() {
        /*
         * Should test the following...
         * (1) Number of points < 2 --> false (invalid input)
         * (2) Valid input --> true (condition met)
         * (3) Valid input --> false (condition not met)
         */

//        double[][] onePoint = {{0, 0}};
//        LaunchInterceptor interceptor = new LaunchInterceptor(minParameters, 1, onePoint, minLCM, minPUV);
//        Assert.assertFalse(null, interceptor.determineLIC5());

        double[][] goodTwoPoints = {{1, 1}, {0, 0}};
        LaunchInterceptor interceptor = new LaunchInterceptor(minParameters, 2, goodTwoPoints, minLCM, minPUV);
        Assert.assertTrue(null, interceptor.determineLIC5());

        double[][] badTwoPoints = {{0, 0}, {1, 1}};
        interceptor = new LaunchInterceptor(minParameters, 2, badTwoPoints, minLCM, minPUV);
        Assert.assertFalse(null, interceptor.determineLIC5());
    }

    @Test
    public void LIC6Test() {
        /*
         * Should test the following...
         * (1) Number of points < 3 --> false (not enough points)
         * (2) DIST < 0 --> false (invalid DIST)
         * (3) N_PTS > numPoints --> false (invalid N_PTS)
         * (4) Valid points with valid parameters --> true (there exists a distance > DIST)
         * (5) Valid points with valid parameters --> false (there does not exist a distance > DIST)
         */

        double[][] twoPoints = {{0, 0}, {1, 1}};
        LaunchInterceptor interceptor = new LaunchInterceptor(minParameters, 2, twoPoints, minLCM, minPUV);
        Assert.assertFalse(null, interceptor.determineLIC6());

        LaunchInterceptor.Parameters invalidDistParameters = new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, -0.5, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        double[][] threePoints = {{0, 0}, {1, 0}, {0, 1}};
        interceptor = new LaunchInterceptor(invalidDistParameters, 3, threePoints, minLCM, minPUV);
        Assert.assertFalse(null, interceptor.determineLIC6());

        LaunchInterceptor.Parameters invalidN_PTS = new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5, 3, 2, 4, 1, 1, 1, 1, 1, 1, 1, 1);
        interceptor = new LaunchInterceptor(invalidN_PTS, 3, threePoints, minLCM, minPUV);
        Assert.assertFalse(null, interceptor.determineLIC6());

        double[][] truePoints = {{1, 2}, {4, 1}, {9, 10}};
        LaunchInterceptor.Parameters trueParameters = new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5, 3, 2, 3, 1, 1, 1, 1, 1, 1, 1, 1);
        LaunchInterceptor trueInterceptor = new LaunchInterceptor(trueParameters, 3, truePoints, minLCM, minPUV);
        Assert.assertTrue(null, trueInterceptor.determineLIC6());

        LaunchInterceptor.Parameters largeDistParameters = new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 5.0, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        double[][] falsePoints = {{1, 1}, {2, 2}, {3, 3}};
        LaunchInterceptor falseInterceptor = new LaunchInterceptor(largeDistParameters, 3, falsePoints, minLCM, minPUV);
        Assert.assertFalse(null, falseInterceptor.determineLIC6());
    }

    @Test
    public void testLIC7InvalidInput(){
        LaunchInterceptor interceptTooFew = new LaunchInterceptor(minParameters, minNumPoints, minPoints, minLCM, minPUV);
        Assert.assertFalse(null, interceptTooFew.determineLIC7());

        double[][] points = {{1, 2}, {4, 1}, {9, 10}};
        LaunchInterceptor.Parameters paramSmallK = new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5, 3, 2, 3, 0, 1, 1, 1, 1, 1, 1, 1);
        LaunchInterceptor interceptSmallK = new LaunchInterceptor(paramSmallK, points.length, points, minLCM, minPUV);
        Assert.assertThrows(IllegalArgumentException.class, interceptSmallK::determineLIC7);

        LaunchInterceptor.Parameters paramBigK = new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5, 3, 2, 3, 2, 1, 1, 1, 1, 1, 1, 1);
        LaunchInterceptor interceptBigK = new LaunchInterceptor(paramBigK, points.length, points, minLCM, minPUV);
        Assert.assertThrows(IllegalArgumentException.class, interceptBigK::determineLIC7);

    }

    @Test
    public void testLIC8InvalidInput(){
        LaunchInterceptor.Parameters tooFewPoints = new LaunchInterceptor.Parameters(0, 0, 0, 1, 0,
        0, 20, 0, 0, 0, 0, 2, 0, 0, 0,
        0, 1, 1, 0);
        LaunchInterceptor interceptTooFew = new LaunchInterceptor(tooFewPoints, minNumPoints, minPoints, minLCM, minPUV);
        Assert.assertFalse(null, interceptTooFew.determineLIC8());

        double[][] points = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}};

        LaunchInterceptor.Parameters invalidA = new LaunchInterceptor.Parameters(0, 0, 0, 1, 0,
        0, 20, 0, 0, 0, 0, 2, 0, 1, 0,
        0, 1, 1, 0);
        LaunchInterceptor interceptA = new LaunchInterceptor(invalidA, points.length, points, minLCM, minPUV);
        Assert.assertFalse(null, interceptA.determineLIC8());

        LaunchInterceptor.Parameters invalidB = new LaunchInterceptor.Parameters(0, 0, 0, 1, 0,
        0, 20, 0, 0, 0, 0, 2, 1, 0, 0,
        0, 1, 1, 0);
        LaunchInterceptor interceptB = new LaunchInterceptor(invalidB, points.length, points, minLCM, minPUV);
        Assert.assertFalse(null, interceptB.determineLIC8());

        LaunchInterceptor.Parameters invalidNumPointsToAB = new LaunchInterceptor.Parameters(0, 0, 0, 1, 0,
        0, 20, 0, 0, 0, 0, 2, 2, 2, 0,
        0, 1, 1, 0);
        LaunchInterceptor interceptNumPointsToAB = new LaunchInterceptor(invalidNumPointsToAB, minNumPoints, minPoints, minLCM, minPUV);
        Assert.assertFalse(null, interceptNumPointsToAB.determineLIC8());
    }

    @Test
    public void testLIC8True(){

        double[][] points = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}};

        LaunchInterceptor.Parameters paramTrue = new LaunchInterceptor.Parameters(0, 1, 0, 1, 0,
        0, 20, 0, 0, 0, 0, 2, 1, 1, 0,
        0, 1, 1, 0);
        LaunchInterceptor interceptTrue = new LaunchInterceptor(paramTrue, points.length, points, minLCM, minPUV);
        Assert.assertTrue(null, interceptTrue.determineLIC8());
    }

    @Test
    public void testLIC8False(){

        double[][] points = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}};

        LaunchInterceptor.Parameters paramFalse = new LaunchInterceptor.Parameters(0, 5, 0, 1, 0,
        0, 20, 0, 0, 0, 0, 2, 1, 1, 0,
        0, 1, 1, 0);
        LaunchInterceptor interceptFalse = new LaunchInterceptor(paramFalse, points.length, points, minLCM, minPUV);
        Assert.assertFalse(null, interceptFalse.determineLIC8());
    }

    @Test
    public void testLIC14ValidInput() {
        var param = new LaunchInterceptor.Parameters(0, 0, 0, 1, 0,
                0, 20, 0, 0, 0, 0, 2, 0, 0, 0,
                0, 1, 1, 0);
        var pointCoords = new double[][]{{0, 0}, {0, 1}, {2, 1}, {5, 13}, {1, 3}, {-1, 7}, {7, -1}, {10, 1}};
        var lInterceptor = new LaunchInterceptor(param, 8, pointCoords, minLCM, minPUV);
        Assert.assertTrue(null, lInterceptor.determineLIC14());
    }

    @Test
    public void testLIC14InvalidParameters() {
        var param = new LaunchInterceptor.Parameters(0, 0, 0, 0, 0,
                0, -4, 0, 0, 0, 0, 2, 0, 0, 0,
                0, 0, 0, 0);
        var lInterceptor = new LaunchInterceptor(param, minPoints.length, minPoints, minLCM, minPUV);
        Assert.assertThrows(IllegalArgumentException.class, lInterceptor::determineLIC14);
    }

    @Test
    public void testLIC14InsufficientPoints() {
        var param = new LaunchInterceptor.Parameters(0, 0, 0, 1, 0,
                0, 10, 0, 0, 0, 0, 2, 0, 0, 0,
                0, 0, 0, 0);
        var lInterceptor = new LaunchInterceptor(param, 2, new double[][]{{0, 0}, {0, 0}}, minLCM, minPUV);
        Assert.assertFalse(lInterceptor.determineLIC14());
    }

    @Test
    public void testLIC12ValidInput() {
        var param = new LaunchInterceptor.Parameters(2, 0, 0, 0, 10,
                0, 0, 0, 0, 0, 0, 2, 0, 0, 0,
                0, 0, 0, 0);
        var pointCoords = new double[][]{{0, 0}, {0, 1}, {2, 1}, {5, 1}, {10, 3}, {-1, 7}, {7, -1}, {0, 1}};
        var lInterceptor = new LaunchInterceptor(param, 8, pointCoords, minLCM, minPUV);
        Assert.assertTrue(null, lInterceptor.determineLIC12());
    }

    @Test
    public void testLIC12InvalidParameters() {
        var param = new LaunchInterceptor.Parameters(2, 0, 0, 0, -2,
                0, 0, 0, 0, 0, 0, 2, 0, 0, 0,
                0, 0, 0, 0);
        var lInterceptor = new LaunchInterceptor(param, 2, minPoints, minLCM, minPUV);
        Assert.assertThrows(IllegalArgumentException.class, lInterceptor::determineLIC12);
    }

    @Test
    public void testLIC12InsufficientPoints() {
        var param = new LaunchInterceptor.Parameters(2, 0, 0, 0, 10,
                0, 0, 0, 0, 0, 0, 2, 0, 0, 0,
                0, 0, 0, 0);
        var lInterceptor = new LaunchInterceptor(param, 2, new double[][]{{0, 0}, {0, 0}}, minLCM, minPUV);
        Assert.assertFalse(lInterceptor.determineLIC12());
    }

    @Test
    public void testLIC13ValidInput() {
        var param = new LaunchInterceptor.Parameters(0, 1, 0, 0, 10,
                10, 0, 0, 0, 0, 0, 0, 1, 1, 0,
                0, 0, 0, 0);
        var pointCoords = new double[][]{{0, 0}, {0, 1}, {2, 1}, {5, 1}, {10, 3}, {-1, 7}, {7, -1}, {0, 1}};
        var lInterceptor = new LaunchInterceptor(param, 8, pointCoords, minLCM, minPUV);
        Assert.assertTrue(null, lInterceptor.determineLIC13());
    }

    @Test
    public void testLIC13InvalidParameters() {
        var param = new LaunchInterceptor.Parameters(0, 2, 0, 0, 0,
                -3, 0, 0, 0, 0, 0, 0, 1, 1, 0,
                0, 0, 0, 0);
        var lInterceptor = new LaunchInterceptor(param, 2, minPoints, minLCM, minPUV);
        Assert.assertThrows(IllegalArgumentException.class, lInterceptor::determineLIC13);
    }

    @Test
    public void testLIC13InsufficientPoints() {
        var param = new LaunchInterceptor.Parameters(0, 2, 0, 0, 0,
                10, 0, 0, 0, 0, 0, 0, 1, 1, 0,
                0, 0, 0, 0);
        var lInterceptor = new LaunchInterceptor(param, 2, new double[][]{{0, 0}, {0, 0}}, minLCM, minPUV);
        Assert.assertFalse(lInterceptor.determineLIC13());
    }

    @Test
    public void testLIC10InvalidParameters() {
        // Define the invalid parameters
        double[][] points = {
                {1.0, 2.0},  // Point A
                {2.0, 3.0},  // Point B
                {3.0, 1.0},  // Point C
                {4.0, 4.0},  // Point D
                {5.0, 0.0},  // Point E
                {6.0, 5.0},  // Point F
                {7.0, 3.0},  // Point G
                {8.0, 2.5},  // Point H
                {9.0, 1.5},  // Point I
                {10.0, 4.0}  // Point J
        };

        int numPointsValid = 10;

        LaunchInterceptor.Parameters invalidParEPTS =
                new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5,
                        3, 2, 1, 1, 1, 0, 1, 1, 0, 1, 1);
        // Test with invalid E_PTS
        LaunchInterceptor interceptorInvalidCPTS = new LaunchInterceptor(invalidParEPTS, numPointsValid, points, minLCM, minPUV);
        Assert.assertFalse(interceptorInvalidCPTS.determineLIC10());

        // Define invalid F_PTS
        LaunchInterceptor.Parameters invalidParFPTS =
                new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5,
                        3, 2, 1, 1, 1, 1, 0, 1, 1, 0, 1);

        // Test with invalid D_PTS
        LaunchInterceptor interceptorInvalidDPTS = new LaunchInterceptor(invalidParFPTS, numPointsValid, points, minLCM, minPUV);
        Assert.assertFalse(interceptorInvalidDPTS.determineLIC10());

        // Define parameters with invalid C_PTS and D_PTS combined
        LaunchInterceptor.Parameters invalidParEPTSFPTS =
                new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5,
                        3, 2, 1, 1, 1, 1, 1, 10, 10, 1, 1); // C_PTS + D_PTS too large

        // Test with invalid combined parameters
        LaunchInterceptor interceptorInvalidCPTSDPTS = new LaunchInterceptor(invalidParEPTSFPTS, numPointsValid, points, minLCM, minPUV);
        Assert.assertFalse(interceptorInvalidCPTSDPTS.determineLIC10());
    }

    @Test
    public void testLIC10computeTriangleArea() {
        // Define the points, including cases for 0 degrees (undefined) and negative coordinates
        double[][] points = {
                {1.0, 1.0},    // Point 0
                {0.0, 0.0},    // Point 1
                {1.0, 0.0},    // Point 2
                {-1.0, -1.0},  // Point 3
                {0.0, -1.0},   // Point 4
                {-1.0, 0.0},   // Point 5
                {2.0, 0.0},    // Point 6
                {3.0, 5.0},    // Point 7
                {-2.0, 4.0},    // Point 8
                {-2.0, -3.0}     // Point 9
        };

        // Define parameters
        LaunchInterceptor.Parameters parameters = new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0,
                0.05, 0.5, 3, 2, 1, 1, 1, 0, 1, 1, 1, 1, 1);
        // Create the interceptor
        LaunchInterceptor interceptorArea = new LaunchInterceptor(parameters, 10, points, minLCM, minPUV);
        // Check area for valid points
        Assert.assertEquals(0.5, interceptorArea.computeTriangleArea(0, 1, 2), 1e-6);
        //Check area for valid points (complex points)
        Assert.assertEquals(17.5, interceptorArea.computeTriangleArea(7, 8, 9), 1e-6);
        //Check area for a line (should return 0)
        Assert.assertEquals(0.0, interceptorArea.computeTriangleArea(5, 1, 2), 1e-6);
        //Check area for duplicate points
        Assert.assertEquals(0.0, interceptorArea.computeTriangleArea(1, 1, 0), 1e-6);
    }

    @Test
    public void testLIC10General() {
        // Define the invalid parameters
        double[][] points = {
                {1.0, 2.0},  // Point A
                {2.0, 3.0},  // Point B
                {3.0, 1.0},  // Point C
                {4.0, 4.0},  // Point D
                {5.0, 0.0},  // Point E
                {6.0, 5.0},  // Point F
                {7.0, 3.0},  // Point G
                {8.0, 2.5},  // Point H
                {9.0, 1.5},  // Point I
                {10.0, 4.0}  // Point J
        };
        int numPointsValid = 10;

        //Check for true case (points found with area larger than AREA1)
        LaunchInterceptor.Parameters area1TruePar =
                new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5,
                        3, 2, 1, 1, 1, 0, 1, 1, 1, 1, 1);
        LaunchInterceptor interceptorTrue = new LaunchInterceptor(area1TruePar, points.length, points, minLCM, minPUV);
        Assert.assertTrue(interceptorTrue.determineLIC10());

        //Check for false case (NO points found with area larger than AREA1)
        LaunchInterceptor.Parameters area1FalsePar =
                new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 10, 0.5, 2.0, 0.05, 0.5,
                        3, 2, 1, 1, 1, 0, 1, 1, 1, 1, 1);
        LaunchInterceptor interceptorFalse = new LaunchInterceptor(area1TruePar, points.length, points, minLCM, minPUV);
        Assert.assertTrue(interceptorFalse.determineLIC10());
    }

    @Test
    public void testLIC11InvalidParameters(){
        // Define the invalid parameters
        double[][] points = {
                {1.0, 2.0},  // Point A
                {2.0, 3.0},  // Point B
                {3.0, 1.0},  // Point C
                {4.0, 4.0},  // Point D
                {5.0, 0.0},  // Point E
                {6.0, 5.0},  // Point F
                {7.0, 3.0},  // Point G
                {8.0, 2.5},  // Point H
                {9.0, 1.5},  // Point I
                {10.0, 4.0}  // Point J
        };

        int numPointsValid = 10;

        LaunchInterceptor.Parameters invalidParGPTSSmall =
                new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5,
                        3, 2, 1, 1, 1, 0, 1, 1, 1, 1,0);
        // Test with invalid G_PTS (too small=0)
        LaunchInterceptor interceptorInvalidGPTS1 = new LaunchInterceptor(invalidParGPTSSmall, numPointsValid, points, minLCM, minPUV);
        Assert.assertFalse(interceptorInvalidGPTS1.determineLIC11());

        // Define parameters with invalid G_PTS (too large)
        LaunchInterceptor.Parameters invalidGPTSLarge =
                new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5,
                        3, 2, 1, 1, 1, 1, 1, 1, 1, 1,15);

        // Test with invalid GPTS (too large)
        LaunchInterceptor interceptorInvalidGPTS2 = new LaunchInterceptor(invalidGPTSLarge, numPointsValid, points, minLCM, minPUV);
        Assert.assertFalse(interceptorInvalidGPTS2.determineLIC11());
    }
    @Test
    public void testLIC11General(){
        //Check for true case
        // Define the parameters
        double[][] pointsTrue = {
                {1.0, 2.0},  // Point A
                {2.0, 3.0},  // Point B
                {3.0, 1.0},  // Point C
                {4.0, 4.0},  // Point D
                {5.0, 0.0},  // Point E
                {4.0, 5.0},  // Point F
                {3.0, 3.0},  // Point G
                {2.0, 2.5},  // Point H
                {9.0, 1.5},  // Point I
                {10.0, 4.0}  // Point J
        };
        int numPointsValid = 10;
        LaunchInterceptor.Parameters Par =
                new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5,
                        3, 2, 1, 1, 1, 0, 1, 1, 1, 1,1);

        LaunchInterceptor interceptorTrue = new LaunchInterceptor(Par, numPointsValid, pointsTrue, minLCM, minPUV);
        Assert.assertTrue(interceptorTrue.determineLIC11());

        //Check for false case
        // Define the parameters
        double[][] pointsFalse = {
                {1.0, 2.0},  // Point A
                {2.0, 3.0},  // Point B
                {3.0, 1.0},  // Point C
                {4.0, 4.0},  // Point D
                {5.0, 0.0},  // Point E
                {6.0, 5.0},  // Point F
                {7.0, 3.0},  // Point G
                {8.0, 2.5},  // Point H
                {9.0, 1.5},  // Point I
                {10.0, 4.0}  // Point J
        };

        LaunchInterceptor interceptorFalse = new LaunchInterceptor(Par, numPointsValid, pointsFalse, minLCM, minPUV);
        Assert.assertFalse(interceptorFalse.determineLIC11());
    }

    /**
     * Convert the array with 0 --> NOTUSED, 1 --> ANDD, 2 --> ORR, other --> NOTUSED
     * @param arr the array to convert
     * @return the converted array
     */
    private LaunchInterceptor.Connectors[][] fromIntArray(int[][] arr) {
        var connectArray = new LaunchInterceptor.Connectors[15][15];
        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++)
                if (arr[i][j] == 1)
                    connectArray[i][j] = LaunchInterceptor.Connectors.ANDD;
                else if (arr[i][j] == 2)
                    connectArray[i][j] = LaunchInterceptor.Connectors.ORR;
                else
                    connectArray[i][j] = LaunchInterceptor.Connectors.NOTUSED;
        return connectArray;
    }

    /**
     * Custom method to test if some code does NOT throw any exception
     * @param executable the code to execute
     */
    private static void assertNoExceptionIsThrown(Runnable executable) {
        try {
            executable.run();
        } catch (Exception e) {
            fail(e.getClass().getSimpleName() + " was thrown");
        }
    }
}
