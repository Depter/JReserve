package org.jreserve.triangle.mvc.view;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.jreserve.localesettings.util.DoubleRenderer;
import org.jreserve.triangle.mvc.layer.LayeredTriangleModel;

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
    //private TriangleTableModel model;
    private LayeredTriangleModel model;
    
    public DoubleTriangleTableRenderer(LayeredTriangleModel model) {
        this.model = model;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        boolean hasBackground = model.hasValueAt(row, column);
        String str = hasBackground? getStringValue(value) : null;
        Component c = renderer.getTableCellRendererComponent(table, str, false, hasFocus, row, column);
        c.setBackground(hasBackground? VALUE_BG : EMPTY_BG);
        return c;
    }
    
    private String getStringValue(Object value) {
        if(value instanceof Double)
            return valueRenderer.toString((Double) value);
        Double d = Double.NaN;
        return valueRenderer.toString(d);
    }
    
    public void setFractionDigits(int count) {
        valueRenderer.setFractionDigits(count);
    }
}
