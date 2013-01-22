package org.jreserve.factor.core.linkratio.smoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioSmoothingMethod {
    
    public LinkRatioSmoothingFunction createFunction();
    
    public static @interface Registration {
        public String displayName();
        public String id();
        public int position() default Integer.MAX_VALUE;
    }
}
