package org.jreserve.triangle.visual.widget;

import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface WidgetEditor {

    public boolean isCellEditable(int accident, int development);
    
    public void setLookup(Lookup lookup);
    
    public boolean setCellValue(WidgetTableModel model, int accident, int development, Double value);
    
    public static @interface Registration {
        public String category();
        public int position() default Integer.MAX_VALUE;
    }
}
