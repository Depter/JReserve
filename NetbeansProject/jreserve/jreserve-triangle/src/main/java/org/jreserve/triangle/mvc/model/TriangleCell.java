package org.jreserve.triangle.mvc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jreserve.data.Data;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCell<V> implements Comparable<TriangleCell> {
    
    protected TriangleRow row;
    protected Date developmentBegin;
    protected Date developmentEnd;
    protected V value;
    
    TriangleCell(Date developmentBegin, Date developmentEnd) {
        this.developmentBegin = developmentBegin;
        this.developmentEnd = developmentEnd;
    }
    
    void setRow(TriangleRow row) {
        this.row = row;
    }
    
    public TriangleRow<V> getRow() {
        return row;
    }
    
    public TriangleCell<V> getPreviousCell() {
        if(row == null)
            return null;
        return row.getPreviousCell(this);
    }
    
    public Date getDevelopmentBegin() {
        return developmentBegin;
    }
    
    public Date getDevelopmentEnd() {
        return developmentEnd;
    }
    
    protected boolean containsDate(Date developmentDate) {
        return !developmentDate.before(developmentBegin) &&
                developmentDate.before(developmentEnd);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof TriangleCell)
            return compareTo((TriangleCell)o) == 0;
        return false;
    }
    
    @Override
    public int compareTo(TriangleCell cell) {
        return developmentBegin.compareTo(cell.developmentBegin);
    }
    
    @Override
    public int hashCode() {
        return developmentBegin.hashCode();
    }
    
    @Override
    public String toString() {
        String format = "DataCell [%tF - %tF]";
        return String.format(format, developmentBegin, developmentEnd);
    }
    
    public <O extends PersistentObject, T> List<Data<O, T>> getRelevantData(List<Data<O, T>> values) {
        List<Data<O, T>> result = new ArrayList<Data<O, T>>(values.size());
        for(Data<O, T> data : values)
            if(isRelevant(data))
                result.add(data);
        return result;
    }
    
    private boolean isRelevant(Data data) {
        return row.containsDate(data.getAccidentDate()) &&
               containsDate(data.getDevelopmentDate());
    }
    
    public V getValue() {
        return value;
    }
    
    public void setValue(V value) {
        this.value = value;
    }
}
