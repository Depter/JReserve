package org.jreserve.smoothing.exponential;

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
 * @version 1.0
 */
@SmoothingMethod.Registration(
    id=ExponentialSmoothingMethod.ID,
    displayName="#LBL.ExponentialSmoothingMethod.DisplayName"
)
@Messages({
    "LBL.ExponentialSmoothingMethod.DisplayName=Exponential smoothing"
})
public class ExponentialSmoothingMethod implements SmoothingMethod {
    
    public final static int ID = 200;

    @Override
    public Smoothing createSmoothing(Smoothable smoothable, TriangleWidget widget, TriangleCell[] cells) {
        double[] input = getInput(cells);
        CreatorPanel panel = CreatorPanel.create(smoothable, input, widget.getVisibleDigits());
        ExponentialSmoothing smoothing = panel.isCancelled()? null : new ExponentialSmoothing(smoothable.getOwner(), panel.getSmoothingName(), panel.getAlpha());
        fillSmoothing(smoothing, cells, panel.getApplied());
        return smoothing;
    }
    
    private double[] getInput(TriangleCell[] cells) {
        Arrays.sort(cells);
        int size = cells.length;
        double[] input = new double[size];
        for(int i=0; i<size; i++)
            input[i] = cells[i].getDisplayValue();
        return input;
    }
    
    private void fillSmoothing(ExponentialSmoothing smoothing, TriangleCell[] cells, boolean applied[]) {
        if(smoothing == null) return;
        for(int i=0, size=cells.length; i<size; i++)
            smoothing.addCell(createCell(smoothing, cells[i], applied[i]));
    }
    
    private SmoothingCell createCell(Smoothing smoothing, TriangleCell cell, boolean applied) {
        Date accident = cell.getAccidentBegin();
        Date development = cell.getDevelopmentBegin();
        return new SmoothingCell(smoothing, accident, development, applied);
    }
}
