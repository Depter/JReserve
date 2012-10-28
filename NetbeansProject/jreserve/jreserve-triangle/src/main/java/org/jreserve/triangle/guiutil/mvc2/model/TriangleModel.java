package org.jreserve.triangle.guiutil.mvc2.model;

import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.guiutil.mvc2.data.LayerCriteria;

/**
 *
 * @author Peter Decsi
 */
public interface TriangleModel {
    
    public void setTriangleGeometry(TriangleGeometry geometry);
    
    public TriangleGeometry getTriangleGeometry();
    
    public boolean isCummulated();
    
    public void setCummulated(boolean cummulated);
    
    public AxisModel getColumnModel();
    
    public AxisModel getRowModel();
    
    public LayerCriteria createCriteria(int row, int column);
    
    public boolean hasValueAt(int row, int column);
    
    public static enum ModelType {
        CALENDAR {
            @Override
            TriangleModel createModel() {
                return new CalendarTriangleModel();
            }
        },
        DEVELOPMENT {
            @Override
            TriangleModel createModel() {
                return new DevelopmentTriangleModel();
            }
        };
        
        abstract TriangleModel createModel();
    }
}
