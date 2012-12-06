package org.jreserve.rutil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RUtil {
    
    public static String createArray(double[][] data) {
        int columns = getColumnCount(data);
        StringBuilder sb = new StringBuilder("rbind(\n");
        int size = data.length;
        for(int i=0; i<size; i++) {
            sb.append("  ").append(createVector(data[i], columns));
            if(i < (size-1)) sb.append(',');
            sb.append("\n");
        }
        return sb.append(")\n").toString();
    }
    
    private static int getColumnCount(double[][] data) {
        int count = 0;
        for(double[] row : data) {
            if(row == null) continue;
            if(count < row.length) count = row.length;
        }
        return count;
    }
    
    public static String createVector(double[] values) {
        return createVector(values, values.length);
    }
    
    public static String createVector(double[] values, int length) {
        StringBuilder sb = new StringBuilder("c(");
        int size = values.length;
        
        for(int c=0; c<length; c++) {
            if(c > 0) sb.append(", ");
            if(c >= size || Double.isNaN(values[c])) {
                sb.append("NA");
            } else {
                sb.append(values[c]);
            }
        }
        
        return sb.append(")").toString();
    }
    
    public static String createVector(int[] values) {
        return createVector(values, values.length);
    }
    
    public static String createVector(int[] values, int length) {
        StringBuilder sb = new StringBuilder("c(");
        int size = values.length;
        
        for(int c=0; c<length; c++) {
            if(c > 0) sb.append(", ");
            if(c >= size) {
                sb.append("NA");
            } else {
                sb.append(values[c]);
            }
        }
        
        return sb.append(")").toString();
    }
    
    public static String createVector(boolean[] values) {
        return createVector(values, values.length);
    }
    
    public static String createVector(boolean[] values, int length) {
        StringBuilder sb = new StringBuilder("c(");
        int size = values.length;
        
        for(int c=0; c<length; c++) {
            if(c > 0) sb.append(", ");
            if(c >= size) {
                sb.append("NA");
            } else {
                sb.append(values[c]? "TRUE" : "FALSE");
            }
        }
        
        return sb.append(")").toString();
    }
    
    public static String createVector(String[] values) {
        return createVector(values, values.length);
    }
    
    public static String createVector(String[] values, int length) {
        StringBuilder sb = new StringBuilder("c(");
        int size = values.length;
        
        for(int c=0; c<length; c++) {
            if(c > 0) sb.append(", ");
            if(c >= size || values[c] == null) {
                sb.append("NULL");
            } else {
                sb.append('"').append(values[c]).append('"');
            }
        }
        
        return sb.append(")").toString();
    }
    
    private RUtil() {}
}
