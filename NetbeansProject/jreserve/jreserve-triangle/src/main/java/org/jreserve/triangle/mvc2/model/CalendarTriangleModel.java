package org.jreserve.triangle.mvc2.model;

import java.util.Date;
import java.util.List;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.mvc2.data.TriangleCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CalendarTriangleModel extends AbstractTriangleModel {
    
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
    protected TriangleCell[][] createCells() {
        List<Date> rowBounds = ModelUtil.accidentBounds(geometry);
        int rowCount = rowBounds.size() - 1;
        
        TriangleCell[][] cells = new TriangleCell[rowCount][];
        for(int r=0; r<rowCount; r++)
            cells[r] = createRow(rowBounds.get(r), rowBounds.get(r+1));
        
        return cells;
    }
    
    private TriangleCell[] createRow(Date aBegin, Date aEnd) {
        List<Date> columnBounds = ModelUtil.developmentBounds(geometry);
        if(columnBounds.size() < 2)
            return new TriangleCell[0];
        return createRow(columnBounds, aBegin, aEnd);
    }
    
    private TriangleCell[] createRow(List<Date> columnBounds, Date aBegin, Date aEnd) {
        int cCount = columnBounds.size() - 1;
        TriangleCell[] row = new TriangleCell[cCount];
        
        for(int c=0; c<cCount; c++) {
            Date dBegin = columnBounds.get(c);
            Date dEnd = columnBounds.get(c+1);
            
            if(dEnd.after(aBegin))
                row[c] = new TriangleCell(aBegin, aEnd, dBegin, dEnd);
        }
        
        return row;
    }

}
