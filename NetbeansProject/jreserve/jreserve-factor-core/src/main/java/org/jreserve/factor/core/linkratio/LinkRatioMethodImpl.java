package org.jreserve.factor.core.linkratio;

import org.jreserve.factor.core.LinkRatioFunction;
import org.jreserve.factor.core.LinkRatioMethod;
import org.jreserve.factor.core.entities.LinkRatioSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LinkRatioMethodImpl implements LinkRatioMethod {

    private final LinkRatioMethod delegate;
    private final int position;
    private final String displayName;

    public LinkRatioMethodImpl(LinkRatioMethod delegate, int position, String displayName) {
        this.delegate = delegate;
        this.position = position;
        this.displayName = displayName;
    }
    
    public int getPosition() {
        return position;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public LinkRatioFunction getFunction() {
        return delegate.getFunction();
    }

    @Override
    public LinkRatioSelection createSelection(int development) {
        return delegate.createSelection(development);
    }
}
