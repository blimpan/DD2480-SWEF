import java.util.Arrays;

import org.junit.*;

public class LaunchInterceptorTEST {
    
    // Variables for minimum test input
    private int minNumPoints = 2;
    private double[][] minPoints = {{0, 0}, {1, 1}};
    private LaunchInterceptor.Parameters minParameters = new LaunchInterceptor.Parameters();
    private LaunchInterceptor.Connectors[][] minLCM = new LaunchInterceptor.Connectors[15][15];
    private boolean[] minPUV = {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true};

    @Before
    public void setUp() {
        minParameters.setAllParameters(1, 1, 0.1, 0.1, 3, 2, 0.5, 3, 1, 1, 1, 1, 1, 1, 1, 1, 0.5, 2.0, 0.05);
    
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

        LaunchInterceptor interceptor = new LaunchInterceptor();
        interceptor.setInputVariables(minNumPoints, minPoints, minParameters, minLCM, minPUV);
        
        Assert.assertEquals(minNumPoints, interceptor.x.length);
        Assert.assertEquals(minNumPoints, interceptor.y.length);
       
    }

}

