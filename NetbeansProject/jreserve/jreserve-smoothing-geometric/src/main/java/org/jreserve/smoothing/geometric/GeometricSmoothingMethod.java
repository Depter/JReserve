package org.jreserve.smoothing.geometric;

import java.util.Arrays;
import java.util.Date;
import org.jreserve.smoothing.SmoothingMethod;
import org.jreserve.smoothing.core.Smoothable;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.smoothing.core.SmoothingCell;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@SmoothingMethod.Registration(
    id=GeometricSmoothingMethod.ID,
    displayName="#LBL.GeometricSmoothingMethod.DisplayName"
)
@Messages({
    "LBL.GeometricSmoothingMethod.DisplayName=Geometric smoothing"
})
public class GeometricSmoothingMethod implements SmoothingMethod {
    
    public final static int ID = 100;
    
    @Override
    public Smoothing createSmoothing(Smoothable smoothable, TriangleWidget widget, TriangleCell[] cells) {
        double[] input = getInput(cells);
        NameSelectPanel panel = NameSelectPanel.createPanel(smoothable, input, widget.getVisibleDigits());
        if(panel.isCancelled())
            return null;
        return createGeometricSmoothing(smoothable, panel.getSmoothingName(), cells, panel.getApplied());
    }
    
    private double[] getInput(TriangleCell[] cells) {
        Arrays.sort(cells);
        int size = cells.length;
        double[] input = new double[size];
        for(int i=0; i<size; i++)
            input[i] = cells[i].getDisplayValue();
        return input;
    }
    
    private Smoothing createGeometricSmoothing(Smoothable smoothable, String name, TriangleCell[] cells, boolean[] applied) {
        GeometricSmoothing smoothing = new GeometricSmoothing(smoothable.getOwner(), name);
        for(int i=0, size=cells.length; i<size; i++)
            smoothing.addCell(createCell(smoothing, cells[i], applied[i]));
        smoothing.setOrder(smoothable.getMaxSmoothingOrder() + 1);
        return smoothing;
    }
    
    private SmoothingCell createCell(Smoothing smoothing, TriangleCell cell, boolean applied) {
        Date accident = cell.getAccidentBegin();
        Date development = cell.getDevelopmentBegin();
        return new SmoothingCell(smoothing, accident, development, applied);
    }
}
