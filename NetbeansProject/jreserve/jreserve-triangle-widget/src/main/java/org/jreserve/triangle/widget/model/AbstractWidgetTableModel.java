package org.jreserve.triangle.widget.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.jreserve.triangle.TriangularData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractWidgetTableModel implements WidgetTableModel, ChangeListener {

    protected TriangularData data;
    protected List<TableModelListener> listeners = new ArrayList<TableModelListener>();
    
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
        fireTableStructureChanged();
    }
    
    private void initData(TriangularData data) {
        this.data = data;
        if(this.data != null)
            this.data.addChangeListener(this);
    }
    
    @Override
    public int getRowCount() {
        return data==null? 0 : data.getAccidentCount();
    }

    @Override
    public int getColumnCount() {
        return data==null? 0 : data.getDevelopmentCount()+1;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if(column == 0)
            return getRowTitleClass();
        return Double.class;
    }
    
    protected Class<?> getRowTitleClass() {
        return java.util.Date.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if(column == 0)
            return false;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(column == 0)
            return data.getAccidentName(row);
        return getData(row, column-1);
    }
    
    protected abstract String getRowName(int row);
    
    protected abstract Double getData(int row, int column);

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Set the value on the data object!");
    }

    @Override
    public void addTableModelListener(TableModelListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }
    
    protected void fireTableDataChanged() {
        fireTableChanged(new TableModelEvent(this, 0, Integer.MAX_VALUE));
    }

    protected void fireTableStructureChanged() {
        fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
    }

    protected void fireTableChanged(TableModelEvent event) {
        for(TableModelListener listener : new ArrayList<TableModelListener>(listeners))
            listener.tableChanged(event);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        fireTableStructureChanged();
    }

}
