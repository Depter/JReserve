package org.jreserve.triangle.mvc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.jreserve.data.Data;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleRow<V> implements Comparable<TriangleRow> {
    
    private TriangleTable table;
    
    private final Date accidentBegin;
    private final Date accidentEnd;
    
    private List<TriangleCell<V>> cells = new ArrayList<TriangleCell<V>>();
    private int cellCount;
    
    TriangleRow(Date accidentBegin, Date accidentEnd) {
        this.accidentBegin = accidentBegin;
        this.accidentEnd = accidentEnd;
    }
    
    void setTable(TriangleTable<V> table) {
        this.table = table;
    }
    
    public TriangleTable<V> getTable() {
        return table;
    }
    
    public Date getAccidentBegin() {
        return accidentBegin;
    }
    
    public Date getAccidentEnd() {
        return accidentEnd;
    }
    
    void addCell(TriangleCell<V> cell) {
        cells.add(cell);
        Collections.sort(cells);
        cellCount++;
        cell.setRow(this);
    }
    
    public int getCellCount() {
        return cellCount;
    }
    
    public TriangleCell<V> getCell(int column) {
        if(column <0 || cellCount <= column)
            return null;
        return cells.get(column);
    }
    
    public TriangleCell<V> getCell(Date developmentDate) {
        for(TriangleCell<V> cell : cells)
            if(cell.containsDate(developmentDate))
                return cell;
        return null;
    }
    
    boolean containsDate(Date accidentDate) {
        return !accidentDate.before(accidentBegin) &&
                accidentDate.before(accidentEnd);
    }
    
    TriangleCell<V> getPreviousCell(TriangleCell<V> cell) {
        int index = Collections.binarySearch(cells, cell);
        if(index > 0)
            return cells.get(index-1);
        return null;
    }
    
    public <O extends PersistentObject, T> List<Data<O, T>> getRelevantData(List<Data<O, T>> values) {
        List<Data<O, T>> result = new ArrayList<Data<O, T>>(values.size());
        for(Data<O, T> value : values)
            if(containsDate(value.getAccidentDate()))
                result.add(value);
        return result;
    }
    
    public V getValue(int column) {
        TriangleCell<V> cell = getCell(column);
        return cell==null? null : cell.getValue();
    }
    
    public V getValue(Date developmentDate) {
        TriangleCell<V> cell = getCell(developmentDate);
        return cell==null? null : cell.getValue();
    }
    
    public void setValue(V value, int column) {
        TriangleCell<V> cell = getCell(column);
        if(cell == null)
            throw new IllegalArgumentException("Cell not found for column: "+column);
        cell.setValue(value);
    }
    
    public void setValue(V value, Date developmentDate) {
        TriangleCell<V> cell = getCell(developmentDate);
        if(cell == null)
            throw new IllegalArgumentException(String.format("Cell not found for date: %tF", developmentDate));
        cell.setValue(value);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof TriangleRow)
            return compareTo((TriangleRow)o) == 0;
        return false;
    }
    
    @Override
    public int compareTo(TriangleRow row) {
        return accidentBegin.compareTo(row.accidentBegin);
    }
    
    @Override
    public int hashCode() {
        return accidentBegin.hashCode();
    }
    
    @Override
    public String toString() {
        String format = "DataRow [%tF - %tF]";
        return String.format(format, accidentBegin, accidentEnd);
    }
}
