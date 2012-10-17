package org.jreserve.triangle.mvc.controller;

import javax.swing.event.ChangeListener;
import org.jreserve.triangle.mvc.model.AbstractCell;
import org.jreserve.triangle.mvc.model.TriangleTable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleTableModel {
    
    public void setTable(TriangleTable table);
    
    public TriangleTable getTable();
    
    public int getRowCount();
    
    public int getColumnCount();

    public Object getRowTitle(int row);

    public Object getColumnTitle(int column);
    
    public boolean hasValueAt(int row, int column);
    
    public AbstractCell getCellAt(int row, int column);
    
    public void addChangeListener(ChangeListener listener);
    
    public void removeChangeListener(ChangeListener listener);
}
