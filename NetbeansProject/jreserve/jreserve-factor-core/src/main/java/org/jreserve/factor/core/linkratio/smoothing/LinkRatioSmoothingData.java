package org.jreserve.factor.core.linkratio.smoothing;

import org.jreserve.factor.core.linkratio.selection.LinkRatioData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSmoothingData {
    
    public void setInput(LinkRatioData selection);
    
    public int getDevelopmentCount();
    
    public double getLinkRatio(int development);
    
    public double[] getLinkRatios();
    
    public void recalculate();
}
