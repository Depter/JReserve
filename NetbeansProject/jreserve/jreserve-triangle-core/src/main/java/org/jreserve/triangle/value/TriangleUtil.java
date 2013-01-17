package org.jreserve.triangle.value;

import java.util.List;
import org.jreserve.triangle.TriangularData;

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
    
    public static double[] getValues(List<TriangleCoordiante> coordiantes, TriangularData data) {
        int size = coordiantes.size();
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = coordiantes.get(i).getValue(data);
        return result;
    }

    public static double[] getValues(List<TriangleCoordiante> coordiantes, double[][] data) {
        int size = coordiantes.size();
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = coordiantes.get(i).getValue(data);
        return result;
    }
}
