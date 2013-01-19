package org.jreserve.triangle.smoothing.visual;

import java.awt.Color;
import javax.swing.JTable;
import org.jreserve.triangle.smoothing.Smoothing;
import org.jreserve.triangle.visual.widget.DefaultWidgetRenderer;
import org.jreserve.triangle.visual.widget.DefaultWidgetRenderer.WidgetRendererRegistration;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@WidgetRendererRegistration(layerId=Smoothing.LAYER_ID)
public class SmoothingWidgetRenderer extends DefaultWidgetRenderer {
    
    private final static Color CORRECTION_BG = new Color(167, 191, 255);
    
    @Override
    protected Color getBgNotSelected(JTable table) {
        return CORRECTION_BG;
    }
}
