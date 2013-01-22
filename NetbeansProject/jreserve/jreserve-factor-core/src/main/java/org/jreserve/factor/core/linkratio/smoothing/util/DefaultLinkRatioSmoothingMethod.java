package org.jreserve.factor.core.linkratio.smoothing.util;

import org.jreserve.factor.core.linkratio.smoothing.LinkRatioSmoothingFunction;
import org.jreserve.factor.core.linkratio.smoothing.LinkRatioSmoothingMethod;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.DefaultLinkRatioSmoothingMethod.DisplayName=No Smoothing"
})
public class DefaultLinkRatioSmoothingMethod implements LinkRatioSmoothingMethod {

    public final static String ID = DefaultLinkRatioSmoothingMethod.class.getName();
    
    @Override
    public LinkRatioSmoothingFunction createFunction() {
        return new DefaultLinkRatioSmoothingFunction();
    }

    private static class DefaultLinkRatioSmoothingFunction implements LinkRatioSmoothingFunction {
        
        private double[] linkRatios;
        private int inputLength = 0;
        
        @Override
        public void setInput(double[] linkRatios) {
            this.linkRatios = linkRatios;
            inputLength = linkRatios==null? 0 : linkRatios.length;
        }

        @Override
        public double getLinkRatio(int development) {
            if(development < 0 || development >= inputLength)
                return Double.NaN;
            return linkRatios[development];
        }
    }
}
