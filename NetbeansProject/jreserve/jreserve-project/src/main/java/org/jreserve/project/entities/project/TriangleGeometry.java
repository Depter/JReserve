package org.jreserve.project.entities.project;

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
    
    private final static String ERR_DEV_MONTH = 
         "There must be at least 1 month in a development period, but %d was found!";

    @Column(name="MONTH_IN_DEVELOPMENT", nullable=false)
    private int monthInDevelopment;
    
    protected TriangleGeometry() {
    }
    
    public TriangleGeometry(Date start, Date end, int monthInAccident, int monthInDevelopment) {
        super(start, end, monthInAccident);
        initMonthInDevelopment(monthInDevelopment);
    }
    
    private void initMonthInDevelopment(int month) {
        if(month  < 1)
            throw new IllegalArgumentException(String.format(ERR_DEV_MONTH, month));
        this.monthInDevelopment = month;
    }

    public int getMonthInDevelopment() {
        return monthInDevelopment;
    }

    public void setMonthInDevelopment(int month) {
        initMonthInDevelopment(month);
    }
    
    @Override
    public String toString() {
        return String.format("Geometry [%tF - %tF]: %d/%d",
            getStartDate(), getEndDate(), 
            getMonthInAccident(), monthInDevelopment);
    }
}
