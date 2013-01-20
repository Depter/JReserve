package org.jreserve.triangle.smoothing;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.entities.TriangleCell;
import org.jreserve.triangle.entities.TriangleModification;
import org.jreserve.triangle.util.AbstractTriangleStackQuery;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothingTriangleStackQuery extends AbstractTriangleStackQuery<List<Smoothing>> {
    
    public static List<Smoothing> getSmoothings(ModifiableTriangle triangle, TriangleCell cell) {
        return new SmoothingTriangleStackQuery(cell).query(triangle);
    }
    
    public static List<Smoothing> getSmoothings(ModifiableTriangle triangle, int accident, int development) {
        return new SmoothingTriangleStackQuery(accident, development).query(triangle);
    }
    
    private int accident;
    private int development;
    private List<Smoothing> smoothings;
    
    public SmoothingTriangleStackQuery(TriangleCell cell) {
        this(cell.getAccident(), cell.getDevelopment());
    }
    
    public SmoothingTriangleStackQuery(int accident, int development) {
        this.accident = accident;
        this.development = development;
    }
    
    @Override
    protected void initQuery() {
        smoothings = new ArrayList<Smoothing>();
    }

    @Override
    protected boolean accepts(TriangleModification modification) {
        if(modification instanceof Smoothing)
            return smoothesCell((Smoothing) modification);
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
    protected void process(TriangleModification modification) {
        smoothings.add(((Smoothing) modification));
    }

    @Override
    protected List<Smoothing> getResult() {
        return smoothings;
    }
}
