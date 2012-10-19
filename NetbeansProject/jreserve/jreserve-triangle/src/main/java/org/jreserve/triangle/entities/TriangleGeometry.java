package org.jreserve.triangle.entities;

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
public class TriangleGeometry extends VectorGeometry {
    private final static long serialVersionUID = 1L;
    
    private final static String ERR_PERIODS = 
         "There must be at least 1 development period, but %d was found!";
    private final static String ERR_DEV_MONTH = 
         "There must be at least 1 month in a development period, but %d was found!";

    @Column(name="DEVELOPMENT_START_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date developmentStart;
    
    @Column(name="DEVELOPMENT_PERIODS", nullable=false)
    private int developmentPeriods;

    @Column(name="MONTH_IN_DEVELOPMENT", nullable=false)
    private int monthInDevelopment;
    
    protected TriangleGeometry() {
    }
    
    public TriangleGeometry(Date start, int periods, int months) {
        super(start, periods, months);
        initStartDate(start);
        initDevelopmentPeriods(periods);
        initMonthInDevelopment(months);
    }
    
    public TriangleGeometry(Date accidentStart, int accidentPeriods, int monthInAccident, Date developmentStart, int developmentPeriods, int monthInDevelopment) {
        super(accidentStart, accidentPeriods, monthInAccident);
        initStartDate(developmentStart);
        initDevelopmentPeriods(developmentPeriods);
        initMonthInDevelopment(monthInDevelopment);
    }
    
    private void initStartDate(Date start) {
        if(start == null)
            throw new NullPointerException("Development start date is null!");
        this.developmentStart = start;
    }
    
    private void initDevelopmentPeriods(int periods) {
        if(periods  < 1)
            throw new IllegalArgumentException(String.format(ERR_PERIODS, periods));
        this.developmentPeriods = periods;
    }
    
    private void initMonthInDevelopment(int month) {
        if(month  < 1)
            throw new IllegalArgumentException(String.format(ERR_DEV_MONTH, month));
        this.monthInDevelopment = month;
    }

    public Date getDevelopmentStart() {
        return developmentStart;
    }

    public void setDevelopmentStart(Date startDate) {
        initStartDate(startDate);
    }

    public int getDevelopmentPeriods() {
        return developmentPeriods;
    }

    public void setDevelopmentPeriods(int periods) {
        initDevelopmentPeriods(periods);
    }

    public int getMonthInDevelopment() {
        return monthInDevelopment;
    }

    public void setMonthInDevelopment(int month) {
        initMonthInDevelopment(month);
    }
    
    public TriangleGeometry copy() {
        Date aStart = super.getAccidentStart();
        int aPeriods = super.getAccidentPeriods();
        int aMonth = super.getMonthInAccident();
        return new TriangleGeometry(aStart, aPeriods, aMonth, developmentStart, developmentPeriods, monthInDevelopment);
    }
    
    public boolean isEqualGeometry(TriangleGeometry g) {
        if(!super.isEqualGeometry(g))
            return false;
        return developmentStart.equals(g.developmentStart) &&
               developmentPeriods == g.developmentPeriods &&
               monthInDevelopment == g.monthInDevelopment;
    }
    
    @Override
    public String toString() {
        return String.format("Geometry [%tF; %d; %d] / [%tF; %d; %d]",
            getAccidentStart(), getAccidentPeriods(), getMonthInAccident(), 
            developmentStart, developmentPeriods, monthInDevelopment);
    }
}