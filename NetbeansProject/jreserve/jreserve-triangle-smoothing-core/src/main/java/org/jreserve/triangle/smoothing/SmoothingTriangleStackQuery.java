package org.jreserve.triangle.smoothing;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.triangle.ModifiableTriangularData;
import org.jreserve.triangle.value.TriangleCoordiante;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.util.AbstractTriangleStackQuery;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothingTriangleStackQuery extends AbstractTriangleStackQuery<List<TriangleSmoothing>> {
    
    public static List<TriangleSmoothing> getSmoothings(ModifiableTriangularData triangle, TriangleCoordiante cell) {
        return new SmoothingTriangleStackQuery(cell).query(triangle);
    }
    
    public static List<TriangleSmoothing> getSmoothings(ModifiableTriangularData triangle, int accident, int development) {
        return new SmoothingTriangleStackQuery(accident, development).query(triangle);
    }
    
    private int accident;
    private int development;
    private List<TriangleSmoothing> smoothings;
    
    public SmoothingTriangleStackQuery(TriangleCoordiante cell) {
        this(cell.getAccident(), cell.getDevelopment());
    }
    
    public SmoothingTriangleStackQuery(int accident, int development) {
        this.accident = accident;
        this.development = development;
    }
    
    @Override
    protected void initQuery() {
        smoothings = new ArrayList<TriangleSmoothing>();
    }

    @Override
    protected boolean acceptsData(TriangularData data) {
        if((data instanceof TriangleSmoothing))
            return smoothesCell(((TriangleSmoothing) data).getSmoothing());
        return false;
    }
    
    private boolean smoothesCell(Smoothing smoothing) {
        for(SmoothingCell sc : smoothing.getCells())
            if(isSmoothedCell(sc))
                return true;
        return false;
    }
    
    private boolean isSmoothedCell(SmoothingCell sc) {
        return sc.isApplied() &&
               sc.isSameCell(accident, development);
    }

    @Override
    protected void processData(TriangularData data) {
        smoothings.add(((TriangleSmoothing) data));
    }

    @Override
    protected List<TriangleSmoothing> getResult() {
        return smoothings;
    }
}
