
public class LaunchInterceptor {

    public static final double PI = 3.1415926535;

    public enum Connectors { 
        NOTUSED(777), ORR(778), ANDD(779);
        private final int value;
        Connectors(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    public double[] coordinate = new double[100];

    public Connectors[][] connectorMatrix = new Connectors[15][15];
    
    public boolean[][] boolMatrix = new boolean[15][15];

    public boolean[] PUV = new boolean[15];
    
    public enum CompType { 
        LT(1111), EQ(1112), GT(1113);
        private final int value;
        CompType(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    // INPUTS TO THE DECIDE() FUNCTION
    
    public static class Parameters {
        public double LENGTH1, RADIUS1, EPSILON, AREA1, LENGTH2, RADIUS2, AREA2;
        public int Q_PTS, QUADS, N_PTS, K_PTS, A_PTS, B_PTS, C_PTS, D_PTS, E_PTS, F_PTS, G_PTS;
        public double DIST;

        public void setAllParameters(double LENGTH1, double RADIUS1, double EPSILON, double AREA1, int Q_PTS, int QUADS, double DIST, int N_PTS, int K_PTS, int A_PTS, int B_PTS, int C_PTS, int D_PTS, int E_PTS, int F_PTS, int G_PTS, double LENGTH2, double RADIUS2, double AREA2) {
            this.LENGTH1 = LENGTH1;
            this.RADIUS1 = RADIUS1;
            this.EPSILON = EPSILON;
            this.AREA1 = AREA1;
            this.LENGTH2 = LENGTH2;
            this.RADIUS2 = RADIUS2;
            this.AREA2 = AREA2;
            this.Q_PTS = Q_PTS;
            this.QUADS = QUADS;
            this.N_PTS = N_PTS;
            this.K_PTS = K_PTS;
            this.A_PTS = A_PTS;
            this.B_PTS = B_PTS;
            this.C_PTS = C_PTS;
            this.D_PTS = D_PTS;
            this.E_PTS = E_PTS;
            this.F_PTS = F_PTS;
            this.G_PTS = G_PTS;
            this.DIST = DIST;
        }
    }
    
    // GLOBAL VARIABLE DECLARATIONS 
    
    public Parameters parameters = new Parameters();
    public static Parameters parameters2 = new Parameters();


    // X coordinates of data points
    public double[] x = new double[100];
    public static double[] x2 = new double[100];

    // Y coordinates of data points
    public double[] y = new double[100];
    public static double[] y2 = new double[100];

    // Number of data points
    public int numPoints;
    public static int numPoints2;

    // Logical Connector Matrix
    public Connectors[][] LCM = new Connectors[15][15];
    public static Connectors[][] LCM2 = new Connectors[15][15];

    // Preliminary Unlocking Matrix
    public boolean[][] PUM = new boolean[15][15];
    public static boolean[][] PUM2 = new boolean[15][15];

    // Conditions Met Vector
    public boolean[] CMV = new boolean[15];
    public static boolean[] CMV2 = new boolean[15];

    // Final Unlocking Vector
    public boolean[] FUV = new boolean[15];
    public static boolean[] FUV2 = new boolean[15];

    // Decision: Launch or No
    public boolean launch;
    public static boolean launch2;

    public static CompType doubleCompare(double A, double B) {
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
    public double pointsDistance(double x1, double y1, double x2, double y2){
        double distance = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
        return distance;
    }
    
    /**
     * Determines whether or not there exists at least one set of two consecutive data points that are 
     * less than LENGTH1 apart
     * @return true or false
     */
    public Boolean determineLIC0() {

        if (numPoints < 2) {
            return false; // Not enough points 
        }

        for (int i = 1; i < numPoints; i++) {
            
            // first coordinate = (x[i-1], y[i-1])
            // second coordinate = (x[i], y[i])
            double distance = pointsDistance(x[i-1], y[i-1],x[i], y[i]);
            if (distance > parameters.AREA1) {
                return true;
            }
        }

        return false; // No such points exists
    }


    /**
     * Determines whether or not there exists at least one set of three consecutive data points that are the vertices of a triangle
     * with area greater than AREA1.
     * @return true or false
     */
    public Boolean determineLIC3() {

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
    public Boolean determineLIC7() {

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

    public void setInputVariables(int inNumPoints, double[][] inPoints, Parameters inParameters, Connectors[][] inLCM, boolean[] inPUV) {
        this.numPoints = inNumPoints;
        this.x = new double[inNumPoints];
        this.y = new double[inNumPoints];
        for (int i = 0; i < inNumPoints; i++) {
            this.x[i] = inPoints[i][0];
            this.y[i] = inPoints[i][1];
        }
        this.parameters = inParameters;
        this.LCM = inLCM;
        this.PUV = inPUV;
    }
}