package org.jreserve.triangle.widget.model2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.util.GeometryUtil;
import org.jreserve.triangle.widget.TriangleCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleCellFactory {
    
    private final static TriangleCell[] ROW_SABLON = new TriangleCell[0];
    
    private GeometryUtil util = new GeometryUtil();
    private TriangleGeometry geometry;
    private int rowCount;
    private Date developmentEnd;
    
    TriangleCellFactory(TriangleGeometry geometry) {
        this.geometry = geometry;
    }
    
    public TriangleCell[][] createCells() {
        if(geometry == null)
            return new TriangleCell[0][];
        initParams();
        return buildCells();
    }
    
    private void initParams() {
        rowCount = geometry.getAccidentPeriods();
        int periods = geometry.getDevelopmentPeriods();
        developmentEnd = util.getDevelopmentEnd(geometry, 0, periods-1);
    }
    
    private TriangleCell[][] buildCells() {
        TriangleCell[][] cells = new TriangleCell[rowCount][];
        for(int r=0; r<rowCount; r++)
            cells[r] = buildRow(r);
        return cells;
    }
    
    private TriangleCell[] buildRow(int row) {
        Date aBegin = util.getAccidentBegin(geometry, row);
        Date aEnd = util.getAccidentEnd(geometry, row);
        List<TriangleCell> cells = buildRow(row, aBegin, aEnd);
        return cells.toArray(ROW_SABLON);
    }
    
    private List<TriangleCell> buildRow(int row, Date aBegin, Date aEnd) {
        List<TriangleCell> cells = new ArrayList<TriangleCell>();
        int dev = 0;
        Date dBegin = util.getDevelopmentBegin(geometry, row, dev);
        TriangleCell cell = null;
        
        while(dBegin.before(developmentEnd)) {
            Date dEnd = util.getDevelopmentEnd(geometry, row, dev++);
            cell = new TriangleCell(cell, row, dev, aBegin, aEnd, dBegin, dEnd);
            cells.add(cell);
            dBegin = dEnd;
        }
        
        return cells;
    }
}
