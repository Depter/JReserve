package org.jreserve.smoothing;

/**
 *
 * @author Peter Decsi
 */
public interface SmoothMethod {
    
    public int getId();
    
    public String getDisplayName();
    
    public double[] smooth(double[] input);
}
