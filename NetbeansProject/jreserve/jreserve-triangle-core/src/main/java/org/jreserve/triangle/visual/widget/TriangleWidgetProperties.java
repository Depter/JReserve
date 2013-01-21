package org.jreserve.triangle.visual.widget;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleWidgetProperties {
    
    private boolean cummulated = false;
    private int visibleDigits = 0;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    TriangleWidgetProperties() {
    }
    
    public boolean isCummualted() {
        return cummulated;
    }
    
    void setCummulated(boolean cummulated) {
        if(this.cummulated != cummulated) {
            this.cummulated = cummulated;
            fireChangeEvent();
        }
    }
    
    public int getVisibleDigits() {
        return visibleDigits;
    }
    
    void setVisibleDigits(int visibleDigits) {
        if(this.visibleDigits != visibleDigits) {
            this.visibleDigits = visibleDigits;
            fireChangeEvent();
        }
    }
    
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    @Override
    public String toString() {
        String msg = "TriangleWidgetProperties [cummulated = %s; visibleDigits = %d]";
        return String.format(msg, cummulated, visibleDigits);
    }
}
