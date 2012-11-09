package org.jreserve.smoothing.visual;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.jreserve.localesettings.util.DoubleRenderer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothingTable extends JTable {

    private SmoothingTableModel model;
    private DoubleRenderer renderer;
    
    public SmoothingTable() {
        model = new SmoothingTableModel();
        super.setModel(model);
        renderer = new DoubleRenderer();
        super.setDefaultRenderer(Double.class, new DoubleCellRenderer());
    }
    
    void setDigits(int digits) {
        renderer.setFractionDigits(digits);
        model.fireTableDataChanged();
    }
    
    public void setInput(double[] input) {
        model.setInput(input);
    }
    
    public void setSmoothed(double[] smoothed) {
        model.setSmoothed(smoothed);
    }
    
    public double getInput(int row) {
        return model.getDummy(row).getInput();
    }
    
    public double getSmoothed(int row) {
        return model.getDummy(row).getSmoothed();
    }
    
    public boolean isApplied(int row) {
        return model.getDummy(row).isApplied();
    }
    
    private class DoubleCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(value instanceof Double)
                value = renderer.toString((Double) value);
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        
        
        
    }
}
