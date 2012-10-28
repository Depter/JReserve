package org.jreserve.triangle.guiutil.mvc2.model;

import java.util.Date;
import org.jreserve.triangle.entities.TriangleGeometry;

/**
 *
 * @author Peter Decsi
 */
class CalendarTriangleModel extends AbstractTriangleModel {
    
    @Override
    protected AxisModel createRowModel(TriangleGeometry geometry) {
        if(geometry == null)
            return AxisModel.EMPTY;
        return DateAxisModel.createAccident(geometry);
    }
    
    @Override
    protected AxisModel createColumnModel(TriangleGeometry geometry) {
        if(geometry == null)
            return AxisModel.EMPTY;
        return DateAxisModel.createDevelopment(geometry);
    }

    @Override
    public boolean hasValueAt(int row, int column) {
        Date devBegin = addMonth(developmentBegin, developmentMonths * column);
        Date rowBegin = addMonth(accidentBegin, accidentMonths * row);
        return !rowBegin.after(devBegin);
    }
}
