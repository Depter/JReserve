package org.jreserve.triangle.widget.model;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.widget.WidgetEditor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractWidgetTableModel extends AbstractTableModel implements WidgetTableModel, ChangeListener {

    protected TriangularData data;
    
    protected boolean cummulated;
    protected WidgetEditor editor;
    
    public AbstractWidgetTableModel() {
    }
    
    public AbstractWidgetTableModel(TriangularData data) {
        initData(data);
    }
    
    @Override
    public void setData(TriangularData data) {
        if(this.data != null)
            this.data.removeChangeListener(this);
        initData(data);
        super.fireTableStructureChanged();
    }
    
    private void initData(TriangularData data) {
        this.data = data;
        if(this.data != null)
            this.data.addChangeListener(this);
    }
    
    @Override
    public TriangularData getData() {
        return data;
    }
    
    @Override
    public void setCummulated(boolean cummulated) {
        this.cummulated = cummulated;
        fireTableDataChanged();
    }
    
    @Override
    public boolean isCummulated() {
        return cummulated;
    }
    
    @Override
    public void setWidgetEditor(WidgetEditor editor) {
        this.editor = editor;
    }
    
    @Override
    public WidgetEditor getWidgetEditor() {
        return editor;
    }
    
    @Override
    public String getLayerId(int row, int column) {
        if(data == null || 
           row > data.getAccidentCount() || 
           --column >= data.getDevelopmentCount(row)) 
            return null;
        return data.getLayerTypeId(row, column);
    }
    
    @Override
    public int getRowCount() {
        if(data == null)
            return 0;
        else
            return data.getAccidentCount();
    }

    @Override
    public int getColumnCount() {
        if(data == null)
            return 0;
        else
            return data.getDevelopmentCount()+1;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if(column == 0)
            return getRowTitleClass();
        else
            return Double.class;
    }
    
    protected Class<?> getRowTitleClass() {
        return java.util.Date.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if(column == 0)
            return false;
        return editor!=null && getData(row, column-1)!=null;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(column == 0)
            return data.getAccidentName(row);
        else if(cummulated)
            return getCummulatedData(row, column-1);
        else
            return getData(row, column-1);
    }
    
    protected abstract String getRowName(int row);
    
    private Double getCummulatedData(int row, int column) {
        Double sum = null;
        for(int c=column; c>=0; c--) {
            Double value = getData(row, c);
            if(value == null)
                return sum;
            if(Double.isNaN(value))
                return c==column? Double.NaN : sum;
            sum = sum==null? value : sum+value;
        }
        return sum;
    }
    
    protected abstract Double getData(int row, int column);

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        fireTableStructureChanged();
    }
}
