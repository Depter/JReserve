package org.jreserve.factor.core.data;

import org.jreserve.rutil.RCode;
import org.jreserve.triangle.AbstractChangeableTriangularDataModification;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.util.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CummulatedTriangularData extends AbstractChangeableTriangularDataModification {
    
    private double[][] values;
    
    public CummulatedTriangularData(TriangularData source) {
        super(source);
        cummulateSource();
    }
    
    private void cummulateSource() {
        values = source.toArray();
        TriangleUtil.cummulate(values);
    }

    @Override
    public double getValue(int accident, int development) {
        return values[accident][development];
    }

    @Override
    public void createRTriangle(String triangleName, RCode rCode) {
        source.createRTriangle(triangleName, rCode);
        rCode.addFunction(RCummulateFunction.NAME);
        rCode.addSource(RCummulateFunction.cummulate(triangleName));
    }

    @Override
    protected void sourceChanged() {
        cummulateSource();
        fireChange();
    }

    @Override
    public String getLayerTypeId(int accident, int development) {
        return source.getLayerTypeId(accident, development);
    }
}