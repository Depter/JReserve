package org.jreserve.data;

import java.util.Date;
import org.jreserve.data.entities.ProjectDataType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Data implements Comparable<Data> {

    private ProjectDataType dataType;
    private Date accidentDate;
    private Date developmentDate;
    private double value = Double.NaN;
    
    public ProjectDataType getDataType() {
        return dataType;
    }
    
    public Data setDataType(ProjectDataType dt) {
        this.dataType = dt;
        return this;
    }

    public Date getAccidentDate() {
        return accidentDate;
    }
    
    public Data setAccidentDate(Date date) {
        this.accidentDate = date;
        return this;
    }

    public Date getDevelopmentDate() {
        return developmentDate;
    }
    
    public Data setDevelopmentDate(Date date) {
        this.developmentDate = date;
        return this;
    }

    public double getValue() {
        return value;
    }
    
    public Data setValue(double value) {
        this.value = value;
        return this;
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
