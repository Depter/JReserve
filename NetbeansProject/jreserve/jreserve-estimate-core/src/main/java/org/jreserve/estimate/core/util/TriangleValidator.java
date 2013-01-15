package org.jreserve.estimate.core.util;

import org.jreserve.triangle.TriangularData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleValidator {
    
    public static boolean isEmpty(TriangularData triangle) {
        int aCount = triangle.getAccidentCount();
        for(int a=0; a<aCount; a++)
            if(!isAccidentEmpty(triangle, a))
                return false;
        return true;
    }
    
    public static boolean isAccidentEmpty(TriangularData triangle, int accident) {
        int dCount = triangle.getDevelopmentCount(accident);
        for(int d=0; d<dCount; d++)
            if(!Double.isNaN(triangle.getValue(accident, d)))
                return false;
        return true;
    }
    
    
    public static boolean isContinuous(TriangularData triangle) {
        int aCount = triangle.getAccidentCount();
        for(int a=0; a<aCount; a++)
            if(!isAccidentContinuous(triangle, a))
                return false;
        return true;
    }
    
    public static boolean isAccidentContinuous(TriangularData triangle, int accident) {
        int dCount = triangle.getDevelopmentCount(accident);
        
        boolean valueBegin = false;
        boolean valueEnd = false;
        for(int d=0; d<dCount; d++) {
            double value = triangle.getValue(accident, d);
            if(Double.isNaN(value)) {
                if(valueBegin && !valueEnd)
                    valueEnd = true;
            } else {
                if(!valueBegin)
                    valueBegin = true;
                else if(valueEnd)
                    return false;
            }
        }
        return true;
    }
    
    private TriangleValidator() {}
}
