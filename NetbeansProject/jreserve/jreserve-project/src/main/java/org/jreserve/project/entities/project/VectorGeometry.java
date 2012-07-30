package org.jreserve.project.entities.project;

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
    
    private final static String ERR_END_BEFORE_START = 
         "End date '%tF' is before start date '%tF'!";
    private final static String ERR_ACC_MONTH = 
         "There must be at least 1 month in an accident period, but %d was found!";

    @Column(name="START_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Column(name="END_DATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date endDate;
    
    @Column(name="MONTH_IN_ACCIDENT", nullable=false)
    private int monthInAccident;
    
    protected VectorGeometry() {
    }
    
    public VectorGeometry(Date start, Date end, int monthInAccident) {
        initStartDate(start);
        initEndDate(end);
        initMonthInAccident(monthInAccident);
    }
    
    private void initStartDate(Date start) {
        if(start == null)
            throw new NullPointerException("Start date is null!");
        this.startDate = start;
    }
    
    private void initEndDate(Date end) {
        if(end == null)
            throw new NullPointerException("End date is null!");
        checkAfterStart(end);
        this.endDate = end;
    }
    
    private void checkAfterStart(Date end) {
        if(!end.before(startDate))
            return;
        String msg = String.format(ERR_END_BEFORE_START, end, startDate);
        throw new IllegalArgumentException(msg);
    }
    
    private void initMonthInAccident(int month) {
        if(month  < 1)
            throw new IllegalArgumentException(String.format(ERR_ACC_MONTH, month));
        this.monthInAccident = month;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        initEndDate(endDate);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        initStartDate(startDate);
        checkAfterStart(endDate);
    }

    public int getMonthInAccident() {
        return monthInAccident;
    }

    public void setMonthInAccident(int monthInAccident) {
        initMonthInAccident(monthInAccident);
    }
    
    @Override
    public String toString() {
        return String.format("Geometry [%tF - %tF]: %d",
            startDate, endDate, monthInAccident);
    }
}
