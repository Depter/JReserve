package org.jreserve.triangle.mvc.view;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.jreserve.localesettings.util.DoubleRenderer;
import org.jreserve.triangle.mvc.model.TriangleTableModelConverter;

/**
 *
 * @author Peter Decsi
 */
public class DefaultRenderer implements TableCellRenderer {

    private final static Color VALUE_BG = UIManager.getColor("Table.background");
    private final static Color EMPTY_BG = UIManager.getColor("Panel.background");

    protected TableCellRenderer renderer = new DefaultTableCellRenderer();
    private DoubleRenderer valueRenderer = new DoubleRenderer();
    private TriangleTableModelConverter model;
    protected Color nonEmptyBg;
    
    public DefaultRenderer() {
        nonEmptyBg = VALUE_BG;
    }
    
    void setModel(TriangleTableModelConverter model) {
        this.model = model;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        boolean hasBackground = model.hasValueAt(row, column);
        
        String str = hasBackground? getStringValue(value) : null;
        
        Component c = renderer.getTableCellRendererComponent(table, str, false, hasFocus, row, column);
        
        c.setBackground(getBackground(hasBackground));
        
        return c;
    }
    
    private String getStringValue(Object value) {
        Double d = TriangleDoubleUtil.getTableValue(value);
        return valueRenderer.toString(d);
    }
    
    protected Color getBackground(boolean hasValue) {
        if(hasValue)
            return nonEmptyBg;
        return EMPTY_BG;
    }
    
    public void setFractionDigits(int count) {
        valueRenderer.setFractionDigits(count);
    }
}
