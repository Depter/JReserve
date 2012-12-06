package org.jreserve.triangle.smoothing;

import java.util.List;
import org.jreserve.triangle.ModifiableTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Smoothable extends ModifiableTriangle {
    public final static String SMOOTHING_PROPERTY = "PROP_SMOOTHINGS";
    
    public List<Smoothing> getSmoothings();
    
    public void setSmoothings(List<Smoothing> smoothings);
    
    public void addSmoothing(Smoothing smoothing);
    
    public void removeSmoothing(Smoothing smoothing);
}
