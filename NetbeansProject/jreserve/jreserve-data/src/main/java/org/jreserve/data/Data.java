package org.jreserve.data;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Data<T> implements Comparable<Data> {

    private ProjectDataType dataType;
    private Date accidentDate;
    private Date developmentDate;
    private T value;
    
    public Data(ProjectDataType dt, Date accidentDate, Date developmentDate, T value) {
        setDataType(dt);
        setAccidentDate(accidentDate);
        setDevelopmentDate(developmentDate);
        setValue(value);
    }
    
    private void setDataType(ProjectDataType dt) {
        if(dt == null)
            throw new NullPointerException("ProjectDataType is null!");
        this.dataType = dt;
    }
    
    private void setAccidentDate(Date date) {
        if(date == null)
            throw new NullPointerException("AccidentDate is null!");
        this.accidentDate = date;
    }
    
    private void setDevelopmentDate(Date date) {
        if(date == null)
            throw new NullPointerException("DevelopmentDate is null!");
        this.developmentDate = date;
    }
    
    void setValue(T value) {
        if(value == null)
            throw new NullPointerException("Value is null!");
        this.value = value;
    }
    
    public ProjectDataType getDataType() {
        return dataType;
    }

    public Date getAccidentDate() {
        return accidentDate;
    }

    public Date getDevelopmentDate() {
        return developmentDate;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Data)
            return compareTo((Data)o) == 0;
        return false;
    }
    
    @Override
    public int compareTo(Data d) {
        if(d == null) return -1;
        
        int dif = dataType.compareTo(d.dataType);
        if(dif != 0) return dif;
        
        dif = accidentDate.compareTo(d.accidentDate);
        if(dif != 0) return dif;
        return developmentDate.compareTo(d.developmentDate);
    }

    @Override
    public int hashCode() {
        int hash = 31 + dataType.hashCode();
        hash = 17 * hash + accidentDate.hashCode();
        return 17 * hash + developmentDate.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format(
               "Data [%s; %tF; %tF; %f]", 
               dataType, accidentDate, developmentDate, value);
    }
}
