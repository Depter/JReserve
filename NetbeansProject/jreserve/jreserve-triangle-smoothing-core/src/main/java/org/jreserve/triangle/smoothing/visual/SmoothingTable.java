package org.jreserve.triangle.smoothing.visual;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SmoothingTable extends JTable {

    private SmoothingTableModel model;
    private DoubleCellRenderer renderer;
    
    public SmoothingTable() {
        model = new SmoothingTableModel();
        model.setEventSource(this);
        super.setModel(model);
        renderer = new DoubleCellRenderer();
        super.setDefaultRenderer(Double.class, renderer);
    }
    
    void setDigits(int digits) {
        renderer.setFractionDigits(digits);
        model.fireTableDataChanged();
    }
    
    public void setInputValues(double[] input) {
        model.setInputValues(input);
    }
    
    public void setSmoothedValues(double[] smoothed) {
        model.setSmoothedValues(smoothed);
    }
    
    public double getInput(int row) {
        return model.getDummyAt(row).getInput();
    }
    
    public double getSmoothed(int row) {
        return model.getDummyAt(row).getSmoothed();
    }
    
    public boolean hasApplied() {
        return model.hasApplied();
    }
    
    public boolean isApplied(int row) {
        return model.getDummyAt(row).isApplied();
    }
    
    public void addChangeListener(ChangeListener listener) {
        model.addChangeListener(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        model.removeChangeListener(listener);
    }
    
    void setEventSource(Object source) {
        model.setEventSource(source);
    }
    
    private class DoubleCellRenderer extends DefaultTableCellRenderer {
        
        private org.jreserve.localesettings.util.DoubleRenderer renderer;

        private DoubleCellRenderer() {
            renderer = new org.jreserve.localesettings.util.DoubleRenderer();
        }
        
        void setFractionDigits(int digits) {
            renderer.setFractionDigits(digits);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(value instanceof Double)
                value = renderer.toString((Double) value);
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}
