package org.jreserve.dataimport.clipboardtable;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataDummy implements Comparable<DataDummy> {

    private String accident;
    private String development;
    private String value;

    String getAccident() {
        return accident;
    }

    void setAccident(String accident) {
        this.accident = accident;
    }

    boolean isAccidentValid(DateFormat format) {
        return isFormatValid(format, accident);
    }
    
    private boolean isFormatValid(Format format, String str) {
        if(str == null)
            return false;
        try {
            format.parseObject(str);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }
    
    String getDevelopment() {
        return development;
    }

    void setDevelopment(String development) {
        this.development = development;
    }

    boolean isDevelopmentValid(DateFormat format) {
        return isFormatValid(format, development);
    }

    String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    boolean isValueValid(NumberFormat format) {
        return isFormatValid(format, value);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof DataDummy)
            return compareTo((DataDummy) o) == 0;
        return false;
    }

    @Override
    public int compareTo(DataDummy o) {
        int dif = accident.compareTo(o.accident);
        return dif != 0 ? dif : development.compareTo(o.development);
    }

    @Override
    public int hashCode() {
        int hash = 31 + accident.hashCode();
        return 17 * hash + development.hashCode();
    }
}
