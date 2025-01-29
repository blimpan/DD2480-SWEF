import java.util.Arrays;

public class Main {
    /**
     * Main entry to the decision program
     * User can edit the
     * @param args ignored
     */
    public static void main(String[] args) {

        var launchParameters = getLaunchParameters();

        var pointCoordinates = new double[][]{
                {0, 0}, {-1, 4}, {-2, 0}, {5, 10}, {1, 1}, {-6, 15}, {0, 1}, {-15, -2}, {1, 0}, {14, -20}, {-1, 0},
                {0, -1}, {0, 0}, {-1, -1}, {0, 2}, {1, 0}, {2, 1}, {5, 16}, {-20, 5}, {-5, -10}, {12, -7}
        };

        // 1 for ANDD, 2 for ORR, other for NOT_USED
        var decideLCM = fromIntArray(new int[][]{
            //   0  1  2  3  4  5  6  7  8  9 10 11 12 13 14
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},      //00
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},      //01
                {1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},      //02
                {1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},      //03
                {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},      //04
                {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},      //05
                {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},      //06
                {1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1},      //07
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1},      //08
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1},      //09
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1},      //10
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1},      //11
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1},      //12
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},      //13
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0}       //14
        });

        var decidePUV = new boolean[]{true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};

        var lInterceptor = new LaunchInterceptor(
                launchParameters, pointCoordinates.length, pointCoordinates, decideLCM, decidePUV
        );

        System.out.print("\t>> Interceptor decision:\n\t");
        lInterceptor.decide();

        var PUM = lInterceptor.getPUM();

        System.out.println("\n\t>> Primary Unlocking Matrix:");
        System.out.println("  | 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14");
        for (int i = 0; i < 15; i++) {
            System.out.printf("%2d|", i);
            for (int j = 0; j < 15; j++) {
                if (PUM[i][j])
                    System.out.print(" \u2588 ");
                else
                    System.out.print("   ");
            }
            System.out.println();
        }

        var FUV = lInterceptor.getFUV();

        System.out.println("\n\t>> Final Unlocking Vector:");
        System.out.print("| 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14\n|");
        for (int i = 0; i < 15; i++) {
            if (FUV[i])
                System.out.print(" \u2588 ");
            else
                System.out.print("   ");
        }
    }

    /**
     * To generate launch parameters using all the variable defined in the method
     * @return the corresponding launch parameters
     */
    private static LaunchInterceptor.Parameters getLaunchParameters() {
        var LENGTH1 = 1; var RADIUS1 = 1; var AREA1 = 1; var EPSILON = 0.2;
        var LENGTH2 = 10; var RADIUS2 = 10;var AREA2 = 10; var DIST = 4;

        var Q_PTS = 6; var QUADS = 2; var N_PTS = 3; var K_PTS = 1;
        var A_PTS = 1; var B_PTS = 1; var C_PTS = 1; var D_PTS = 1;
        var E_PTS = 1; var F_PTS = 1; var G_PTS = 1;

        return new LaunchInterceptor.Parameters(
                LENGTH1, RADIUS1, EPSILON, AREA1, LENGTH2, RADIUS2, AREA2, DIST, Q_PTS, QUADS,
                N_PTS, K_PTS, A_PTS, B_PTS, C_PTS, D_PTS, E_PTS, F_PTS, G_PTS
        );
    }

    /**
     * Convert the array with 0 --> NOTUSED, 1 --> ANDD, 2 --> ORR, other --> NOTUSED
     * @param arr the array to convert
     * @return the converted array
     */
    private static LaunchInterceptor.Connectors[][] fromIntArray(int[][] arr) {
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
}
