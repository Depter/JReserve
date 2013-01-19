package org.jreserve.triangle.smoothing;

import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Smoother {

    public Smoothing createSmoothing(Lookup lookup);
    
    public static @interface Registration {
        
        public String displayName();
        
        public int position() default Integer.MAX_VALUE;
    }
}
