package org.jreserve.triangle.guiutil.mvc2.model;

import java.util.Calendar;
import java.util.Date;
import org.jreserve.localesettings.util.DateRenderer;
import org.jreserve.triangle.entities.TriangleGeometry;

/**
 *
 * @author Peter Decsi
 */
class DateAxisModel implements AxisModel {

    static AxisModel createAccident(TriangleGeometry geometry) {
        Date start = geometry.getAccidentStart();
        int periods = geometry.getAccidentPeriods();
        int months = geometry.getMonthInAccident();
        return new DateAxisModel(start, periods, months);
    } 
    
    static AxisModel createDevelopment(TriangleGeometry geometry) {
        Date start = geometry.getDevelopmentStart();
        int periods = geometry.getDevelopmentPeriods();
        int months = geometry.getMonthInDevelopment();
        return new DateAxisModel(start, periods, months);
    }
    
    private Calendar calendar = Calendar.getInstance();
    private DateRenderer dateRenderer = new DateRenderer();
    private Date start;
    private int periods;
    private int months;
    
    public DateAxisModel(Date start, int periods, int months) {
        this.start = start;
        this.periods = periods;
        this.months = months;
    }
    
    @Override
    public int size() {
        return periods;
    }

    @Override
    public String getTitle(int index) {
        Date date = getDate(index);
        return dateRenderer.toString(date);
    }
    
    private Date getDate(int index) {
        if(index == 0)
            return start;
        calendar.setTime(start);
        calendar.add(Calendar.MONTH, months * index);
        return calendar.getTime();
    }
}
