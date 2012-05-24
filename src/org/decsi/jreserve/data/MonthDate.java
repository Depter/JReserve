package org.decsi.jreserve.data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MonthDate implements Comparable<MonthDate> {
    
    private int year;
    private int month;
    
    public MonthDate(int year, int month) {
        this.year = year;
        this.month = month;
        assert month>=0 && month<=11 : String.format("Month %d is out of bounds [0,11]!", month);
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public int compareTo(MonthDate o) {
        if(o == null)
            return -1;
        int dif = year - o.year;
        return dif!=0? dif : month - o.month;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof MonthDate)
            return compareTo((MonthDate)o) == 0;
        return false;
    }
    
    @Override
    public int hashCode() {
        return 17 * (31 + year) + month;
    }
    
    @Override
    public String toString() {
        return String.format("%d-%02d", year, month+1);
    }
}
