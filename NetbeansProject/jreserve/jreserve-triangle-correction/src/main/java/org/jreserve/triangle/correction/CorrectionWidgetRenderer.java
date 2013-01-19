package org.jreserve.triangle.correction;

import java.awt.Color;
import javax.swing.JTable;
import org.jreserve.triangle.correction.entities.TriangleCorrection;
import org.jreserve.triangle.visual.widget.DefaultWidgetRenderer;
import org.jreserve.triangle.visual.widget.DefaultWidgetRenderer.WidgetRendererRegistration;

/**
 *
 * @author Peter Decsi
 */
@WidgetRendererRegistration(layerId=TriangleCorrection.LAYER_ID)
public class CorrectionWidgetRenderer extends DefaultWidgetRenderer {
    
    private final static Color CORRECTION_BG = new Color(235, 204, 204);
    
    @Override
    protected Color getBgNotSelected(JTable table) {
        return CORRECTION_BG;
    }
}
