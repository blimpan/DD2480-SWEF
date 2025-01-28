import java.util.HashSet;
import java.util.Set;
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
        if (!Arrays.stream(LCM).allMatch(v -> v.length == 15))
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
                                    int Q_PTS, int QUADS, int N_PTS, int K_PTS, int A_PTS, int B_PTS,
                                    int C_PTS, int D_PTS, int E_PTS, int F_PTS, int G_PTS){}

    /**
     * Decides whether a launch has to be done for the inputs fed to the constructor
     * This method will set the values of the launch, CMV, PUM and FUV attributes which are then accessible with getters
     * @return the launch decision (boolean)
     */
    public boolean decide(){
        
        this.processed = true;
        throw new Error("Decide function is not implemented yet");
    }


    //==========LIC RELATED METHODS==========

    /**
     * Determines whether or not there exists at least one set of two consecutive data points that are
     * less than LENGTH1 apart
     * @return true or false
     */
    public boolean determineLIC0() {

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
     * Determines whether or not there exists at least one set of three consecutive data points that can not
     * be contained in a circle of radius RADIUS1
     * @return true or false
     */
    public boolean determineLIC1() {

        if (numPoints < 3) {
            return false; // Not enough points to test
        }

        for (int i = 2; i < numPoints; i++) {
            // first coordinate = (x[i-2], y[i-2])
            // second coordinate = (x[i-1], y[i-1])
            // third coordinate = (x[i], y[i])
            double a = pointsDistance(x[i-2],y[i-2],x[i-1],y[i-1]);
            double b = pointsDistance(x[i-1],y[i-1],x[i],y[i]);
            double c = pointsDistance(x[i-2],y[i-2],x[i],y[i]);

            //Caluclating the area using Heron's formula
            double semiPerimeter = (a+b+c)/2;
            double area = Math.sqrt(semiPerimeter * (semiPerimeter-a)*(semiPerimeter-b)*(semiPerimeter-c));

            if (area == 0) {
                //break;
            }

            //Calculating the circumradius for a triangle
            double circumRadius = (a*b*c)/(4*a);
            if (circumRadius > parameters.RADIUS1) {
                return true;
            }
        }

        return false; // No consecutive points do not fulfuill the criteria
    }


/**
 * Determines whether or not there exists at least one set of three consecutive data points
 * that form an angle < (PI − EPSILON) or angle > (PI + EPSILON).
 * @return true if such a set exists, false otherwise.
 */
public boolean determineLIC2() {
    if (numPoints < 3) {
        return false; // Not enough points to form an angle
    }

    for (int i = 2; i < numPoints; i++) {
        // First point: (x[i-2], y[i-2])
        // Second point: (x[i-1], y[i-1])
        // Third point: (x[i], y[i])

        // Vector A (from second to first point): (x2 - x1, y2 - y1)
        double vectorAx = x[i-1] - x[i-2];
        double vectorAy = y[i-1] - y[i-2];

        // Vector B (from second to third point): (x3 - x2, y3 - y2)
        double vectorBx = x[i] - x[i-1];
        double vectorBy = y[i] - y[i-1];

        // Dot product of A and B
        double dotProduct = vectorAx * vectorBx + vectorAy * vectorBy;

        // Magnitudes of A and B
        double magnitudeA = Math.sqrt(vectorAx * vectorAx + vectorAy * vectorAy);
        double magnitudeB = Math.sqrt(vectorBx * vectorBx + vectorBy * vectorBy);

        // Handle edge cases: If either magnitude is zero, skip this triplet
        if (magnitudeA == 0 || magnitudeB == 0) {
            continue;
        }

        // Calculate the angle in radians
        double angle = Math.acos(dotProduct / (magnitudeA * magnitudeB));

        // Check if the angle is < (PI - EPSILON) or > (PI + EPSILON)
        if (angle < Math.PI - parameters.EPSILON || angle > Math.PI + parameters.EPSILON) {
            return true;
        }
    }

    return false; // No such angle found
}


    /**
     * Determines whether or not there exists at least one set of three consecutive data points that are the vertices of a triangle
     * with area greater than AREA1.
     * @return true or false
     */
    public boolean determineLIC3() {

        if (numPoints < 3) {
            return false; // Not enough points to form a triangle
        }
        if (parameters.AREA1 < 0) {
            return false; // Invalid area
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
     * Determines whether or not there exists at least one set of Q_PTS points that lie in more than QUADS different quadrants.
     * @return true or false
     */
    public Boolean determineLIC4() {
        if (2 > parameters.Q_PTS || parameters.Q_PTS > numPoints) {
            return false; // Invalid conditions
        }
        if (1 > parameters.QUADS || parameters.QUADS > 3) {
            return false; // Invalid conditions
        }

        for (int i = 0; i <= numPoints - parameters.Q_PTS; i++) {
            Set<Integer> quadrants = new HashSet<>();

            for (int j = 0; j < parameters.Q_PTS; j++) {
                double xPos = x[i + j];
                double yPos = y[i + j];

                // Determine the quadrant of the point
                if (xPos >= 0 && yPos >= 0) {
                    quadrants.add(1); // Quadrant I
                } else if (xPos < 0 && yPos >= 0) {
                    quadrants.add(2); // Quadrant II
                } else if (xPos <= 0 && yPos < 0) {
                    quadrants.add(3); // Quadrant III
                } else if (xPos > 0 && yPos < 0) {
                    quadrants.add(4); // Quadrant IV
                }

                // Break early if enough quadrants are found
                if (quadrants.size() > parameters.QUADS) {
                    return true;
                }
            }
        }

    return false; // No set meets the condition
    }

    /**
     * Determines if there exists at least one set of two consecutive data points, (X[i], Y[i]) and (X[j], Y[j]),
     * such that X[j] - X[i] < 0 where i=j-1.
     * @return true or false
     */
    public boolean determineLIC5() {

        if (this.numPoints < 2) {
            return false; // Not enough points
        }

        for (int j = 1; j < numPoints; j++) {
            if (x[j] - x[j - 1] < 0) {
                return true;
            }
        }
        return false; // If program reaches this point, no such triangle exists
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
        double distance;
        int k = 0;

        for (int i = 0; i < numPoints && k < numPoints; i++) {
            k = i + parameters.N_PTS - 1;
            for (int j = i + 1; j < k; j++) {

                //special case when first and last coordinate is the same
                if (x[i] == x[k] && y[i] == y[k]) {
                    distance = pointsDistance(x[i], y[i], x[j], y[j]);
                    if (distance > parameters.DIST) {
                        return true;
                    }
                } else {
                    a = y[k]-y[i];
                    b = x[i]-x[k];
                    c = a*x[i] + b*y[i];

                    distance = pointLineDistance(a,b,c,x[j],y[j]);
                    if (distance > parameters.DIST){
                        return true;
                    }
                }
            }
        }
        return false; //no such set of points found
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
     * Determines if there exists a set of 3 points (A,B,C) which meets the following conditions:
     * 1. there are C PTS in bewteen A and B
     * 2. there are D PTS in between B and C
     * 3. the angle is defined
     * 4. angle < (PI−EPSILON) or angle > (PI+EPSILON)
     * 5. NUMPOINTS >= 5 
     * 6. C PTS >= 1
     * 7. D PTS >= 1
     * 8. C PTS+D PTS ≤ NUMPOINTS−3 (to ensure the size of data is enough)
     * @return true or flase
     */
    public boolean determineLIC9(){
        // Check the requirments 5-8 for the parameters
        int C_PTS = parameters.C_PTS;
        int D_PTS = parameters.D_PTS;
        boolean found = false; // set to true when a set of points are found to meet all requirements
        if(C_PTS <1 || D_PTS<1 || numPoints<5 || C_PTS + D_PTS>numPoints-3 ) return false;
        // Parameters passed requirements, proceed to finding data points
        // Start from first data point to be A
        for (int aIndex=0; aIndex<numPoints; ++aIndex){
            if (aIndex+C_PTS+1>=numPoints)break; //if B is already out of boundary then so is C, therefore quit the loop
            int bIndex = aIndex+C_PTS+1; // reaching here indicates B is in boundary
            if (bIndex+D_PTS+1>=numPoints)break;  //if C is out of boundary, then for the next A, the C also will be out of boundary
            int cIndex = bIndex+D_PTS+1;// reaching here indicates C is in boundary
            //A, B and C are all set
            //Compute the angle using the helper function (implemented in Helper Function Section)
            double angle = computeAngle(aIndex, bIndex, cIndex);
            //Check conditions 3-4
            if(doubleCompare(angle, 0.0) ==CompType.EQ) continue; //if angle is undefined, continue to next set of points
            if(doubleCompare(angle, PI-parameters.EPSILON) == CompType.LT ||
                doubleCompare(angle, PI+parameters.EPSILON) == CompType.GT){
                    found = true; //met conditions 3-4, found the set
                    break; //stop exploring
                }
        }
        return found;
    }

   /** 
     * Determines if there exists a set of 3 points (A, B, C) such that:
     * 1. There are E PTS in between A and B.
     * 2. There are F PTS in between B and C.
     * 3. The points form a triangle with an area greater than AREA1.
     * 4. The condition is not met when NUMPOINTS < 5.
     * 5. E PTS >= 1.
     * 6. F PTS >= 1.
     * 7. E PTS + F PTS ≤ NUMPOINTS - 3 (to ensure sufficient data points).
     * 
     * @return true if at least one valid set of points exists, false otherwise.
     */
    public boolean determineLIC10(){
        int E_PTS = parameters.E_PTS; int F_PTS = parameters.F_PTS;
        boolean found =false; // set to true when a set of points are found to meet all requirements
        //Check requirements 4-7
        if(E_PTS<1 || F_PTS<1 || E_PTS+F_PTS>numPoints-3 || numPoints<5) return false;
        //Loop through each point as A
        for (int aIndex=0; aIndex<numPoints; ++aIndex){
            if (aIndex+E_PTS+1 >=numPoints)break; //if B is already out of boundary then so is C, therefore quit the loop
            int bIndex = aIndex+E_PTS+1; // reaching here indicates B is in boundary
            if (bIndex+F_PTS+1 >=numPoints)break;  //if C is out of boundary, then for the next A, the C also will be out of boundary
            int cIndex = bIndex+F_PTS+1;// reaching here indicates C is in boundary
            //A, B and C are all set
            //Compute the angle using the helper function (implemented in Helper Function Section)
            double area = computeTriangleArea(aIndex, bIndex, cIndex);
            //Check conditions 3-4
            if(doubleCompare(area, 0.0) ==CompType.EQ) continue; //if area is undefined, continue to next set of points
            if(doubleCompare(area, parameters.AREA1) == CompType.GT){
                    found = true; //met conditions 3, found the set
                    break; //stop exploring
                }
        }
        return found;
    }

    /** 
     * Determines if there exists a set of two data points (X[i], Y[i]) and (X[j], Y[j]) such that:
     * 1. There are G PTS in between the two points (A, B).
     * 2. The condition X[j] - X[i] < 0 holds true (where i < j).
     * 3. The condition is not met when NUMPOINTS < 3.
     * 4. G PTS >= 1.
     * 5. G PTS ≤ NUMPOINTS - 2 (to ensure sufficient data points).
     * 
     * @return true if at least one valid set of points exists, false otherwise.
     */
    public boolean determineLIC11(){
        int G_PTS = parameters.G_PTS;
        boolean found = false; //set to true when a set of points are found to meet all requirements
        //Check requirements 3-5
        if(numPoints<3 || G_PTS<1 || G_PTS>numPoints-2) return false;
        //loop through all data points for A
        for (int aIndex=0; aIndex<numPoints; ++aIndex){
            if (aIndex+G_PTS>=numPoints)break; //if B is out of boundary, then the next A will also result in out boundary B
            int bIndex = aIndex+G_PTS+1; // reaching here indicates B is in boundary
            //A, B  are all set
            //Check conditions 2
            if (x[bIndex]-x[aIndex]<0){found = true; break;}
        }
        return found;
    }


    /**
     *
     * @return true or false
     */
    public boolean determineLIC14() {
        if (parameters.AREA2 < 0)
            throw new IllegalArgumentException("Area2 must be greater than 0");

        //Condition is not met when numPoints < 5
        if (numPoints < 5) {
            return false;
        }

        boolean matchFound = false;
        for (int i = 0; ! matchFound && i + parameters.E_PTS + parameters.F_PTS + 2 < numPoints; i++) {
            var area = computeTriangleArea(i, i + parameters.E_PTS + 1, i + parameters.E_PTS + parameters.F_PTS + 1);
            if (area > parameters.AREA1)
                matchFound = true;
        }

        if (!matchFound)
            return false;

        for (int i = 0; i + parameters.E_PTS + parameters.F_PTS + 2 < numPoints; i++) {
            var area = computeTriangleArea(i, i + parameters.E_PTS + 1, i + parameters.E_PTS + parameters.F_PTS + 1);
            if (area < parameters.AREA2)
                return true;
        }
        return false;
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

    //Calculates distance between a point and a line
    public double pointLineDistance(double a, double b, double c, double x, double y){
        double distance = Math.abs(a*x + b*y + c)/Math.sqrt(Math.pow(a,2) + Math.pow(b,2));
        return distance;
    }

    //Calculates distance between two points
    private static double pointsDistance(double x1, double y1, double x2, double y2){
        double distance = Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2));
        return distance;
    }

    /**
     * Calculates the angle given the indexes of three points.
     * 
     * Input: 
     * - int aIndex: the index of the first point (Point A)
     * - int bIndex: the index of the second point (Point B), which is always the vertex of the angle
     * - int cIndex: the index of the third point (Point C)
     * 
     * This function determines the angle formed by the three points, with the vertex at Point B.
     * It uses the coordinates of the points identified by the provided indexes to compute the angle 
     * between the lines connecting Point A to Point B and Point B to Point C.
     * 
     * The function returns the angle in radians
     */
    private double computeAngle(int aIndex, int bIndex, int cIndex){
        // get x, y coordinates of each point
        double x_a = x[aIndex];double y_a = y[aIndex];
        double x_b = x[bIndex];double y_b = y[bIndex];
        double x_c = x[cIndex];double y_c = y[cIndex];
        // Compute vector BA and BC 
        //BA = OA - OB
        double[] vecBA = {x_a-x_b, y_a-y_b};
        //BC = OC - OB
        double[] vecBC = {x_c-x_b, y_c-y_b};
        //dot product of BA and BC
        double dot = vecBA[0] * vecBC[0] + vecBA[1] * vecBC[1];
        //Magnitude of BA and BC
        double magBA = Math.sqrt(vecBA[0]* vecBA[0] +vecBA[1] * vecBA[1]);// |BA| = sqrt(x^2 + y^2)
        double magBC = Math.sqrt(vecBC[0]* vecBC[0] +vecBC[1] * vecBC[1]);
        //Compute cosTheta
        double cosTheta = dot/(magBA*magBC);
        //theta = arccos(cosTheta)
        return Math.acos(cosTheta);
    }

    /**
     * Calculates the area of a triangle formed by three points.
     *
     * Input:
     * - int aIndex: the index of the first point (Point A)
     * - int bIndex: the index of the second point (Point B)
     * - int cIndex: the index of the third point (Point C)
     *
     * This function computes the area of the triangle defined by the three points using the
     * coordinates of the points identified by the provided indexes. The area is calculated
     * using the SHOELACE formula:
     *
     * Area = 0.5 * (x1y2 +x2y3+ x3y1 - x2y1 - x3y2 - x1y3)
     *
     * The function returns the area as a double value.
     */
    double computeTriangleArea (int aIndex, int bIndex, int cIndex){
        // get x, y coordinates of each point
        double x_a = x[aIndex];double y_a = y[aIndex];
        double x_b = x[bIndex];double y_b = y[bIndex];
        double x_c = x[cIndex];double y_c = y[cIndex];
        return  0.5 * Math.abs(x_a*y_b + x_b* y_c + x_c* y_a
                - x_b*y_a - x_c* y_b - x_a* y_c);
    }
}
