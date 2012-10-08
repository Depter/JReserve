package org.jreserve.triangle.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Embeddable
public class VectorGeometry implements Serializable  {
    private final static long serialVersionUID = 1L;
    
    private final static String ERR_PERIODS = 
         "There must be at least 1 accident period, but %d was found!";
    private final static String ERR_ACC_MONTH = 
         "There must be at least 1 month in an accident period, but %d was found!";

    @Column(name="ACCIDENT_START_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date accidentStart;
    
    @Column(name="ACCIDENT_PERIODS", nullable=false)
    private int accidentPeriods;
    
    @Column(name="MONTH_IN_ACCIDENT", nullable=false)
    private int monthInAccident;
    
    protected VectorGeometry() {
    }
    
    public VectorGeometry(Date start, int accidentPeriods, int monthInAccident) {
        initStartDate(start);
        initAccidnetPeriods(accidentPeriods);
        initMonthInAccident(monthInAccident);
    }
    
    private void initStartDate(Date start) {
        if(start == null)
            throw new NullPointerException("Accident start date is null!");
        this.accidentStart = start;
    }
    
    private void initAccidnetPeriods(int periods) {
        if(periods  < 1)
            throw new IllegalArgumentException(String.format(ERR_PERIODS, periods));
        this.accidentPeriods = periods;
    }
    
    private void initMonthInAccident(int month) {
        if(month  < 1)
            throw new IllegalArgumentException(String.format(ERR_ACC_MONTH, month));
        this.monthInAccident = month;
    }

    public Date getAccidentStart() {
        return accidentStart;
    }

    public void setAccidentStart(Date startDate) {
        initStartDate(startDate);
    }

    public int getAccidentPeriods() {
        return accidentPeriods;
    }

    public void setAccidentPeriods(int periods) {
        initAccidnetPeriods(periods);
    }

    public int getMonthInAccident() {
        return monthInAccident;
    }

    public void setMonthInAccident(int monthInAccident) {
        initMonthInAccident(monthInAccident);
    }
    
    @Override
    public String toString() {
        return String.format("Geometry [%tF; %d; %d]",
            accidentStart, accidentPeriods, monthInAccident);
    }
}
