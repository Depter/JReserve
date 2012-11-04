package org.jreserve.triangle.widget.model;

import java.util.Date;
import java.util.List;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.TriangleCell;

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
            cells[r] = createRow(r, rowBounds.get(r), rowBounds.get(r+1));
        
        return cells;
    }
    
    private TriangleCell[] createRow(int rowIndex, Date aBegin, Date aEnd) {
        List<Date> columnBounds = ModelUtil.developmentBounds(geometry);
        if(columnBounds.size() < 2)
            return new TriangleCell[0];
        return createRow(rowIndex, columnBounds, aBegin, aEnd);
    }
    
    private TriangleCell[] createRow(int rowIndex, List<Date> columnBounds, Date aBegin, Date aEnd) {
        int cCount = columnBounds.size() - 1;
        TriangleCell[] row = new TriangleCell[cCount];
        TriangleCell previous = null;
        
        for(int c=0; c<cCount; c++) {
            Date dBegin = columnBounds.get(c);
            Date dEnd = columnBounds.get(c+1);
            
            if(dEnd.after(aBegin)) {
                row[c] = new TriangleCell(previous, rowIndex, c, aBegin, aEnd, dBegin, dEnd);
               previous = row[c];
            }
        }
        
        return row;
    }

}
