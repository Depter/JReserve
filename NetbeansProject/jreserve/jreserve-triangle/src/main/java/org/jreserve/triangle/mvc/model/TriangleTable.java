package org.jreserve.triangle.mvc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleTable<V> {

    private List<TriangleRow<V>> rows = new ArrayList<TriangleRow<V>>();
    private int rowCount = 0;
    private int columnCount = 0;
    
    public TriangleTable() {
    }
    
    void addRow(TriangleRow row) {
        rows.add(row);
        row.setTable(this);
        Collections.sort(rows);
        rowCount++;
        setColumnCount(row.getCellCount());
    }
    
    private void setColumnCount(int cellCount) {
        if(cellCount > columnCount)
            columnCount = cellCount;
    }
    
    public int getRowCount() {
        return rowCount;
    }
    
    public int getColumnCount() {
        return columnCount;
    }
    
    public int getColumnCount(int row) {
        return rows.get(row).getCellCount();
    }
    
    public TriangleRow<V> getRow(int row) {
        if(row < 0 || rowCount <= row)
            return null;
        return rows.get(row);
    }
    
    public TriangleCell<V> getCell(int row, int column) {
        TriangleRow<V> r = getRow(row);
        return r==null? null : r.getCell(column);
    }
    
    public TriangleRow<V> getRow(Date accidentDate) {
        for(TriangleRow<V> row : rows)
            if(row.containsDate(accidentDate))
                return row;
        return null;
    }
    
    public TriangleCell<V> getCell(Date accidentdate, Date developmentDate) {
        TriangleRow<V> row = getRow(accidentdate);
        return row==null? null : row.getCell(developmentDate);
    }
    
    
    public V getValue(int row, int column) {
        TriangleRow<V> r = getRow(row);
        return r==null? null : r.getValue(column);
    }
    
    public V getValue(Date accidentDate, Date developmentDate) {
        TriangleRow<V> row = getRow(accidentDate);
        return row==null? null : row.getValue(developmentDate);
    }
    
    public void setValue(V value, int row, int column) {
        TriangleRow<V> r = getRow(row);
        if(r == null)
            throw new IllegalArgumentException("Row not found for index: "+row);
        r.setValue(value, column);
    }
    
    public void setValue(V value, Date accidentdate, Date developmentDate) {
        TriangleRow<V> row = getRow(accidentdate);
        if(row == null)
            throw new IllegalArgumentException(String.format("Row not found for date: %tF", accidentdate));
        row.setValue(value, developmentDate);
    }
}
