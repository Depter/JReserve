package org.jreserve.factor.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.rutil.RCode;
import org.jreserve.triangle.TriangularData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CummulatedTriangularData implements TriangularData {

    private SourceListener sourceListener;
    private TriangularData source;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private double[][] values;
    
    public CummulatedTriangularData(TriangularData source) {
        sourceListener = new SourceListener();
        this.source = source;
        source.addChangeListener(sourceListener);
        cummulateSource();
    }
    
    private void cummulateSource() {
        int accidents = source.getAccidentCount();
        values = new double[accidents][];
        for(int a=0; a<accidents; a++)
            values[a] = cummulateAccident(a);
    }
    
    private double[] cummulateAccident(int accident) {
        int developments = source.getDevelopmentCount(accident);
        double[] result = new double[developments];
        if(developments > 0)
            result[0] = source.getValue(accident, 0);
        
        for(int d=1; d<developments; d++) {
            double prev = result[d - 1];
            double value = source.getValue(accident, d);
            result[d] = cummulate(prev, value);
        }
        
        return result;
    }
    
    private double cummulate(double prev, double current) {
        if(Double.isNaN(prev))
            return current;
        return prev + current;
    }
    
    public void releaseSource() {
        this.source.removeChangeListener(sourceListener);
    }
    
    @Override
    public int getAccidentCount() {
        return source.getAccidentCount();
    }

    @Override
    public int getDevelopmentCount() {
        return source.getDevelopmentCount();
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
    public double getValue(int accident, int development) {
        return values[accident][development];
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
    
    private void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }

    @Override
    public double[][] toArray() {
        int accidents = values.length;
        double[][] copy = new double[accidents][];
        for(int a=0; a<accidents; a++) {
            int developments = values[a].length;
            copy[a] = new double[developments];
            System.arraycopy(values[a], 0, copy[a], 0, developments);
        }
        return copy;
    }

    @Override
    public String getLayerTypeId(int accident, int development) {
        return source.getLayerTypeId(accident, development);
    }

    @Override
    public void createTriangle(String triangleName, RCode rCode) {
        source.createTriangle(triangleName, rCode);
        rCode.addFunction(RCummulateFunction.NAME);
        rCode.addSource(RCummulateFunction.cummulate(triangleName));
    }
    
    private class SourceListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            cummulateSource();
            fireChange();
        }
    }
}