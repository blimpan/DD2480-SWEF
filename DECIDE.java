import java.awt.geom.*;

public class DECIDE {

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

    public boolean[] vector = new boolean[15];
    
    public enum CompType { 
        LT(1111), EQ(1112), GT(1113);
        private final int value;
        CompType(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    // INPUTS TO THE DECIDE() FUNCTION
    
    public static class Parameters {
        public double LENGTH1, RADIUS1, EPSILON, AREA1, LENGTH2, RADIUS2, AREA2;
        public int QPTS, QUADS, NPTS, KPTS, APTS, BPTS, CPTS, DPTS, EPTS, FPTS, GPTS;
        public double DIST;
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

    //Calculates distance between a point and a line
    public double pointLineDistance(double a, double b, double c, double x, double y){
        double distance = abs(a*x + b*y + c)/sqrt(Math.pow(a,2) + Math.pow(b,2));
        return distance;
    }

    //Calculates distance between two points
    public double pointsDistance(double x1, double y1, double x2, double y2){
        double distance = sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2))
        return distance;
    }

    /* LIC 6 : 
    - There exists at least one set of N PTS consecutive data points s.t. at least one
    of these lies at a calculated distance > DIST from the line joining the first and last point.
    - If first and last points are identical, calculated distance = distance from coincident point to all other 
    consecutive points. */
    public Boolean determineLIC6() {

        //Condition is not met when NUMPOINTS < 3
        if(numPoints<3){
            return false;
        }

        double a, b, c; //parameters for straight line equation between first and last point
        double k = 0;

        for (int i = 0; i < numPoints && k < numPoints; i++) {
            k = i + NPTS - 1;
            for (int j = i + 1; j < k; j++) {

                //special case when first and last coordinate is the same
                if (x[i] == x[k] && y[i] == y[k]) {
                    distance = pointsDistance(x[i], y[i], x[j], y[j]);
                    if (distance > DIST) {
                        return true;
                    }
                } else {
                    a = (y[k]-y[i])/(x[k]-x[i]);
                    b = (x[i]-x[k])/(x[k]-x[i]);
                    c = (y[i]*x[k] - y[k]*x[i])/(x[k]-x[i]);

                    distance = pointLineDistance(a,b,c,x[j],y[j]);
                    if (distance > DIST){
                        return true;
                    } 
                }
            }
        }
        return false; //no such set of points found
    }

}