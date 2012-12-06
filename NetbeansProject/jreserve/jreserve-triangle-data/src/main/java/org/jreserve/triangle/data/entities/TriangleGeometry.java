package org.jreserve.triangle.data.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

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
    
    @Column(name="DEVELOPMENT_PERIODS", nullable=false)
    private int developmentPeriods;

    @Column(name="MONTH_IN_DEVELOPMENT", nullable=false)
    private int developmentMonths;
    
    protected TriangleGeometry() {
    }
    
    public TriangleGeometry(Date start, int periods, int months) {
        super(start, periods, months);
        initDevelopmentPeriods(periods);
        initMonthInDevelopment(months);
    }
    
    public TriangleGeometry(Date start, int accidentPeriods, int accidentMonths, int developmentPeriods, int developmentMonths) {
        super(start, accidentPeriods, accidentMonths);
        initDevelopmentPeriods(developmentPeriods);
        initMonthInDevelopment(developmentMonths);
    }
    
    private void initDevelopmentPeriods(int periods) {
        if(periods  < 1)
            throw new IllegalArgumentException(String.format(ERR_PERIODS, periods));
        this.developmentPeriods = periods;
    }
    
    private void initMonthInDevelopment(int month) {
        if(month  < 1)
            throw new IllegalArgumentException(String.format(ERR_DEV_MONTH, month));
        this.developmentMonths = month;
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
    
    @Override
    public TriangleGeometry copy() {
        Date aStart = super.getStartDate();
        int aPeriods = super.getAccidentPeriods();
        int aMonth = super.getAccidentMonths();
        return new TriangleGeometry(aStart, aPeriods, aMonth, developmentPeriods, developmentMonths);
    }
    
    public boolean isEqualGeometry(TriangleGeometry g) {
        if(!super.isEqualGeometry(g))
            return false;
        return developmentPeriods == g.developmentPeriods &&
               developmentMonths == g.developmentMonths;
    }
    
    @Override
    public String toString() {
        return String.format("Geometry [%tF] [%d; %d] / [%d; %d]",
            getStartDate(), 
            getAccidentPeriods(), getAccidentMonths(), 
            developmentPeriods, developmentMonths);
    }
}