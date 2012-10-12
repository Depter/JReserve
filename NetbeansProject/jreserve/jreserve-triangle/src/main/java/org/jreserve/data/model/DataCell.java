package org.jreserve.data.model;

import java.util.Date;
import java.util.List;
import org.jreserve.data.Data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DataCell implements Comparable<DataCell> {
    
    private final static double DEFAULT_VALUE = 0d;
    
    private DataRow row;
    private Date developmentBegin;
    private Date developmentEnd;
    private double value = DEFAULT_VALUE;
    
    DataCell(Date developmentBegin, Date developmentEnd) {
        this.developmentBegin = developmentBegin;
        this.developmentEnd = developmentEnd;
    }
    
    void setRow(DataRow row) {
        this.row = row;
    }
    
    public DataRow getRow() {
        return row;
    }
    
    public Date getDevelopmentBegin() {
        return developmentBegin;
    }
    
    public Date getDevelopmentEnd() {
        return developmentEnd;
    }
    
    public double getValue() {
        return value;
    }
    
    void setValues(List<Data> datas) {
        value = DEFAULT_VALUE;
        for(Data data : datas)
            if(myData(data))
                addData(data);
    }
    
    private boolean myData(Data data) {
        Date date = data.getDevelopmentDate();
        return !developmentBegin.after(date) &&
                developmentEnd.after(date);
    }
    
    private void addData(Data data) {
        double dataValue = data.getValue();
        if(!Double.isNaN(dataValue))
            value += dataValue;
    }
    
    public double getCummulatedValue() {
        double previous = getPreviousValue();
        if(Double.isNaN(previous))
            return value;
        return previous + value;
    }
    
    private double getPreviousValue() {
        if(row == null)
            return Double.NaN;
        DataCell previous = row.getPreviousCell(this);
        return previous==null? Double.NaN : previous.getCummulatedValue();
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof DataCell)
            return compareTo((DataCell)o) == 0;
        return false;
    }
    
    @Override
    public int compareTo(DataCell row) {
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
