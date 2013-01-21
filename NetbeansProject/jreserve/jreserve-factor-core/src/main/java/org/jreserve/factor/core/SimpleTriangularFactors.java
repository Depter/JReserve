package org.jreserve.factor.core;

import org.jreserve.rutil.RCode;
import org.jreserve.triangle.AbstractChangeableTriangularDataModification;
import org.jreserve.triangle.TriangularData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleTriangularFactors extends AbstractChangeableTriangularDataModification {
    
    public final static String LAYER_TYPE_ID = "INPUT_FACTORS";
    
    private int accidentCount;
    private int developmentCount;
    
    public SimpleTriangularFactors(TriangularData source) {
        super(source);
        calculateFactors();
    }
    
    private void calculateFactors() {
        calculateAccidentCount();
        calculateDevelopmentCount();
    }
    
    private void calculateAccidentCount() {
        int lastAccident = source.getAccidentCount() - 1;
        while(lastAccident >= 0 && source.getDevelopmentCount(lastAccident) < 2)
            lastAccident--;
        accidentCount = lastAccident+1;
    }
    
    private void calculateDevelopmentCount() {
        developmentCount = 0;
        for(int a=0; a<accidentCount; a++) {
            int dev = source.getDevelopmentCount(a) - 1;
            if(dev > developmentCount)
                developmentCount = dev;
        }
        
        if(developmentCount == 0)
            accidentCount = 0;
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
        int dev = source.getDevelopmentCount(accident) - 1;
        return dev < 0? 0 : dev;
    }

    @Override
    public double getValue(int accident, int development) {
        double current = source.getValue(accident, development);
        double next = source.getValue(accident, development + 1);
        return next / current;
    }

    @Override
    public String getLayerTypeId(int accident, int development) {
        return LAYER_TYPE_ID;
    }

    @Override
    public void createRTriangle(String triangleName, RCode rCode) {
        source.createRTriangle(triangleName, rCode);
        rCode.addFunction(RFactorsFunction.NAME);
        rCode.addSource(RFactorsFunction.factors(triangleName)+"\n\n");
    }

    @Override
    protected void sourceChanged() {
        calculateFactors();
        fireChange();
    }

    @Override
    public void close() {
        super.close();
        sourceChanged();
    }
}
