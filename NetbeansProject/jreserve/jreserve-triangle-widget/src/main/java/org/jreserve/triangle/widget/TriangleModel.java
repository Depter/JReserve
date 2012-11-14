package org.jreserve.triangle.widget;

import java.awt.Image;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.TriangleCell;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleModel {

    public Image getIcon();
    
    public String getToolTipName();
    
    public void setTriangleGeometry(TriangleGeometry geometry);
    
    public void setTriangleCells(TriangleCell[][] cells);
    
    public int getColumnCount();
    
    public String getColumnName(int column);
    
    public int getRowCount();
    
    public String getRowName(int row);
    
    public TriangleCell getCellAt(int row, int column);
}
