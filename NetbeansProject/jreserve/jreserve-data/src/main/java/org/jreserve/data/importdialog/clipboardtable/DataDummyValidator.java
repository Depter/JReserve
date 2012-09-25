package org.jreserve.data.importdialog.clipboardtable;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DataDummyValidator {
    
    private List<DataDummy> dummies;
    private int size;
    private DateFormat df;
    private NumberFormat nf;
    private boolean isTriangle;
    
    private Set<Integer> errorRows;
    private boolean valid = true;
    private Set<DateDummy> dateDummies = new HashSet<DateDummy>();
    
    DataDummyValidator(List<DataDummy> dummies, boolean isTriangle) {
        this.size = dummies.size();
        this.dummies = dummies;
        this.isTriangle = isTriangle;
    }
    
    DataDummyValidator setDateFormat(DateFormat df) {
        this.df = df;
        return this;
    }
    
    DataDummyValidator setNumberFormat(NumberFormat nf) {
        this.nf = nf;
        return this;
    }
    
    boolean checkDummies() {
        initCheck();
        return check();
    }
    
    int[] getErrorRows() {
        if(errorRows == null)
            throw new IllegalStateException("checkDummies() must be called first!");
        int[] result = new int[errorRows.size()];
        int i=0;
        for(Integer row : errorRows)
            result[i++] = row;
        return result;
    }
    
    private void initCheck() {
        errorRows = new TreeSet<Integer>();
        dateDummies.clear();
    }
    
    private boolean check() {
        for(int i=0; i<size; i++) {
            if(!checkDummy(dummies.get(i)))
                errorRows.add(i);
        }
        return errorRows.isEmpty();
    }
    
    private boolean checkDummy(DataDummy dummy) {
        try {
            Date accident = df.parse(dummy.getAccident());
            Date development = df.parse(dummy.getDevelopment());
            double value = nf.parse(dummy.getValue()).doubleValue();
            return checkDates(accident, development);
        } catch (ParseException ex) {
            return false;
        }
    }
    
    private boolean checkDates(Date accident, Date development) {
        return isNewValue(accident, development) &&
               isValidDates(accident, development);
    }
    
    private boolean isNewValue(Date accident, Date development) {
        DateDummy d = new DateDummy(accident, development);
        return dateDummies.add(d);
    }
    
    private boolean isValidDates(Date accident, Date development) {
        if(isTriangle) {
            return !accident.after(development);
        } else {
            return accident.equals(development);
        }
    }
    
    private static class DateDummy {
        private Date accident;
        private Date development;
        
        private DateDummy(Date accidnet, Date development) {
            this.accident = accidnet;
            this.development = development;
        }
        
        @Override
        public boolean equals(Object o) {
            if(o instanceof DateDummy) {
                DateDummy d = (DateDummy) o;
                return accident.equals(d.accident) &&
                       development.equals(d.development);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int hash = 31 + accident.hashCode();
            return 17 * hash + development.hashCode();
        }
    }
}
