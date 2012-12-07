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
public class TriangleGeometry implements Serializable {
    private final static long serialVersionUID = 1L;
    
    private final static String ERR_ACC_PERIODS = 
         "There must be at least 1 accident period, but %d was found!";
    private final static String ERR_ACC_MONTH = 
         "There must be at least 1 month in an accident period, but %d was found!";
    private final static String ERR_DEV_PERIODS = 
         "There must be at least 1 development period, but %d was found!";
    private final static String ERR_DEV_MONTH = 
         "There must be at least 1 month in a development period, but %d was found!";

    @Column(name="START_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date start;
    
    @Column(name="ACCIDENT_PERIODS", nullable=false)
    private int accidentPeriods;
    
    @Column(name="MONTH_IN_ACCIDENT", nullable=false)
    private int accidentMonths;
    
    @Column(name="DEVELOPMENT_PERIODS", nullable=false)
    private int developmentPeriods;

    @Column(name="MONTH_IN_DEVELOPMENT", nullable=false)
    private int developmentMonths;
    
    protected TriangleGeometry() {
    }
    
    public TriangleGeometry(Date start, int periods, int months) {
        this(start, periods, months, periods, months);
    }
    
    public TriangleGeometry(Date start, int accidentPeriods, int accidentMonths, int developmentPeriods, int developmentMonths) {
        initStartDate(start);
        initAccidnetPeriods(accidentPeriods);
        initAccidentMonths(accidentMonths);
        initDevelopmentPeriods(developmentPeriods);
        initMonthInDevelopment(developmentMonths);
    }
    
    private void initStartDate(Date start) {
        if(start == null)
            throw new NullPointerException("Accident start date is null!");
        this.start = start;
    }
    
    private void initAccidnetPeriods(int periods) {
        if(periods  < 1)
            throw new IllegalArgumentException(String.format(ERR_ACC_PERIODS, periods));
        this.accidentPeriods = periods;
    }
    
    private void initAccidentMonths(int month) {
        if(month  < 1)
            throw new IllegalArgumentException(String.format(ERR_ACC_MONTH, month));
        this.accidentMonths = month;
    }
    
    private void initDevelopmentPeriods(int periods) {
        if(periods  < 1)
            throw new IllegalArgumentException(String.format(ERR_DEV_PERIODS, periods));
        this.developmentPeriods = periods;
    }
    
    private void initMonthInDevelopment(int month) {
        if(month  < 1)
            throw new IllegalArgumentException(String.format(ERR_DEV_MONTH, month));
        this.developmentMonths = month;
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

    public int getDevelopmentPeriods() {
        return developmentPeriods;
    }

    public void setDevelopmentPeriods(int periods) {
        initDevelopmentPeriods(periods);
    }

    public int getDevelopmentMonths() {
        return developmentMonths;
    }

    public void setDevelopmentMonths(int month) {
        initMonthInDevelopment(month);
    }
    
    public TriangleGeometry copy() {
        TriangleGeometry copy = new TriangleGeometry();
        copy.start = start;
        copy.accidentPeriods = accidentPeriods;
        copy.accidentMonths = accidentMonths;
        copy.developmentPeriods = developmentPeriods;
        copy.developmentMonths = developmentMonths;
        return copy;
    }
    
    public boolean isEqualGeometry(TriangleGeometry g) {
        if(g==null)
            return false;
        return start.equals(g.start) &&
               accidentPeriods == g.accidentPeriods &&
               accidentMonths == g.accidentMonths &&
               developmentPeriods == g.developmentPeriods &&
               developmentMonths == g.developmentMonths;
    }
    
    @Override
    public String toString() {
        return String.format("Geometry [%tF] [%d; %d] / [%d; %d]",
            start, 
            accidentPeriods, accidentMonths, 
            developmentPeriods, developmentMonths);
    }
}
