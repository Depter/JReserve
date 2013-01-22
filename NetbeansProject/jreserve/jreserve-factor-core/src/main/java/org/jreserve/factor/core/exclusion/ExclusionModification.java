package org.jreserve.factor.core.exclusion;

import org.jreserve.factor.core.entities.FactorExclusion;
import org.jreserve.rutil.RCode;
import org.jreserve.rutil.RUtil;
import org.jreserve.triangle.AbstractTriangularDataModification;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.entities.TriangleCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExclusionModification extends AbstractTriangularDataModification {

    private final static String R_CODE = "%s[%d, %d] = "+RUtil.NAN_VALUE+"%n";
    private final int accident;
    private final int development;

    public ExclusionModification(TriangularData source, TriangleCell cell) {
        this(source, cell.getAccident(), cell.getDevelopment());
    }

    public ExclusionModification(TriangularData source, int accident, int development) {
        super(source);
        this.accident = accident;
        this.development = development;
    }
    
    @Override
    public double getValue(int accident, int development) {
        if(myCell(accident, development))
            return Double.NaN;
        return source.getValue(accident, development);
    }

    private boolean myCell(int accident, int development) {
        return this.accident == accident
                && this.development == development;
    }

    @Override
    public String getLayerTypeId(int accident, int development) {
        if(myCell(accident, development))
            return FactorExclusion.LAYER_ID;
        return source.getLayerTypeId(accident, development);
    }

    @Override
    public void createRTriangle(String triangleName, RCode rCode) {
        String code = String.format(R_CODE, triangleName, accident+1, development+1);
        rCode.addSource(code);
    }
}
