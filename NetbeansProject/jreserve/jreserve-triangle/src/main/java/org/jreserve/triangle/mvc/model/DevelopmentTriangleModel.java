package org.jreserve.triangle.mvc.model;

import java.util.Date;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.mvc.data.LayerCriteria;

/**
 *
 * @author Peter Decsi
 */
class DevelopmentTriangleModel extends AbstractTriangleModel {
    
    @Override
    protected AxisModel createRowModel(TriangleGeometry geometry) {
        if(geometry == null)
            return AxisModel.EMPTY;
        return DateAxisModel.createAccident(geometry);
    }

    @Override
    public boolean hasValueAt(int row, int column) {
        Date developmentEnd = addMonth(developmentBegin, developmentMonths * developmentPeriods);
        Date rowBegin = getDevelopmentBeginAtRow(row);
        Date cellBegin = addMonth(rowBegin, developmentMonths * column);
        return cellBegin.before(developmentEnd);
    }
    
    private Date getDevelopmentBeginAtRow(int row) {
        Date aDate = addMonth(accidentBegin, accidentMonths * row);
        if(aDate.after(developmentBegin))
            return aDate;
        return developmentBegin;
    }

    @Override
    protected void setColumnCellCriteria(LayerCriteria criteria, int row, int column) {
        setNotCummulatedColumnCriteria(criteria, row, column);
    }
    
    @Override
    protected void setColumnCriteria(LayerCriteria criteria, int row, int column) {
        if(cummulated)
            setCummulatedColumnCriteria(criteria, row, column);
        else
            setNotCummulatedColumnCriteria(criteria, row, column);
    }
    
    private void setCummulatedColumnCriteria(LayerCriteria criteria, int row, int column) {
        Date from = getDevelopmentBeginAtRow(row);
        int months = developmentMonths * (column + 1);
        Date end = addMonth(from, months);
        criteria.setDevelopmentFrom(from).setDevelopmentEnd(end);
    }
    
    private void setNotCummulatedColumnCriteria(LayerCriteria criteria, int row, int column) {
        Date from = getDevelopmentBeginAtRow(row);
        from = addMonth(from, developmentMonths * column);
        Date end = addMonth(from, developmentMonths);
        criteria.setDevelopmentFrom(from).setDevelopmentEnd(end);
    }
}
