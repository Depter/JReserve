package org.jreserve.triangle.mvc.layer;

import org.jreserve.triangle.entities.TriangleGeometry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
interface GeometryModel {
    
    
    public void setTriangleGeometry(TriangleGeometry geometry);
    
    public int getRowCount();
    
    public int getColumnCount();
    
    public Object getRowName(int row);
    
    public Object getColumnTitle(int column);
    
    public Class getColumnTitleClass();
    
    public boolean hasCellAt(int row, int column);
    
    public LayerCriteria createCriteria(int row, int column);
}
