package org.jreserve.smoothing.core;

import java.util.List;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Smoothable {

    public final static String SMOOTHING_PROPERTY = "PROP_SMOOTHINGS";
    
    public PersistentObject getOwner();
    
    public List<Smoothing> getSmoothings();
    
    public void setSmoothings(List<Smoothing> smoothings);
    
    public void addSmoothing(Smoothing smoothing);
    
    public void removeSmoothing(Smoothing smoothing);
    
    public int getMaxSmoothingOrder();
}
