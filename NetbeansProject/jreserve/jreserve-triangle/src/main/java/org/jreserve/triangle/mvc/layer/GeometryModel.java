package org.jreserve.triangle.mvc.layer;

import org.jreserve.triangle.entities.TriangleGeometry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface GeometryModel {
    
    public void setTriangleGeometry(TriangleGeometry geometry);
    
    public TriangleGeometry getTriangleGeometry();
    
    public int getRowCount();
    
    public int getColumnCount();
    
    public Object getRowName(int row);
    
    public Class getRowTitleClass();
    
    public Object getColumnTitle(int column);
    
    public Class getColumnTitleClass();
    
    public boolean hasCellAt(int row, int column);
    
    public boolean isCummulated();
    
    public void setCummulated(boolean cummulated);
    
    public LayerCriteria createCriteria(int row, int column);
    
    public static enum ModelType {
        CALENDAR {
            @Override
            GeometryModel createModel() {
                return new CalendarGeometryModel();
            }
        },
        DEVELOPMENT {
            @Override
            GeometryModel createModel() {
                return new DevelopmentGeometryModel();
            }
        };
        
        abstract GeometryModel createModel();
    }
}
