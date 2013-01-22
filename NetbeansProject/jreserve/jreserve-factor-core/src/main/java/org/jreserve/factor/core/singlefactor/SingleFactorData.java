package org.jreserve.factor.core.singlefactor;

import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface SingleFactorData {
    public int getAccidentCount();
    public int getDevelopmentCount();
    
    public double getCummulatedInput(int accident, int development);
    public double getFactor(int accident, int development);
    
    public double[][] getCummulatedInput();
    public double[][] getFactors();
    
    public void addChangeListener(ChangeListener listener);
    public void removeChangeListener(ChangeListener listener);
}
