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
public class SimpleTriangularFactors implements TriangularData {
    
    public final static String LAYER_TYPE_ID = "INPUT_FACTORS";
    
    private TriangularData source;
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private ChangeListener sourceListener;
    
    private int accidentCount;
    private int developmentCount;
    private double[][] factors;
    
    public SimpleTriangularFactors(TriangularData source) {
        sourceListener = new SourceListener();
        this.source = source;
        source.addChangeListener(sourceListener);
        calculateFactors();
    }
    
    private void calculateFactors() {
        accidentCount = source.getAccidentCount();
        factors = new double[accidentCount][];
        for(int a=0; a<accidentCount; a++)
            factors[a] = calculateFactors(a);
    }
    
    private double[] calculateFactors(int accident) {
        int developments = source.getDevelopmentCount(accident);
        return null;
    }
    
    @Override
    public int getAccidentCount() {
        return accidentCount;
    }

    @Override
    public int getDevelopmentCount() {
        return developmentCount;
    }

    @Override
    public int getDevelopmentCount(int accident) {
        if(accident >= accidentCount)
            return 0;
        return factors[accident].length;
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
        if(accident >= accidentCount)
            return Double.NaN;
        double[] developments = factors[accident];
        if(development >= developments.length)
            return Double.NaN;
        return developments[development];
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
        double[][] copy = new double[accidentCount][];
        for(int a=0; a<accidentCount; a++) {
            int developments = factors[a].length;
            copy[a] = new double[developments];
            System.arraycopy(factors[a], 0, copy[a], 0, developments);
        }
        return copy;
    }

    @Override
    public String getLayerTypeId(int accident, int development) {
        return LAYER_TYPE_ID;
    }

    @Override
    public void createTriangle(String triangleName, RCode rCode) {
        source.createTriangle(triangleName, rCode);
        rCode.addFunction(RFactorsFunction.NAME);
        rCode.addSource(RFactorsFunction.factors(triangleName)+"\n\n");
    }
    
    public void releaseSource() {
        this.source.removeChangeListener(sourceListener);
    }
    
    private class SourceListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            calculateFactors();
            fireChange();
        }
    }
}
