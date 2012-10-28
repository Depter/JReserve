package org.jreserve.triangle.guiutil.mvc2.model;

import java.util.Date;
import org.jreserve.triangle.entities.TriangleGeometry;

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
}
