package org.jreserve.triangle.smoothing.geometric;

import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import org.jreserve.triangle.smoothing.visual.SmoothingCreatorPanel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class GeometricSmoothingCreatorPanel extends SmoothingCreatorPanel {
    
    private GeometricSmoothing smoothing;
    
    GeometricSmoothingCreatorPanel(int visibleDigits, double[] input) {
        super(visibleDigits, input);
    }

    @Override
    protected void addUserInputs(JPanel panel, PropertyChangeListener listener) {
    }

    @Override
    protected double[] getSmoothedValues(double[] input) {
        if(smoothing == null)
            smoothing = new GeometricSmoothing();
        return smoothing.smooth(input);
    }
}
