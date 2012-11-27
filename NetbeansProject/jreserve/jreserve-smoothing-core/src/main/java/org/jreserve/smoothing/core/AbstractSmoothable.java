package org.jreserve.smoothing.core;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.project.system.ProjectElement;
import static org.jreserve.smoothing.core.Smoothable.SMOOTHING_PROPERTY;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractSmoothable<T> implements Smoothable {

    private final static String WRONG_OWNER = "Smoothing '%s' belongs to another owner than '%s'! OwnerId = '%s'.";
    
    protected ProjectElement<T> element;
    protected PersistentObject owner;
    
    public AbstractSmoothable(ProjectElement<T> element, PersistentObject owner) {
        this.element = element;
        this.owner = owner;
    }
    
    @Override
    public PersistentObject getOwner() {
        return owner;
    }
    
    @Override
    public List<Smoothing> getSmoothings() {
        return (List<Smoothing>) element.getProperty(SMOOTHING_PROPERTY);
    }
    
    @Override
    public void setSmoothings(List<Smoothing> smoothings) {
        checkOwnerId(smoothings);
        element.setProperty(SMOOTHING_PROPERTY, smoothings);
    }
    
    private void checkOwnerId(List<Smoothing> smoothings) {
        for(Smoothing smoothing : smoothings)
            checkOwnerId(smoothing);
    }

    private void checkOwnerId(Smoothing smoothing) {
        if(!smoothing.getOwner().equals(owner.getId())) {
            String msg = String.format(WRONG_OWNER, smoothing, owner, smoothing.getOwner());
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public void addSmoothing(Smoothing smoothing) {
        checkOwnerId(smoothing);
        List<Smoothing> smoothings = new ArrayList<Smoothing>(getSmoothings());
        if(!smoothings.contains(smoothing))
            smoothings.add(smoothing);
        element.setProperty(SMOOTHING_PROPERTY, smoothings);
    }
    
    @Override
    public void removeSmoothing(Smoothing smoothing) {
        List<Smoothing> smoothings = new ArrayList<Smoothing>(getSmoothings());
        smoothings.remove(smoothing);
        element.setProperty(SMOOTHING_PROPERTY, smoothings);
    }
    
    @Override
    public int getMaxSmoothingOrder() {
        int order = 0;
        for(Smoothing smoothing : getSmoothings())
            if(smoothing.getOrder() > order)
                order = smoothing.getOrder();
        return order;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Smoothable)
            return owner.equals(((Smoothable)o).getOwner());
        return false;
    }
    
    @Override
    public int hashCode() {
        return owner.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("AbstractSmoothable [%s; %s]", element, owner);
    }
}
