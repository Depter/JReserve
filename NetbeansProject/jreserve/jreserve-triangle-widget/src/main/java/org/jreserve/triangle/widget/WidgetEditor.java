package org.jreserve.triangle.widget;

import org.jreserve.triangle.widget.model.WidgetTableModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface WidgetEditor {

    public boolean isCellEditable(int accident, int development);
    
    public boolean setCellValue(WidgetTableModel model, int accident, int development, Double value);
    
    public static @interface Registration {
        public String category();
        public int position() default Integer.MAX_VALUE;
    }
}
