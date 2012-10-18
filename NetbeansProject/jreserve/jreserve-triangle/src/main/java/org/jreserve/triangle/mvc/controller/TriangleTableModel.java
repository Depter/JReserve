package org.jreserve.triangle.mvc.controller;

import javax.swing.event.ChangeListener;
import org.jreserve.triangle.mvc.model.TriangleCell;
import org.jreserve.triangle.mvc.model.TriangleTable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleTableModel<V> {
    
    public void setTable(TriangleTable<V> table);
    
    public TriangleTable<V> getTable();
    
    public int getRowCount();
    
    public int getColumnCount();

    public Object getRowTitle(int row);

    public Object getColumnTitle(int column);
    
    public boolean hasCellAt(int row, int column);
    
    public TriangleCell<V> getCellAt(int row, int column);
    
    public void addChangeListener(ChangeListener listener);
    
    public void removeChangeListener(ChangeListener listener);
}
