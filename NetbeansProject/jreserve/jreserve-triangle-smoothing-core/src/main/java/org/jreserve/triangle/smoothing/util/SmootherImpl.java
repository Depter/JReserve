package org.jreserve.triangle.smoothing.util;

import org.jreserve.triangle.smoothing.Smoother;
import org.jreserve.triangle.smoothing.Smoothing;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmootherImpl implements Smoother {

    private final int position;
    private final String displayName;
    private final Smoother smoother;

    SmootherImpl(int position, String displayName, Smoother smoother) {
        this.position = position;
        this.displayName = displayName;
        this.smoother = smoother;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public Smoothing createSmoothing(Lookup lookup) {
        return smoother.createSmoothing(lookup);
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof SmootherImpl) &&
               position == ((SmootherImpl) o).position;
    }
    
    @Override
    public int hashCode() {
        return position;
    }
    
    @Override
    public String toString() {
        return String.format("Smoother [%d; %s]",
                position, displayName);
    }
}
