package org.jreserve.factor.core.linkratio.smoothing.util;

import org.jreserve.factor.core.linkratio.smoothing.LinkRatioSmoothingFunction;
import org.jreserve.factor.core.linkratio.smoothing.LinkRatioSmoothingMethod;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioSmoothingMethodImpl implements LinkRatioSmoothingMethod {
    
    private final int position;
    private final String id;
    private final String displayName;
    private final LinkRatioSmoothingMethod delegate;

    public LinkRatioSmoothingMethodImpl(int position, String id, String displayName, LinkRatioSmoothingMethod delegate) {
        this.position = position;
        this.id = id;
        this.displayName = displayName;
        this.delegate = delegate;
    }
    
    public int getPosition() {
        return position;
    }
    
    public String getId() {
        return id;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public LinkRatioSmoothingFunction createFunction() {
        return delegate.createFunction();
    }
}
