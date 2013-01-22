package org.jreserve.triangle.entities;

import java.util.*;
import org.jreserve.triangle.ModifiableTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleModificationContainer {
    
    private Set<TriangleModification> modifications;
    private ModifiableTriangle triangle;
    private List<TriangleModificationListener> listeners = new ArrayList<TriangleModificationListener>();
    
    public TriangleModificationContainer(ModifiableTriangle triangle, Set<TriangleModification> modifications) {
        this.triangle = triangle;
        this.modifications = modifications;
    }
    
    public void addModification(TriangleModification modification) {
        checkModification(modification);
        modifications.add(modification);
        fireModificationsChanged();
    }
    
    private void checkModification(TriangleModification modification) {
        checkModification(modifications, modification);
    }
    
    private void checkModification(Set<TriangleModification> mods, TriangleModification modification) {
        if(modification == null) 
            throw new NullPointerException("Modification is null!");
        checkModificationOrder(mods, modification);
    }
    
    private void checkModificationOrder(Set<TriangleModification> mods, TriangleModification modification) {
        int order = modification.getOrder();
        if(containsOrder(mods, order)) {
            String msg = "Can not add modification %s to triangle %s! Order %d already exists!";
            msg = String.format(msg, modification, this, order);
            throw new IllegalArgumentException(msg);
        }
    }
    
    private boolean containsOrder(Set<TriangleModification> mods, int order) {
        for(TriangleModification mod : mods)
            if(mod.getOrder() == order)
                return true;
        return false;
    }
    
    public void removeModification(TriangleModification modification) {
        modifications.remove(modification);
        fireModificationsChanged();
    }
    
    public void setModifications(Collection<TriangleModification> modifications) {
        Set<TriangleModification> newMods = new TreeSet<TriangleModification>();
        for(TriangleModification mod : modifications) {
            checkModification(newMods, mod);
            newMods.add(mod);
        }
        this.modifications = newMods;
        fireModificationsChanged();
    }

    public int getMaxModificationOrder() {
        int max = 0;
        for(TriangleModification mod : modifications)
            if(max < mod.getOrder())
                max = mod.getOrder();
        return max;
    }
    
    public List<TriangleModification> getModifications() {
        return new ArrayList<TriangleModification>(modifications);
    }

    public void addTriangleModificationListener(TriangleModificationListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeTriangleModificationListener(TriangleModificationListener listener) {
        listeners.remove(listener);
    }
    
    public void fireModificationsChanged() {
        for(TriangleModificationListener listener : new ArrayList<TriangleModificationListener>(listeners)) 
            listener.modificationsChanged(triangle);
    }
}
