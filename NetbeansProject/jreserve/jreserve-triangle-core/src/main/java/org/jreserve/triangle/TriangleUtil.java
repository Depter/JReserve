package org.jreserve.triangle;

/**
 *
 * @author Peter Decsi
 */
public class TriangleUtil {

    public static void cummulate(double[][] triangle) {
        for(double[] row : triangle)
            cummulate(row);
    }

    private static void cummulate(double[] row) {
        for(int i=1, size=row.length; i<size; i++) {
            double prev = row[i-1];
            double current = row[i];
            if(!Double.isNaN(prev) && !Double.isNaN(current))
                row[i] = prev+current;
        }
    }
    
    public static void deCummulate(double[][] triangle) {
        for(double[] row : triangle)
            deCummulate(row);
    }

    private static void deCummulate(double[] row) {
        for(int i=row.length-1; i>0; i--) {
            double prev = row[i-1];
            double current = row[i];
            if(!Double.isNaN(prev) && !Double.isNaN(current))
                row[i] = current - prev;
        }
    }
}
