package org.jreserve.factor.core.linkratio.selection;

import org.jreserve.factor.core.singlefactor.SingleFactorData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioData {
    public void setInput(SingleFactorData data);
    
    public int getDevelopmentCount();
    
    public double getLinkRatio(int development);
    
    public double[] getLinkRatios();
    
    public void recalculate();
}
