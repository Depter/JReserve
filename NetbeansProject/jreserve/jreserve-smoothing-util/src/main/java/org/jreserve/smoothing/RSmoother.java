package org.jreserve.smoothing;

import java.util.Collections;
import java.util.List;
import org.jreserve.rutil.RUtil;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.smoothing.core.SmoothingCell;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class RSmoother {
    
    private TriangleWidget widget;
    private String triangle;
    
    public RSmoother(String triangle, TriangleWidget widget) {
        this.triangle = triangle;
        if(widget == null)
            throw new NullPointerException("TriangleWidget was null!");
        this.widget = widget;
    }
    
    public String getRSmoothing(Smoothing smoothing) {
        List<SmoothingCell> sCells = getCells(smoothing);
        List<TriangleCell> tCells = widget.getCells();
        
        int size = sCells.size();
        int x[] = new int[size];
        int y[] = new int[size];
        boolean applied[] = new boolean[size];
        
        for(int c=0; c<size; c++) {
            SmoothingCell sc = sCells.get(c);
            TriangleCell tc = getTriangleCell(tCells, sc);
            applied[c] = tc==null? false : sc.isApplied();
            x[c] = tc==null? -1 : tc.getColumn() + 1;
            y[c] = tc==null? -1 : tc.getRow() + 1;
        }
        
        return smoothing.getRSmoothing(triangle, RUtil.createVector(y), RUtil.createVector(x), RUtil.createVector(applied));
    }
    
    private List<SmoothingCell> getCells(Smoothing smoothing) {
        List<SmoothingCell> cells = smoothing.getCells();
        Collections.sort(cells);
        return cells;
    }

    private TriangleCell getTriangleCell(List<TriangleCell> tCells, SmoothingCell sc) {
        for(TriangleCell tc : tCells)
            if(tc.acceptsDates(sc.getAccident(), sc.getDevelopment()))
                return tc;
        return null;
    }
}
