package org.jreserve.smoothing;

import java.util.Collections;
import java.util.List;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.smoothing.core.SmoothingCell;
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
        
        
        List<SmoothingCell> cells = getCells(smoothing);
    
        return null;
    }
    
    private List<SmoothingCell> getCells(Smoothing smoothing) {
        List<SmoothingCell> cells = smoothing.getCells();
        Collections.sort(cells);
        return cells;
    }

}
