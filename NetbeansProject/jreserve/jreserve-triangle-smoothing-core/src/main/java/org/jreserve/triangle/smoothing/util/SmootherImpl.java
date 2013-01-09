package org.jreserve.triangle.smoothing.util;

import java.util.List;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.TriangleCoordiante;
import org.jreserve.triangle.smoothing.Smoother;
import org.jreserve.triangle.smoothing.Smoothing;

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
    public Smoothing createSmoothing(ModifiableTriangle triangle, List<TriangleCoordiante> cells, int visibleDigits) {
        return smoother.createSmoothing(triangle, cells, visibleDigits);
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
