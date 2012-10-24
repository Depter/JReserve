package org.jreserve.data;

import java.util.Date;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Data<O extends PersistentObject, V> implements Comparable<Data> {

    private O owner;
    private Date accidentDate;
    private Date developmentDate;
    private V value;
    
    public Data(O owner, Date accidentDate, Date developmentDate, V value) {
        setOwner(owner);
        setAccidentDate(accidentDate);
        setDevelopmentDate(developmentDate);
        setValue(value);
    }
    
    private void setOwner(O owner) {
        if(owner == null)
            throw new NullPointerException("Owner is null!");
        this.owner = owner;
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
    
    void setValue(V value) {
        if(value == null)
            throw new NullPointerException("Value is null!");
        this.value = value;
    }
    
    public O getOwner() {
        return owner;
    }

    public Date getAccidentDate() {
        return accidentDate;
    }

    public Date getDevelopmentDate() {
        return developmentDate;
    }

    public V getValue() {
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
        
        int dif = accidentDate.compareTo(d.accidentDate);
        
        if(dif != 0) return dif;
        return developmentDate.compareTo(d.developmentDate);
    }

    @Override
    public int hashCode() {
        int hash = 31 + owner.hashCode();
        hash = 17 * hash + accidentDate.hashCode();
        return 17 * hash + developmentDate.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format(
               "Data [%s; %tF; %tF; %f]", 
               owner, accidentDate, developmentDate, value);
    }
}
