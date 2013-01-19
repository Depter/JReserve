package org.jreserve.triangle.smoothing.arithmetic;

import org.jreserve.triangle.smoothing.arithmetic.entities.ArithmeticSmoothing;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import org.jreserve.triangle.smoothing.visual.SmoothingCreatorPanel;

/**
 *
 * @author Peter Decsi
 */
public class ArithmeticSmoothingCreatePanel extends SmoothingCreatorPanel {
    
    private ArithmeticSmoothing smoothing;
    
    ArithmeticSmoothingCreatePanel(int visibleDigits, double[] input) {
        super(visibleDigits, input);
    }

    @Override
    protected void addUserInputs(JPanel panel, PropertyChangeListener listener) {
    }

    @Override
    protected double[] getSmoothedValues(double[] input) {
        if(smoothing == null)
            smoothing = new ArithmeticSmoothing();
        return smoothing.smooth(input);
    }
}