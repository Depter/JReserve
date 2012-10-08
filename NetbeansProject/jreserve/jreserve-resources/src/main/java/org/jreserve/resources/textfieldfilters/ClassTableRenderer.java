package org.jreserve.resources.textfieldfilters;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClassTableRenderer<T> extends DefaultTableCellRenderer {

    private Class<T> clazz;
    private TextRenderer<T> renderer;
    
    public ClassTableRenderer(Class<T> clazz, TextRenderer<T> renderer) {
        this.clazz = clazz;
        this.renderer = renderer;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(value == null || clazz.isAssignableFrom(value.getClass()))
            setText(renderer.toString((T) value));
        return this;
    }

    
}
