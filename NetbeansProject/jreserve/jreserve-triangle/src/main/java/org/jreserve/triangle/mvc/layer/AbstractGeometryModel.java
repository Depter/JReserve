package org.jreserve.triangle.mvc.layer;

import java.util.Calendar;
import java.util.Date;
import org.jreserve.triangle.entities.TriangleGeometry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
abstract class AbstractGeometryModel implements GeometryModel {

    protected TriangleGeometry geometry;
    private Calendar calendar = Calendar.getInstance();
    private boolean cummulated = false;
    
    @Override
    public void setTriangleGeometry(TriangleGeometry geometry) {
        this.geometry = geometry;
    }
    
    @Override
    public TriangleGeometry getTriangleGeometry() {
        return geometry;
    }

    @Override
    public int getRowCount() {
        if(geometry == null)
            return 0;
        return geometry.getAccidentPeriods();
    }

    @Override
    public int getColumnCount() {
        if(geometry == null)
            return 0;
        return geometry.getDevelopmentPeriods() + 1;
    }

    @Override
    public Object getRowName(int row) {
        return getAccidentBegin(row);
    }

    protected Date getAccidentBegin(int row) {
        calendar.setTime(geometry.getAccidentStart());
        int amount = geometry.getMonthInAccident() * row;
        calendar.add(Calendar.MONTH, amount);
        return calendar.getTime();
    }
    
    protected Date getAccidentEnd() {
        calendar.setTime(geometry.getAccidentStart());
        int periods = geometry.getAccidentPeriods();
        int amount = geometry.getMonthInAccident() * (periods + 1);
        calendar.add(Calendar.MONTH, amount);
        return calendar.getTime();
    }
    
    protected Date getDevelopmentBegin(int column) {
        Date start = geometry.getDevelopmentStart();
        return getDevelopmentBegin(start, column);
    }
    
    protected Date getDevelopmentBegin(Date start, int column) {
        calendar.setTime(start);
        int amount = geometry.getMonthInDevelopment() * (column - 1);
        calendar.add(Calendar.MONTH, amount);
        return calendar.getTime();
    }
    
    protected Date getDevelopmentEnd() {
        calendar.setTime(geometry.getDevelopmentStart());
        int periods = geometry.getDevelopmentPeriods();
        int amount = geometry.getMonthInDevelopment() * (periods + 1);
        calendar.add(Calendar.MONTH, amount);
        return calendar.getTime();
    }
    
    @Override
    public boolean isCummulated() {
        return cummulated;
    }
    
    @Override
    public void setCummulated(boolean cummulated) {
        this.cummulated = cummulated;
    }
    
    @Override
    public LayerCriteria createCriteria(int row, int column) {
        Date accidentFrom = getAccidentBegin(row);
        Date accidentTo = getAccidentBegin(row+1);
        Date developmentFrom = getDevelopmentBegin(accidentFrom, cummulated? 1 : column);
        Date developmentEnd = getDevelopmentBegin(accidentFrom, column+1);
        return new LayerCriteria()
                .setAccidentFrom(accidentFrom).setAccidentEnd(accidentTo)
                .setDevelopmentFrom(developmentFrom).setDevelopmentEnd(developmentEnd);
    }
}
