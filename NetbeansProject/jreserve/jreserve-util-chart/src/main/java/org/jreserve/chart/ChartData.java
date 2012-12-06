package org.jreserve.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 */
public abstract class ChartData<R extends Comparable<R>, C extends Comparable<C>> extends ChartFormat {

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    public String getRowLabel(R rowKey) {
        return ""+rowKey;
    }
    
    public String getColumnLabel(C columnKey) {
        return ""+columnKey;
    }
    
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    protected void fireChangeEvent() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }

    public abstract Map<R, Map<C, Double>> getData();
}
