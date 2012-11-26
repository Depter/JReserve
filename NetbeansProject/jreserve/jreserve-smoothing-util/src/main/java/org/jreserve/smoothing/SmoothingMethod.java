package org.jreserve.smoothing;

import org.jreserve.smoothing.core.Smoothable;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;

/**
 *
 * @author Peter Decsi
 */
public interface SmoothingMethod {

    public Smoothing createSmoothing(Smoothable owner, TriangleWidget widget, TriangleCell[] cells);
    
    public @interface Registration {
        public int id();
        public String displayName();
    }
}
