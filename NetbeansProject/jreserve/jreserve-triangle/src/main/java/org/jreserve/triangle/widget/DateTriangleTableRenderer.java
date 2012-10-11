package org.jreserve.triangle.widget;

import java.awt.Component;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.jreserve.localesettings.util.DateRenderer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DateTriangleTableRenderer implements TableCellRenderer {

    private TableCellRenderer renderer;
    private DateRenderer dateRenderer = new DateRenderer();
    
    DateTriangleTableRenderer(JTable table) {
        renderer = table.getTableHeader().getDefaultRenderer();
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value instanceof Date)
            value = dateRenderer.toString((Date) value);
        return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

}
