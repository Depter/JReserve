package org.decsi.jreserve.data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Triangle {
    
    public TriangleType getType();
    
    public DateType getAccidentPeriodType();
    
    public int getAccidentPeriodCount();
    
    public DateType getDevelopmentPeriodType();
    
    public int getDevelopmentPeriodCount();
    
    public double[][] getValues();
}
