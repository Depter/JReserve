package org.jreserve.triangle.widget.model2;

import java.awt.Image;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangleModel implements TriangleModel {

    protected TriangleGeometry geometry;
    protected TriangleCell[][] cells;
    protected int rowCount = 0;
    protected int columnCount = 0;
    
    private Image img;
    private String toolTip;
    
    protected AbstractTriangleModel(Image img, String toolTip) {
        this.img = img;
        this.toolTip = toolTip;
    }

    @Override
    public Image getIcon() {
        return img;
    }

    @Override
    public String getToolTipName() {
        return toolTip;
    }

    @Override
    public void setTriangleGeometry(TriangleGeometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public void setTriangleCells(TriangleCell[][] cells) {
        this.cells = cells;
        calculateRowCount();
        calculateColumnCount();
    }
    
    protected void calculateRowCount() {
        rowCount = cells==null? 0 : cells.length;
    }
    
    protected void calculateColumnCount() {
        columnCount = 0;
        for(int r=0; r<rowCount; r++) {
            if(cells[r] != null && columnCount < cells[r].length)
                columnCount = cells[r].length;
        }
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public String getColumnName(int column) {
        return ""+(column+1);
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public String getRowName(int row) {
        return ""+(row+1);
    }

    @Override
    public TriangleCell getCellAt(int row, int column) {
        if(cells == null)
            return null;
        if(row >= rowCount)
            return null;
        return getCellAt(cells[row], column);
    }
    
    protected TriangleCell getCellAt(TriangleCell[] row, int column) {
        if(row == null || row.length <= column)
            return null;
        return row[column];
    }
}
