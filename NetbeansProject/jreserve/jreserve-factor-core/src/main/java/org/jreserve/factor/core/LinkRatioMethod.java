package org.jreserve.factor.core;

import org.jreserve.factor.core.entities.LinkRatioSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioMethod {

    public LinkRatioFunction getFunction();
    
    public LinkRatioSelection createSelection(int development);
    
    public static @interface Registration {
        public String displayName();
        public int position() default Integer.MAX_VALUE;
    }
}
