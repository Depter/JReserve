package org.jreserve.triangle.widget.model;

import java.util.Date;
import java.util.List;
import javax.swing.table.TableModel;
import org.jreserve.triangle.entities.Comment;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidgetListener;
import org.jreserve.triangle.widget.WidgetData;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleModel extends TableModel {
    
    public AxisModel getRowModel();
    
    public AxisModel getColumnModel();
    
    public void setTriangleGeometry(TriangleGeometry geometry);
    
    public TriangleGeometry getTriangleGeometry();
    
    public void setCummulated(boolean isCummulated);
    
    public boolean isCummulated();
    
    public TriangleCell[][] getCells();
    
    public void addValues(List<WidgetData<Double>> datas);
    
    public void addValues(int layer, List<WidgetData<Double>> datas);
    
    public void setValues(int layer, List<WidgetData<Double>> datas);
    
    public List<List<WidgetData<Double>>> getValues();
    
    public List<WidgetData<Double>> getValues(int layer);
    
    public void removeValues(int layer);
    
    public TriangleCell getCellAt(Date accident, Date development);
    
    public void setComments(List<WidgetData<Comment>> comments);
    
    public void addComments(List<WidgetData<Comment>> comments);
    
    public void addComment(WidgetData<Comment> comment);
    
    public List<WidgetData<Comment>> getComments();
    
    public void removeComment(WidgetData<Comment> comment);
    
    public void setEditableLayer(int layer);
    
    public int getEditableLayer();
    
    public void addTriangleWidgetListener(TriangleWidgetListener listener);
    
    public void removeTriangleWidgetListener(TriangleWidgetListener listener);
    
    public List<TriangleWidgetListener> getTriangleWidgetListeners();
    
    public void copyStateFrom(TriangleModel model);
    
    public static enum ModelType {
        CALENDAR {
            @Override
            public TriangleModel createModel() {
                return new CalendarTriangleModel();
            }
        },
        DEVELOPMENT {
            @Override
            public TriangleModel createModel() {
                return new DevelopmentTriangleModel();
            }
        };
        
        public abstract TriangleModel createModel();
    }
    
}
