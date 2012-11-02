package org.jreserve.triangle.widget;

import java.util.Date;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class WidgetData<T> {

    private Date accident;
    private Date development;
    private T value;
    
    public WidgetData(Date accident, Date development, T value) {
        this.accident = accident;
        this.development = development;
        this.value = value;
    }
    
    public Date getAccident() {
        return accident;
    }
    
    public Date getDevelopment() {
        return development;
    }
    
    public T getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof WidgetData)
            return equals((WidgetData) o);
        return false;
    }
    
    private boolean equals(WidgetData d) {
        if(!accident.equals(d.accident) || !development.equals(d.development))
            return false;
        if(value == null) return d.value==null;
        return value.equals(d.value);
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + accident.hashCode();
        hash = 17 * hash + development.hashCode();
        return value==null? hash :  17 * hash + value.hashCode(); 
    }
    
    @Override
    public String toString() {
        return String.format("WidgetData [%tF/%tF]: %s", accident, development, value);
    }
}
