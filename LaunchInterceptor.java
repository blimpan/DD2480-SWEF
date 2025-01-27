import java.util.Arrays;

public class LaunchInterceptor {

    public static final double PI = 3.1415926535;

    // Launch control parameters
    private final Parameters parameters;
    // Number of data points
    private final int numPoints;
    // X coordinates of data points
    private final double[] x;
    // Y coordinates of data points
    private final double[] y;
    // Logical Connector Matrix
    private final Connectors[][] LCM;
    // Preliminary Unlocking Vector
    private final boolean[] PUV;
    // Determine if the decide() method has already been called
    private boolean processed;
    // Preliminary Unlocking Matrix
    private boolean[][] PUM;
    // Conditions Met Vector
    private boolean[] CMV;
    // Final Unlocking Vector
    private boolean[] FUV;
    // Decision: Launch or No
    private boolean launch;


    public LaunchInterceptor(Parameters parameters, int numPoints, double[][] pointCoords,
                             Connectors[][] LCM, boolean[] PUV) {
        this.parameters = parameters;
        this.numPoints = numPoints;

        if (pointCoords.length != numPoints)
            throw new IllegalArgumentException("Coordinate array is not of length numPoints");

        this.x = new double[numPoints];
        this.y = new double[numPoints];
        for (int i = 0; i < this.numPoints; i++) {
            this.x[i] = pointCoords[i][0];
            this.y[i] = pointCoords[i][1];
        }

        if (LCM.length != 15)
            throw new IllegalArgumentException("LCM matrix is not of length 15x15");
        if (Arrays.stream(LCM).allMatch(v -> v.length == 15))
            throw new IllegalArgumentException("LCM  matrix is not of length 15x15");

        this.LCM = new Connectors[15][15];
        for (int i = 0; i < 15; i++)
            System.arraycopy(LCM[i], 0, this.LCM[i], 0, 15);

        if (PUV.length != 15)
            throw new IllegalArgumentException("PUV is not of length 15x15");

        this.PUV = new boolean[15];
        System.arraycopy(PUV, 0, this.PUV, 0, 15);

        this.PUM = new boolean[15][15];
        this.CMV = new boolean[15];
        this.FUV = new boolean[15];
        this.processed = false;
        this.launch = false;
    }

    public enum Connectors { 
        NOTUSED(777), ORR(778), ANDD(779);
        private final int value;
        Connectors(int value) { this.value = value; }
        public int getValue() { return value; }
    }
    
    public enum CompType { 
        LT(1111), EQ(1112), GT(1113);
        private final int value;
        CompType(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    /**
     * This record contains every parameter needed to determine a launch situation
     */
    public record Parameters(double LENGTH1, double RADIUS1, double EPSILON, double AREA1,
                                    double LENGTH2, double RADIUS2, double AREA2, double DIST,
                                    int Q_PTS, int QUADS, int K_PTS, int A_PTS, int B_PTS,
                                    int C_PTS, int D_PTS, int E_PTS, int F_PTS, int G_PTS){}

    /**
     * Decides whether a launch has to be done for the inputs fed to the constructor
     * This method will set the values of the launch, CMV, PUM and FUV attributes which are then accessible with getters
     * @return the launch decision (boolean)
     */
    public boolean decide(){
        //TODO call each LIC and populate the PUM, CMV, FUV as well as encode launch decision in STDOUT
        this.processed = true;
        throw new Error("Decide function is not implemented yet");
    }

    //==========LIC RELATED METHODS==========

    /**
     * Determines whether or not there exists at least one set of three consecutive data points that are the vertices of a triangle
     * with area greater than AREA1.
     * @return true or false
     */
    public boolean determineLIC3() {

        if (numPoints < 3) {
            return false; // Not enough points to form a triangle
        }

        for (int i = 2; i < numPoints; i++) {
            // first coordinate = (x[i-2], y[i-2])
            // second coordinate = (x[i-1], y[i-1])
            // third coordinate = (x[i], y[i])

            double triangleArea = 0.5 * Math.abs( x[i-2] * (y[i-1]- y[i]) + x[i-1] * (y[i] - y[i-2]) + x[i] * (y[i-2] - y[i-1]));
            if (triangleArea > parameters.AREA1) {
                return true;
            }
        }

        return false; // If program reaches this point, no such triangle exists
    }

    /**
     * Determines if there exists at least one set of two data points 
     * separated by exactly K_PTS consecutive intervening points 
     * that are a distance greater than LENGTH1 apart
     * @return true or false
     */
    public boolean determineLIC7() {

        //Condition is not met when NUMPOINTS < 3
        if(numPoints<3){
            return false;
        }

        int k = 0;
        double x1, x2, y1, y2;
        for (int i = 0; i < numPoints && k < numPoints; i++) {
            k = i + parameters.K_PTS + 1;
            x1 = x[i];
            y1 = y[i];
            x2 = x[k];
            y2 = y[k];

            double distance = pointsDistance(x1, y1, x2, y2);

            if (doubleCompare(distance, parameters.LENGTH1) == CompType.GT){
                return true; //points found
            }
        }
        return false; //no such points
    }

    /**
     *
     * @return true or false
     */
    public boolean determineLIC12() {
        if (parameters.LENGTH2 < 0)
            throw new IllegalArgumentException("Length2 must be greater than 0");

        //Condition is not met when numPoints < 3
        if (numPoints < 3) {
            return false;
        }

        boolean matchFound = false;
        for (int i = 0; ! matchFound && i < numPoints + parameters.K_PTS + 1; i++) {
            if (pointsDistance(x[i], y[i], x[i + parameters.K_PTS + 1], y[i + parameters.K_PTS + 1])
                    < parameters.LENGTH1) {
                matchFound = true;
            }
        }
        
        if (!matchFound)
            return false;

        matchFound = false;
        for (int i = 0; ! matchFound && i < numPoints + parameters.K_PTS + 1; i++) {
            if (pointsDistance(x[i], y[i], x[i + parameters.K_PTS + 1], y[i + parameters.K_PTS + 1])
                    > parameters.LENGTH2) {
                matchFound = true;
            }
        }
        return matchFound;
    }

    //==========GETTER METHODS==========
    public Parameters getParameters() {
        return parameters;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public double[] getX() {
        return x.clone();
    }

    public double[] getY() {
        return y.clone();
    }

    public Connectors[][] getLCM() {
        var LCMReturn = new Connectors[15][15];
        for (int i = 0; i < 15; i++) {
            System.arraycopy(this.LCM[i], 0, LCMReturn[i], 0, 15);
        }
        return LCMReturn;
    }

    public boolean[] getPUV() {
        return PUV.clone();
    }

    public boolean[][] getPUM() throws IllegalAccessException {
        if (!processed)
            throw new IllegalAccessException("Decision has not been processed yet");
        var PUMReturn = new boolean[15][15];
        for (int i = 0; i < 15; i++) {
            System.arraycopy(this.PUM[i], 0, PUMReturn[i], 0, 15);
        }
        return PUM;
    }

    public boolean[] getCMV() throws IllegalAccessException {
        if (!processed)
            throw new IllegalAccessException("Decision has not been processed yet");
        return CMV.clone();
    }

    public boolean[] getFUV() throws IllegalAccessException {
        if (!processed)
            throw new IllegalAccessException("Decision has not been processed yet");
        return FUV.clone();
    }

    public boolean isLaunch() throws IllegalAccessException {
        if (!processed)
            throw new IllegalAccessException("Decision has not been processed yet");
        return launch;
    }

    //==========HELPER METHODS==========
    private static CompType doubleCompare(double A, double B) {
        // Compares floating point numbers, see Nonfunctional Requirements.

        if (Math.abs(A - B) < 0.000001) {
            return CompType.EQ; // A is approximately equal to B
        } else if (A < B) {
            return CompType.LT; // A is less than B
        } else {
            return CompType.GT; // A is greater than B
        }
    }

    //Calculates distance between two points
    private static double pointsDistance(double x1, double y1, double x2, double y2){
        double distance = Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2));
        return distance;
    }

}