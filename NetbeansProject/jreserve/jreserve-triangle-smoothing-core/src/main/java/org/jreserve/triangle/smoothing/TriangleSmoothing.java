package org.jreserve.triangle.smoothing;

import java.util.List;
import org.hibernate.Session;
import org.jreserve.rutil.RCode;
import org.jreserve.triangle.value.AbstractTriangleModification;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleSmoothing extends AbstractTriangleModification {
    
    private Smoothing smoothing;
    private List<SmoothingCell> cells;
    private int cellCount;
    
    public TriangleSmoothing(Smoothing smoothing) {
        this.smoothing = smoothing;
        this.cells = smoothing.getCells();
        this.cellCount = cells.size();
    }
    
    public Smoothing getSmoothing() {
        return smoothing;
    }
    
    @Override
    public String getOwnerId() {
        return smoothing.getOwnerId();
    }

    @Override
    public int getOrder() {
        return smoothing.getOrder();
    }

    @Override
    public void save(Session session) {
        session.saveOrUpdate(smoothing);
    }

    @Override
    public void delete(Session session) {
        Object contained = session.merge(smoothing);
        session.delete(contained);
    }

    @Override
    public double getValue(int accident, int development) {
        if(cellsWithinSourceBounds())
            return getSmoothedValue(accident, development);
        return source.getValue(accident, development);
    }
    
    private double getSmoothedValue(int accident, int development) {
        int index = getSmoothedIndex(accident, development);
        if(index < 0)
            return source.getValue(accident, development);
        return getSmoothedValue(index);
    }
    
    private int getSmoothedIndex(int accident, int development) {
        for(int i=0; i<cellCount; i++)
            if(cells.get(i).isSameCell(accident, development))
                return cells.get(i).isApplied()? i : -1;
        return -1;
    }
    
    private double getSmoothedValue(int index) {
        double[] input = getInput();
        double[] smoothed = smoothing.smooth(input);
        return smoothed[index];
    }
    
    private double[] getInput() {
        double[] input = new double[cellCount];
        for(int i=0; i<cellCount; i++)
            input[i] = getInput(cells.get(i));
        return input;
    }
    
    private double getInput(SmoothingCell cell) {
        int accident = cell.getAccidentPeriod();
        int development = cell.getDevelopmentPeriod();
        return source.getValue(accident, development);
    }

    @Override
    public String getLayerTypeId(int accident, int development) {
        if(!cellsWithinSourceBounds())
            return source.getLayerTypeId(accident, development);
        return getSmoothedLayerTypeId(accident, development);
    }
    
    private String getSmoothedLayerTypeId(int accident, int development) {
        int index = getSmoothedIndex(accident, development);
        if(index < 0)
            return source.getLayerTypeId(accident, development);
        return Smoothing.LAYER_ID;
    }
    
    @Override
    protected void appendModification(String triangleName, RCode rCode) {
        if(cellsWithinSourceBounds())
            smoothing.appendSmoothing(triangleName, rCode);
    }
    
    private boolean cellsWithinSourceBounds() {
        for(SmoothingCell cell : smoothing.getCells())
            if(!cellWithinSourceBounds(cell))
                return false;
        return true;
    }
    
    private boolean cellWithinSourceBounds(SmoothingCell cell) {
        return withinSourceBounds(
                cell.getAccidentPeriod(), 
                cell.getDevelopmentPeriod());
    }
}