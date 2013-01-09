package org.jreserve.triangle;

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
public class TriangularDataAdapter implements TriangularData, ChangeListener {
    
    private TriangularData data;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    
    public TriangularDataAdapter(TriangularData data) {
        this.data = data;
        this.data.addChangeListener(this);
    }
    
    @Override
    public int getAccidentCount() {
        return data.getAccidentCount();
    }

    @Override
    public int getDevelopmentCount() {
        return data.getDevelopmentCount();
    }

    @Override
    public int getDevelopmentCount(int accident) {
        return data.getDevelopmentCount(accident);
    }

    @Override
    public Date getAccidentName(int accident) {
        return data.getAccidentName(accident);
    }

    @Override
    public Date getDevelopmentName(int accident, int development) {
        return data.getDevelopmentName(accident, development);
    }

    @Override
    public double getValue(int accident, int development) {
        return data.getValue(accident, development);
    }

    @Override
    public double[][] toArray() {
        return data.toArray();
    }

    @Override
    public String getLayerTypeId(int accident, int development) {
        return data.getLayerTypeId(accident, development);
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener));
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener l : new ArrayList<ChangeListener>(listeners))
            l.stateChanged(evt);
    }
    
    public void releaseData() {
        this.data.removeChangeListener(this);
        this.data = TriangularData.EMPTY;
    }
}