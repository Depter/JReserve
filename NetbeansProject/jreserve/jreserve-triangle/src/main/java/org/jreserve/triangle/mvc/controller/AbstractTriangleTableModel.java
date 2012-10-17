package org.jreserve.triangle.mvc.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jreserve.triangle.mvc.model.AbstractCell;
import org.jreserve.triangle.mvc.model.TriangleRow;
import org.jreserve.triangle.mvc.model.TriangleTable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractTriangleTableModel implements TriangleTableModel {

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
    protected List<Date> accidentDates = new ArrayList<Date>();
    protected List<Date> developmentDates = new ArrayList<Date>();
    protected TriangleTable table;
    
    protected AbstractTriangleTableModel(TriangleTable table) {
        if(table == null)
            throw new NullPointerException("TriangleTable can not be null!");
        this.table = table;
        initModel();
    }

    @Override
    public void setTable(TriangleTable table) {
        if(table == null)
            throw new NullPointerException("TriangleTable can not be null!");
        this.table = table;
        initModel();
        fireChange();
    }
    
    @Override
    public TriangleTable getTable() {
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
    
    private void queryCell(AbstractCell cell) {
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
    public boolean hasValueAt(int row, int column) {
        return getCellAt(row, column) != null;
    }
}
