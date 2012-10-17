package org.jreserve.triangle.mvc.model;

import java.util.Date;
import org.jreserve.data.Data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractCell implements Comparable<AbstractCell> {
    
    protected TriangleRow row;
    protected Date developmentBegin;
    protected Date developmentEnd;
    
    AbstractCell(Date developmentBegin, Date developmentEnd) {
        this.developmentBegin = developmentBegin;
        this.developmentEnd = developmentEnd;
    }
    
    void setRow(TriangleRow row) {
        this.row = row;
    }
    
    public TriangleRow getRow() {
        return row;
    }
    
    public Date getDevelopmentBegin() {
        return developmentBegin;
    }
    
    public Date getDevelopmentEnd() {
        return developmentEnd;
    }
    
    protected boolean myData(Data data) {
        Date date = data.getDevelopmentDate();
        return !developmentBegin.after(date) &&
                developmentEnd.after(date);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof AbstractCell)
            return compareTo((AbstractCell)o) == 0;
        return false;
    }
    
    @Override
    public int compareTo(AbstractCell row) {
        return developmentBegin.compareTo(row.developmentBegin);
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
}
