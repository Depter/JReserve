package org.jreserve.triangle.widget.util;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.jreserve.triangle.widget.DefaultWidgetRenderer;
import org.jreserve.triangle.widget.model.WidgetTableModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleWidgetValueRenderer implements TableCellRenderer {
    
    private final TableCellRenderer defaultRenderer = new DefaultWidgetRenderer();
    
    public TriangleWidgetValueRenderer() {
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String layerId = getLayerId(table, row, column);
        TableCellRenderer renderer = getRenderer(layerId);
        return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
    
    private String getLayerId(JTable table, int row, int column) {
        TableModel model = table.getModel();
        if(model instanceof WidgetTableModel)
            return ((WidgetTableModel) model).getLayerId(row, column);
        return null;
    }
    
    private TableCellRenderer getRenderer(String layerId) {
        if(layerId == null)
            return defaultRenderer;
        TableCellRenderer renderer = WidgetRendererRegistry.getRenderer(layerId);
        return renderer!=null? renderer : defaultRenderer;
    }
} 