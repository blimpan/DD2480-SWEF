
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
}

