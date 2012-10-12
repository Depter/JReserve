package org.jreserve.triangle.widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.TableModelListener;
import org.jreserve.data.model.DataCell;
import org.jreserve.data.model.DataRow;
import org.jreserve.data.model.DataTable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractTriangleModel implements TriangleModel {
    
    protected DataTable table;
    protected boolean cummulated;
    
    private List<TableModelListener> listeners = new ArrayList<TableModelListener>();
    
    @Override
    public int getRowCount() {
        return table==null? 0 : table.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return table==null? 0 : table.getColumnCount()+1;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if(column == 0)
            return Date.class;
        return Double.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
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
    
    @Override
    public Object getValueAt(int row, int column) {
        DataRow dataRow = table.getRow(row);
        if(dataRow == null)
            return null;
        return getValueAt(dataRow, column);
    }

    protected Object getValueAt(DataRow row, int column) {
        if(column == 0)
            return row.getAccidentBegin();
        DataCell cell = getCellAt(row, column);
        return getValue(cell);
    }
    
    protected abstract DataCell getCellAt(DataRow row, int column);
    
    protected Object getValue(DataCell cell) {
        if(cell == null)
            return null;
        return cummulated? cell.getCummulatedValue() : cell.getValue();
    }

    @Override
    public void setDataTable(DataTable table) {
        this.table = table;
    }
    
    @Override
    public void setCummulated(boolean cummulated) {
        this.cummulated = cummulated;
    }
    
    @Override
    public boolean hasValueAt(int row, int column) {
        if(table==null)
            return false;
        if(column == 0)
            return true;
        DataRow tableRow = table.getRow(row);
        if(tableRow == null)
            return false;
        return getCellAt(tableRow, column) != null;
    }
}
