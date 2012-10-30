package org.jreserve.triangle.widget.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.data.TriangleCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DevelopmentTriangleModel extends AbstractTriangleModel {
    
    @Override
    protected AxisModel createRowModel(TriangleGeometry geometry) {
        if(geometry == null)
            return AxisModel.EMPTY;
        return DateAxisModel.createAccident(geometry);
    }

    @Override
    protected TriangleCell[][] createCells() {
        List<Date> rowBounds = ModelUtil.accidentBounds(geometry);
        List<Date> devBounds = ModelUtil.developmentBounds(geometry);
        int rowCount = rowBounds.size() - 1;
        
        TriangleCell[][] cells = new TriangleCell[rowCount][];
        for(int r=0; r<rowCount; r++)
            cells[r] = createRow(devBounds, rowBounds.get(r), rowBounds.get(r+1));
        
        return cells;
    }
    
    private TriangleCell[] createRow(List<Date> devBounds, Date aBegin, Date aEnd) {
        List<Date> columnBounds = createColumnBoundsForRow(devBounds, aBegin);
        if(columnBounds.size() < 2)
            return new TriangleCell[0];
        return createBoundedRow(columnBounds, aBegin, aEnd);
    }
    
     private List<Date> createColumnBoundsForRow(List<Date> bounds, Date rowBegin) {
        List<Date> result = new ArrayList<Date>(bounds);
         for(Iterator<Date> it=result.iterator(); it.hasNext(); )
            if(rowBegin.after(it.next()))
                it.remove();
        return result;
    }
    
    private TriangleCell[] createBoundedRow(List<Date> columnBounds, Date aBegin, Date aEnd) {
        int cCount = columnBounds.size() - 1;
        TriangleCell[] row = new TriangleCell[cCount];
        
        for(int c=0; c<cCount; c++) {
            Date dBegin = columnBounds.get(c);
            Date dEnd = columnBounds.get(c+1);
            row[c] = new TriangleCell(aBegin, aEnd, dBegin, dEnd);
        }
        
        return row;
    }
}
