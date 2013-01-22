package org.jreserve.factor.core;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioFunction {

    public double getLinkRatio(int development, double[][] cummulatedSource, double[][] factors);
}
