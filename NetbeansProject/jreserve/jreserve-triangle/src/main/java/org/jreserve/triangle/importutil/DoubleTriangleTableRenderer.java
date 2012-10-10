package org.jreserve.triangle.importutil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.jreserve.data.model.DataTable;
import org.jreserve.localesettings.util.DoubleRenderer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DoubleTriangleTableRenderer implements TableCellRenderer {

    private TableCellRenderer renderer = DoubleRenderer.getTableRenderer();
    private ImportTableModel model;

    public DoubleTriangleTableRenderer(ImportTableModel model) {
        this.model = model;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setBackground(getBackground(table, row, column));
        return c;
    }
    
    private Color getBackground(JTable table, int row, int column) {
        if(hasValue(row, column))
            return table.getBackground();
        return getBackground(table);
    }
    
    private boolean hasValue(int row, int column) {
        DataTable table = model.getTable();
        if(table == null)
            return false;
        return table.getCell(row, column) != null;
    }
    
    private Color getBackground(JTable table) {
        Container c = table.getParent();
        if(c == null)
            return table.getBackground();
        return c.getBackground();
    }
}
