package org.jreserve.triangle.mvc.view;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.jreserve.localesettings.util.DoubleRenderer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DoubleTriangleTableRenderer implements TableCellRenderer {

    private final static Color VALUE_BG = UIManager.getColor("Table.background");
    private final static Color EMPTY_BG = UIManager.getColor("Panel.background");
    
    private TableCellRenderer renderer = new DefaultTableCellRenderer();
    private DoubleRenderer valueRenderer = new DoubleRenderer();
    private TriangleTableModel model;
    
    public DoubleTriangleTableRenderer(TriangleTableModel model) {
        this.model = model;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String str = valueRenderer.toString((Double) value);
        Component c = renderer.getTableCellRendererComponent(table, str, false, hasFocus, row, column);
        c.setBackground(getBackground(row, column));
        return c;
    }
    
    private Color getBackground(int row, int column) {
        if(model.hasValueAt(row, column))
            return VALUE_BG;
        return EMPTY_BG;
    }
    
    public void setFractionDigits(int count) {
        valueRenderer.setFractionDigits(count);
    }
}
