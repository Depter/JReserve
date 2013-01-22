package org.jreserve.factor.core.linkratio.smoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSmoothingFunction {
    
    public void setInput(double[] linkRatios);
    
    public double getLinkRatio(int development);
}
