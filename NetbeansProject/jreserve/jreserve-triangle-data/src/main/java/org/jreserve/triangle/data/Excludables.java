package org.jreserve.triangle.data;

import java.util.List;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 */
public interface Excludables {

    public final static String EXCLUSION_PROPERTY = "PROP_EXCLUSION";

    public PersistentObject getOwner();
    
    public List<TriangleExclusion> getExclusions();
    
    public void setExclusions(List<TriangleExclusion> exclusions);
    
    public void addExclusion(TriangleExclusion exclusion);
    
    public void removeExclusion(TriangleExclusion exclusion);

}
