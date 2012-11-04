package org.jreserve.smoothing;

import java.util.List;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;

/**
 *
 * @author Peter Decsi
 */
public interface SmoothingMethod {

    public Smoothing createSmoothing(PersistentObject owner, TriangleWidget widget, TriangleCell[] cells);
    
    public List<Smoothing> getSmoothings(PersistentObject owner);
    
    public @interface Registration {
        public int id();
        public String displayName();
    }
}
