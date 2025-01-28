import org.junit.*;

public class LaunchInterceptorTEST {
    
    // Variables for minimum test input
    private int minNumPoints = 2;
    private double[][] minPoints = {{0, 0}, {1, 1}};

    private LaunchInterceptor.Parameters minParameters = new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 0.5, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1,1);
    private LaunchInterceptor.Connectors[][] minLCM = new LaunchInterceptor.Connectors[15][15];
    private boolean[] minPUV = {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true};

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
    public void constructorTest() {

        LaunchInterceptor interceptor = new LaunchInterceptor(minParameters, minNumPoints, minPoints, minLCM, minPUV);

        // should be moved to another test with better naming (tests the getters functionality)
        Assert.assertThrows(IllegalAccessException.class, interceptor::getPUM);
        Assert.assertThrows(IllegalAccessException.class, interceptor::getFUV);
        Assert.assertThrows(IllegalAccessException.class, interceptor::getCMV);
        Assert.assertThrows(IllegalAccessException.class, interceptor::isLaunch);

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

        Assert.assertEquals(minNumPoints, interceptor.getX().length);
        Assert.assertEquals(minNumPoints, interceptor.getY().length);  
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

        double[][] onePoint = {{0, 0}};
        LaunchInterceptor interceptor = new LaunchInterceptor(minParameters, 1, onePoint, minLCM, minPUV);
        Assert.assertFalse(null, interceptor.determineLIC5());

        double[][] goodTwoPoints = {{1, 1}, {0, 0}};
        interceptor = new LaunchInterceptor(minParameters, 2, goodTwoPoints, minLCM, minPUV);
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
        LaunchInterceptor trueInterceptor = new LaunchInterceptor(minParameters, 2, truePoints, minLCM, minPUV);
        Assert.assertTrue(null, trueInterceptor.determineLIC6());

        LaunchInterceptor.Parameters largeDistParameters = new LaunchInterceptor.Parameters(1.0, 1.0, 0.1, 0.1, 0.5, 2.0, 0.05, 5.0, 3, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        double[][] falsePoints = {{1, 1}, {2, 2}, {3, 3}};
        LaunchInterceptor falseInterceptor = new LaunchInterceptor(largeDistParameters, 2, falsePoints, minLCM, minPUV);
        Assert.assertFalse(null, falseInterceptor.determineLIC6());
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
        var lInterceptor = new LaunchInterceptor(param, 1, new double[][]{{0, 0}}, minLCM, minPUV);
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
        var lInterceptor = new LaunchInterceptor(param, 1, new double[][]{{0, 0}}, minLCM, minPUV);
        Assert.assertFalse(lInterceptor.determineLIC12());
    }

}

