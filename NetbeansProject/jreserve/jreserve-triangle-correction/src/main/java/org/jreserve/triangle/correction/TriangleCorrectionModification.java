package org.jreserve.triangle.correction;

import org.jreserve.rutil.RCode;
import org.jreserve.triangle.AbstractTriangularDataModification;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.correction.entities.TriangleCorrection;
import org.jreserve.triangle.entities.TriangleCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCorrectionModification extends AbstractTriangularDataModification {

    private final static String R_CODE = "%s[%d, %d] = %d";
    
    private final int accident;
    private final int development;
    private final double value;
    
    public TriangleCorrectionModification(TriangularData source, TriangleCorrection correction) {
        super(source);
        TriangleCell cell = correction.getTriangleCell();
        this.accident = cell.getAccident();
        this.development = cell.getDevelopment();
        this.value = correction.getCorrigatedValue();
    }
    
    @Override
    public double getValue(int accident, int development) {
        if(isMyCell(accident, development))
            return value;
        return source.getValue(accident, development);
    }
    
    private boolean isMyCell(int accident, int development) {
        return withinSourceBounds(accident, development) &&
               this.accident == accident &&
               this.development == development;
    }

    @Override
    public String getLayerTypeId(int accident, int development) {
        if(isMyCell(accident, development))
            return TriangleCorrection.LAYER_ID;
        return source.getLayerTypeId(accident, development);
    }

    @Override
    public void createTriangle(String triangleName, RCode rCode) {
        source.createTriangle(triangleName, rCode);
        rCode.addSource(getRCode(triangleName));
    }
    
    private String getRCode(String triangleName) {
        return String.format(R_CODE, 
                triangleName,
                accident, development,
                value);
    }
}