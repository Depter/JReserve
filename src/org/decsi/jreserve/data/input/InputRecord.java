package org.decsi.jreserve.data.input;

import org.decsi.jreserve.data.MonthDate;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class InputRecord {
    
    private MonthDate accidentPeriod;
    private MonthDate developmentPeriod;
    private double value;
    
    InputRecord(MonthDate accidentPeriod, MonthDate developmentPeriod, double value) {
        this.accidentPeriod = accidentPeriod;
        this.developmentPeriod = developmentPeriod;
        this.value = value;
    }
    
    public MonthDate getAccidentPeriod() {
        return accidentPeriod;
    }
    
    public MonthDate getDevelopmentPeriod() {
        return developmentPeriod;
    }
    
    public double getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof InputRecord)
            return equals((InputRecord) o);
        return false;
    }
    
    public boolean equals(InputRecord record) {
        if(record == null)
            return false;
        return accidentPeriod.equals(record.accidentPeriod) &&
               developmentPeriod.equals(record.developmentPeriod);
    }
    
    @Override
    public int hashCode() {
        int hash = 31+accidentPeriod.hashCode();
        return 17 * hash + developmentPeriod.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("InputRecord [%s, %s, %f]", 
                accidentPeriod, developmentPeriod, value);
    }
}