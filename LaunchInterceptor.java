
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