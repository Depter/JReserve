package org.jreserve.triangle.mvc.view;

import java.awt.Component;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.jreserve.triangle.mvc.layer.LayeredTriangleModel;

/**
 *
 * @author Peter Decsi
 */
public class CellRenderer extends DefaultTableCellRenderer {

    private LayeredTriangleModel model;
    private HashMap<Class, TableCellRenderer> renderers = new HashMap<Class, TableCellRenderer>();
    
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
    
}
