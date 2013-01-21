package org.jreserve.triangle.util;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.entities.TriangleCell;

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
    
    public static double getValue(TriangleCell coordinate, TriangularData data) {
        int accident = coordinate.getAccident();
        int development = coordinate.getDevelopment();
        return data.getValue(accident, development);
    }
    
    public static double getValue(TriangleCell coordinate, double[][] data) {
        return getValue(coordinate.getAccident(), coordinate.getDevelopment(), data);
    }
    
    public static double getValue(int accident, int development, double[][] data) {
        if(accident < data.length)
            if(development < data[accident].length)
                return data[accident][development];
        return Double.NaN;
    }
    
    public static double[] getValues(List<TriangleCell> coordiantes, TriangularData data) {
        int size = coordiantes.size();
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = getValue(coordiantes.get(i), data);
        return result;
    }
    
    public static double[] getCellValues(List<? extends TriangleCell.Provider> coordiantes, TriangularData data) {
        int size = coordiantes.size();
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = getValue(coordiantes.get(i).getTriangleCell(), data);
        return result;
    }

    public static double[] getValues(List<TriangleCell> coordiantes, double[][] data) {
        int size = coordiantes.size();
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = getValue(coordiantes.get(i), data);
        return result;
    }
    
    public static <T extends TriangleCell.Provider> List<T> filterValues(List<T> values, TriangleCell cell) {
        return filterValues(values, cell.getAccident(), cell.getDevelopment());
    } 
    
    public static <T extends TriangleCell.Provider> List<T> filterValues(List<T> values, int accident, int development) {
        List<T> result = new ArrayList<T>();
        for(T value : values)
            if(value != null && withinCell(value.getTriangleCell(), accident, development))
                result.add(value);
        return result;
    } 
    
    private static boolean withinCell(TriangleCell cell, int accident, int development) {
        return cell!=null && 
               accident == cell.getAccident() && 
               development==cell.getDevelopment();
    }
}