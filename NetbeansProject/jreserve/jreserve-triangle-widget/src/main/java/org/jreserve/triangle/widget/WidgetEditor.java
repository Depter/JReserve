package org.jreserve.triangle.widget;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface WidgetEditor {

    public boolean isCellEditable(int accident, int development);
    
    public boolean setCellValue(int accident, int development, double value);
    
    public static @interface Registration {
        public String category();
        public int position() default Integer.MAX_VALUE;
    }
}
