package org.jreserve.triangle.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangleModification implements TriangleModification {
    
    protected TriangularData source;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    @Override
    public void setSource(TriangularData source) {
        this.source = source;
    }

    @Override
    public int getAccidentCount() {
        return source==null? 0 : source.getAccidentCount();
    }

    @Override
    public int getDevelopmentCount() {
        return source==null? 0 : source.getDevelopmentCount();
    }

    @Override
    public int getDevelopmentCount(int accident) {
        return source.getDevelopmentCount(accident);
    }

    @Override
    public Date getAccidentName(int accident) {
        return source.getAccidentName(accident);
    }

    @Override
    public Date getDevelopmentName(int accident, int development) {
        return source.getDevelopmentName(accident, development);
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    protected void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    @Override
    public int compareTo(TriangleModification modification) {
        if(modification == null)
            return -1;
        return getOrder() - modification.getOrder();
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof TriangleModification)
            return getOrder() == ((TriangleModification)o).getOrder();
        return false;
    }
    
    @Override
    public int hashCode() {
        return getOrder();
    }
    
    @Override
    public String toString() {
        return String.format("AbstractTriangleModification [%d]", getOrder());
    }
}
