package org.jreserve.triangle.smoothing.exponential.factory;

import org.jreserve.triangle.smoothing.Smoothing;
import org.jreserve.triangle.smoothing.exponential.ExponentialSmoothing;

/**
 *
 * @author Peter Decsi
 */
public class ExponentialSmoothingFactory {

    public static Smoothing createExponentialSmoothing(int order, String name, double alpha) {
        return new ExponentialSmoothing(order, name, alpha);
    }
    
    private ExponentialSmoothingFactory() {}
}
