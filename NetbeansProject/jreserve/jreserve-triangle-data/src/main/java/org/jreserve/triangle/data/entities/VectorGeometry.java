package org.jreserve.triangle.data.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Embeddable
@MappedSuperclass
public class VectorGeometry implements Serializable, Cloneable  {
    private final static long serialVersionUID = 1L;
    
    private final static String ERR_PERIODS = 
         "There must be at least 1 accident period, but %d was found!";
    private final static String ERR_ACC_MONTH = 
         "There must be at least 1 month in an accident period, but %d was found!";

    @Column(name="START_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date start;
    
    @Column(name="ACCIDENT_PERIODS", nullable=false)
    private int accidentPeriods;
    
    @Column(name="MONTH_IN_ACCIDENT", nullable=false)
    private int accidentMonths;
    
    protected VectorGeometry() {
    }
    
    public VectorGeometry(Date start, int accidentPeriods, int monthInAccident) {
        initStartDate(start);
        initAccidnetPeriods(accidentPeriods);
        initAccidentMonths(monthInAccident);
    }
    
    private void initStartDate(Date start) {
        if(start == null)
            throw new NullPointerException("Accident start date is null!");
        this.start = start;
    }
    
    private void initAccidnetPeriods(int periods) {
        if(periods  < 1)
            throw new IllegalArgumentException(String.format(ERR_PERIODS, periods));
        this.accidentPeriods = periods;
    }
    
    private void initAccidentMonths(int month) {
        if(month  < 1)
            throw new IllegalArgumentException(String.format(ERR_ACC_MONTH, month));
        this.accidentMonths = month;
    }

    public Date getStartDate() {
        return start;
    }

    public void setStartDate(Date startDate) {
        initStartDate(startDate);
    }

    public int getAccidentPeriods() {
        return accidentPeriods;
    }

    public void setAccidentPeriods(int periods) {
        initAccidnetPeriods(periods);
    }

    public int getAccidentMonths() {
        return accidentMonths;
    }

    public void setAccidentMonths(int accidentMonths) {
        initAccidentMonths(accidentMonths);
    }
    
    public boolean isEqualGeometry(VectorGeometry g) {
        if(g==null)
            return false;
        return start.equals(g.start) &&
               accidentPeriods == g.accidentPeriods &&
               accidentMonths == g.accidentMonths;
    }
    
    public VectorGeometry copy() {
        return new VectorGeometry(start, accidentPeriods, accidentMonths);
    }
    
    @Override
    public String toString() {
        return String.format("Geometry [%tF; %d; %d]",
            start, accidentPeriods, accidentMonths);
    }
}
