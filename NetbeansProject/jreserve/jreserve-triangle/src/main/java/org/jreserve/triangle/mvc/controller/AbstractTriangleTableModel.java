package org.jreserve.triangle.mvc.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.triangle.mvc.model.TriangleCell;
import org.jreserve.triangle.mvc.model.TriangleRow;
import org.jreserve.triangle.mvc.model.TriangleTable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractTriangleTableModel<V> implements TriangleTableModel<V> {

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    protected List<Date> accidentDates = new ArrayList<Date>();
    protected List<Date> developmentDates = new ArrayList<Date>();
    protected TriangleTable<V> table;
    
    protected AbstractTriangleTableModel(TriangleTable<V> table) {
        if(table == null)
            throw new NullPointerException("TriangleTable can not be null!");
        this.table = table;
        initModel();
    }

    @Override
    public void setTable(TriangleTable<V> table) {
        if(table == null)
            throw new NullPointerException("TriangleTable can not be null!");
        this.table = table;
        initModel();
        fireChange();
    }
    
    @Override
    public TriangleTable<V> getTable() {
        return table;
    }
    
    private void initModel() {
        clearDates();
        if(table != null)
            queryTable();
        sortDates();
    }
    
    private void clearDates() {
        accidentDates.clear();
        developmentDates.clear();
    }
    
    private void queryTable() {
        for(int r=0, rCount = table.getRowCount(); r<rCount; r++)
            queryRow(table.getRow(r));
    }
    
    private void queryRow(TriangleRow row) {
        accidentDates.add(row.getAccidentBegin());
        for(int c=0, cCount=row.getCellCount(); c<cCount; c++)
            queryCell(row.getCell(c));
    }
    
    private void queryCell(TriangleCell cell) {
        Date date = cell.getDevelopmentBegin();
        if(!developmentDates.contains(date))
            developmentDates.add(date);
    }
    
    private void sortDates() {
        Collections.sort(accidentDates);
        Collections.sort(developmentDates);
    }
    
    @Override
    public void addChangeListener(ChangeListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    protected void fireChange() {
        ChangeEvent evt = new ChangeEvent(this);
        for(ChangeListener listener : new ArrayList<ChangeListener>(listeners))
            listener.stateChanged(evt);
    }
    
    @Override
    public int getRowCount() {
        return accidentDates.size();
    }

    @Override
    public int getColumnCount() {
        return developmentDates.size();
    }
    
    @Override
    public boolean hasCellAt(int row, int column) {
        return getCellAt(row, column) != null;
    }

    @Override
    public Object getRowTitle(int row) {
        return Integer.valueOf(row + 1);
    }

    @Override
    public Object getColumnTitle(int column) {
        return Integer.valueOf(column+1);
    }
}
