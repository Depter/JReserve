package org.jreserve.triangle.correction;

import org.jreserve.triangle.AbstractTriangleModification;
import org.jreserve.triangle.correction.entities.TriangleCorrection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCorrectionModification extends AbstractTriangleModification {

    private TriangleCorrection correction;
    
    @Override
    public int getOrder() {
        return correction.getOrder();
    }

    @Override
    public double getValue(int accident, int development) {
        double base = source.getValue(accident, development);
        if(myCell(accident, development))
            return correction.getCorrigatedValue();
        return base;
    }
    
    private boolean myCell(int accident, int development) {
        return accident == correction.getAccident() &&
               development == correction.getDevelopment();
    }

    @Override
    public String getLayerTypeId(int accident, int development) {
        String sourceId = source.getLayerTypeId(accident, development);
        if(myCell(accident, development))
            return TriangleCorrection.LAYER_ID;
        return sourceId;
    }
    
    @Override
    public String toString() {
        return String.format("TriangleCorrectionModification [o=%d; a=%d; d=%d; v=%f]", 
                getOrder(), 
                correction.getAccident(), correction.getDevelopment(),
                correction.getCorrigatedValue());
    }
}