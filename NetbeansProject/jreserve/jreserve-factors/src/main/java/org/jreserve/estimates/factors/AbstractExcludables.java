package org.jreserve.estimates.factors;

import java.util.ArrayList;
import java.util.List;
import static org.jreserve.estimates.factors.Excludables.EXCLUSION_PROPERTY;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 */
public class AbstractExcludables<T> implements Excludables {

    private final static String WRONG_OWNER = "Exclusion '%s' belongs to another owner than '%s'! OwnerId = '%s'.";
    
    protected ProjectElement<T> element;
    protected PersistentObject owner;

    public AbstractExcludables(ProjectElement<T> element, PersistentObject owner) {
        this.element = element;
        this.owner = owner;
    }

    @Override
    public PersistentObject getOwner() {
        return owner;
    }

    @Override
    public List<FactorExclusion> getExclusions() {
        return (List<FactorExclusion>) element.getProperty(EXCLUSION_PROPERTY);
    }

    @Override
    public void setExclusions(List<FactorExclusion> exclusions) {
        checkOwnerId(exclusions);
        element.setProperty(EXCLUSION_PROPERTY, exclusions);
    }
    
    private void checkOwnerId(List<FactorExclusion> exclusions) {
        for(FactorExclusion exclusion : exclusions)
            checkOwnerId(exclusion);
    }

    private void checkOwnerId(FactorExclusion exclusion) {
        if(!exclusion.getOwnerId().equals(owner.getId())) {
            String msg = String.format(WRONG_OWNER, exclusion, owner, exclusion.getOwnerId());
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public void addExclusion(FactorExclusion exclusion) {
        checkOwnerId(exclusion);
        List<FactorExclusion> exclusions = new ArrayList<FactorExclusion>(getExclusions());
        if(!exclusions.contains(exclusion))
            exclusions.add(exclusion);
        element.setProperty(EXCLUSION_PROPERTY, exclusions);
    }

    @Override
    public void removeExclusion(FactorExclusion exclusion) {
        List<FactorExclusion> exclusions = new ArrayList<FactorExclusion>(getExclusions());
        exclusions.remove(exclusion);
        element.setProperty(EXCLUSION_PROPERTY, exclusions);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Excludables)
            return owner.equals(((Excludables)o).getOwner());
        return false;
    }
    
    @Override
    public int hashCode() {
        return owner.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("AbstractExcludables [%s; %s]", element, owner);
    }
}
