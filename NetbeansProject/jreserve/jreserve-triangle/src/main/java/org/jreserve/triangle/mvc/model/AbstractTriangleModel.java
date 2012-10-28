package org.jreserve.triangle.mvc.model;

import java.util.Calendar;
import java.util.Date;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.mvc.data.LayerCriteria;

/**
 *
 * @author Peter Decsi
 */
public abstract class AbstractTriangleModel implements TriangleModel {

    protected TriangleGeometry geometry;
    protected Date accidentBegin;
    protected int accidentPeriods = -1;
    protected int accidentMonths = -1;
    protected Date developmentBegin;
    protected int developmentPeriods = -1;
    protected int developmentMonths = -1;
    
    protected AxisModel rowModel = AxisModel.EMPTY;
    protected AxisModel columnModel = AxisModel.EMPTY;
    
    protected boolean cummulated;
    private Calendar calendar = Calendar.getInstance();
    
    @Override
    public void setTriangleGeometry(TriangleGeometry geometry) {
        this.geometry = geometry;
        initGeometry(geometry);
        this.rowModel = createRowModel(geometry);
        this.columnModel = createColumnModel(geometry);
    }
    
    protected void initGeometry(TriangleGeometry geometry) {
        this.accidentBegin = geometry==null? null : geometry.getAccidentStart();
        this.accidentPeriods = geometry==null? -1 : geometry.getAccidentPeriods();
        this.accidentMonths = geometry==null? -1 : geometry.getMonthInAccident();
        this.developmentBegin = geometry==null? null : geometry.getDevelopmentStart();
        this.developmentPeriods = geometry==null? -1 : geometry.getDevelopmentPeriods();
        this.developmentMonths = geometry==null? -1 : geometry.getMonthInDevelopment();
    }
    
    protected AxisModel createRowModel(TriangleGeometry geometry) {
        if(geometry == null)
            return AxisModel.EMPTY;
        int size = geometry.getAccidentPeriods();
        return new AxisModel.NumberAxisModel(size);
    }
    
    protected AxisModel createColumnModel(TriangleGeometry geometry) {
        if(geometry == null)
            return AxisModel.EMPTY;
        int size = geometry.getDevelopmentPeriods();
        return new AxisModel.NumberAxisModel(size);
    }
    
    @Override
    public TriangleGeometry getTriangleGeometry() {
        return geometry;
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
    public AxisModel getColumnModel() {
        return columnModel;
    }
    
    @Override
    public AxisModel getRowModel() {
        return rowModel;
    }
    
    @Override
    public LayerCriteria createCellCriteria(int row, int column) {
        LayerCriteria criteria = new LayerCriteria();
        setRowCriteria(criteria, row, column);
        setColumnCellCriteria(criteria, row, column);
        return criteria;
    }

    protected void setRowCellCriteria(LayerCriteria criteria, int row, int column) {
        setRowCriteria(criteria, row, column);
    }

    protected void setColumnCellCriteria(LayerCriteria criteria, int row, int column) {
        Date from = addMonth(developmentBegin, developmentMonths * column);
        Date end = addMonth(from, developmentMonths);
        criteria.setDevelopmentFrom(from).setDevelopmentEnd(end);
    }

    @Override
    public LayerCriteria createCriteria(int row, int column) {
        LayerCriteria criteria = new LayerCriteria();
        setRowCriteria(criteria, row, column);
        setColumnCriteria(criteria, row, column);
        return criteria;
    }

    protected void setRowCriteria(LayerCriteria criteria, int row, int column) {
        Date from = addMonth(accidentBegin, accidentMonths * row); 
        Date end = addMonth(from, accidentMonths);
        criteria.setAccidentFrom(from).setAccidentEnd(end);
    }
    
    protected Date addMonth(Date start, int months) {
        if(months == 0)
            return start;
        calendar.setTime(start);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    protected void setColumnCriteria(LayerCriteria criteria, int row, int column) {
        Date from = cummulated? developmentBegin : addMonth(developmentBegin, developmentMonths * column);
        int months = cummulated? developmentMonths * (column + 1) : developmentMonths;
        Date end = addMonth(from, months);
        criteria.setDevelopmentFrom(from).setDevelopmentEnd(end);
    }
}
