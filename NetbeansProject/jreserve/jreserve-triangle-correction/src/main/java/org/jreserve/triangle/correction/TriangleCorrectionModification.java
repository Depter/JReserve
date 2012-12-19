package org.jreserve.triangle.correction;

import org.hibernate.Session;
import org.jreserve.triangle.AbstractTriangleModification;
import org.jreserve.triangle.correction.entities.TriangleCorrection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleCorrectionModification extends AbstractTriangleModification {

    private TriangleCorrection correction;
    
    TriangleCorrectionModification(TriangleCorrection correction) {
        this.correction = correction;
    }
    
    @Override
    public int getOrder() {
        return correction.getOrder();
    }

    @Override
    public double getValue(int accident, int development) {
        if(myCell(accident, development))
            return correction.getCorrigatedValue();
        return source.getValue(accident, development);
    }
    
    public boolean myCell(int accident, int development) {
        return accident == correction.getAccident() &&
               development == correction.getDevelopment();
    }

    @Override
    public String getLayerTypeId(int accident, int development) {
        if(myCell(accident, development))
            return TriangleCorrection.LAYER_ID;
        return source.getLayerTypeId(accident, development);
    }
    
    @Override
    public String toString() {
        return String.format("TriangleCorrectionModification [o=%d; a=%d; d=%d; v=%f]", 
                getOrder(), 
                correction.getAccident(), correction.getDevelopment(),
                correction.getCorrigatedValue());
    }

    @Override
    public String getOwnerId() {
        return correction.getOwnerId();
    }

    @Override
    public void save(Session session) {
        session.saveOrUpdate(correction);
    }

    @Override
    public void delete(Session session) {
        Object contained = session.merge(correction);
        session.delete(contained);
    }
}