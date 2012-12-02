package org.jreserve.estimates.factors;

import java.util.List;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 */
public interface Excludables {

    public final static String EXCLUSION_PROPERTY = "PROP_EXCLUSION";

    public PersistentObject getOwner();
    
    public List<FactorExclusion> getExclusions();
    
    public void setExclusions(List<FactorExclusion> exclusions);
    
    public void addExclusion(FactorExclusion exclusion);
    
    public void removeExclusion(FactorExclusion exclusion);

}
