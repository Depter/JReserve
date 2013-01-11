package org.jreserve.triangle.smoothing.visual;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.SmoothingTableModel.Original=Original",
    "LBL.SmoothingTableModel.Smoothed=Smoothed",
    "LBL.SmoothingTableModel.Use=Apply"
})
class SmoothingTableModel extends AbstractTableModel {

    private List<SmoothDummy> dummies = new ArrayList<SmoothDummy>();
    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    private Object source;

    void setInputValues(double[] input) {
        dummies.clear();
        for(double i : input)
            dummies.add(new SmoothDummy(i));
        fireTableDataChanged();
    }
    
    double[] getInputValues() {
        int size = dummies.size();
        double[] result = new double[size];
        for(int i=0; i<size; i++)
            result[i] = dummies.get(i).input;
        return result;
    }
    
    void setSmoothedValues(double[] smoothed) {
        if(smoothed == null) {
            for(SmoothDummy dummy : dummies)
                dummy.smoothed = Double.NaN;
        } else {
            for(int i=0, size=smoothed.length; i<size; i++)
                dummies.get(i).smoothed = smoothed[i];
        }
        fireTableDataChanged();
    }
    
    @Override
    public Class<?> getColumnClass(int column) {
        switch(column) {
            case 0: return Double.class;
            case 1: return Double.class;
            default: return Boolean.class;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case 0: return Bundle.LBL_SmoothingTableModel_Original();
            case 1: return Bundle.LBL_SmoothingTableModel_Smoothed();
            default: return Bundle.LBL_SmoothingTableModel_Use();
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 2;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        SmoothDummy dummy = dummies.get(row);
        if(value instanceof Boolean)
            dummy.apply = (Boolean) value;
        else
            dummy.apply = false;
        fireChanged();
    }

    @Override
    public int getRowCount() {
        if(dummies != null)
            return dummies.size();
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int row, int column) {
        SmoothDummy dummy = dummies.get(row);
        switch(column) {
            case 0: return dummy.input;
            case 1: return dummy.smoothed;
            default: return dummy.apply;
        }
    }
    
    SmoothDummy getDummyAt(int row) {
        return dummies.get(row);
    }
    
    boolean hasApplied() {
        for(SmoothDummy dummy : dummies)
            if(dummy.isApplied())
                return true;
        return false;
    }
    
    void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
    
    void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void fireChanged() {
        ChangeEvent evt = new ChangeEvent(source==null? this : source);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    void setEventSource(Object source) {
        this.source = source;
    }
    
    static class SmoothDummy {
        private double input;
        private double smoothed;
        private boolean apply;
        
        private SmoothDummy(double original) {
            this.input = original;
            this.smoothed = original;
            this.apply = true;
        }
        
        double getInput() {
            return input;
        }
        
        double getSmoothed() {
            return smoothed;
        }
        
        boolean isApplied() {
            return apply;
        }
    }
}
