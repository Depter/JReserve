package org.jreserve.estimates.factors.factorestimator;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface FactorEstimator {

    public double[] getFactors(double[][] input, double[][] factors);
    
    public @interface Registration {
        
        public int position() default Integer.MAX_VALUE;
        
        public String displayName();
    }    
}
