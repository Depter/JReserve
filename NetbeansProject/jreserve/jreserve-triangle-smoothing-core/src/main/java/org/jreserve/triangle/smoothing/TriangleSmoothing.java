package org.jreserve.triangle.smoothing;

import java.util.List;
import org.jreserve.rutil.RCode;
import org.jreserve.triangle.AbstractTriangularDataModification;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.util.TriangleUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleSmoothing extends AbstractTriangularDataModification {
    
    private Smoothing smoothing;
    private List<SmoothingCell> cells;
    private int cellCount;
    
    public TriangleSmoothing(TriangularData source, Smoothing smoothing) {
        super(source);
        this.smoothing = smoothing;
        this.cells = smoothing.getCells();
        this.cellCount = cells.size();
    }
    
    public Smoothing getSmoothing() {
        return smoothing;
    }

    @Override
    public double getValue(int accident, int development) {
        if(cellsWithinSourceBounds())
            return getSmoothedValue(accident, development);
        return source.getValue(accident, development);
    }
    
    private boolean cellsWithinSourceBounds() {
        for(SmoothingCell cell : smoothing.getCells())
            if(!withinSourceBounds(cell))
                return false;
        return true;
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
        double[] input = TriangleUtil.getCellValues(cells, source);
        double[] smoothed = smoothing.smooth(input);
        return smoothed[index];
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
    public void createTriangle(String triangleName, RCode rCode) {
        if(cellsWithinSourceBounds())
            smoothing.appendSmoothing(triangleName, rCode);
    }
}
