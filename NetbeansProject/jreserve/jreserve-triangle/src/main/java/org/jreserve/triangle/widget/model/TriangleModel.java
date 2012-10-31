package org.jreserve.triangle.widget.model;

import java.util.List;
import javax.swing.table.TableModel;
import org.jreserve.data.Data;
import org.jreserve.data.DataComment;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.entities.TriangleGeometry;
import org.jreserve.triangle.widget.TriangleWidget;
import org.jreserve.triangle.widget.TriangleWidget.TriangleWidgetListener;
import org.jreserve.triangle.widget.data.TriangleCell;

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
    
    public void addValues(List<Data<PersistentObject, Double>> datas);
    
    public void addValues(int layer, List<Data<PersistentObject, Double>> datas);
    
    public void setValues(int layer, List<Data<PersistentObject, Double>> datas);
    
    public List<List<Data<PersistentObject, Double>>> getValues();
    
    public List<Data<PersistentObject, Double>> getValues(int layer);
    
    public void removeValues(int layer);
    
    public void addComments(List<Data<PersistentObject, DataComment>> comments);
    
    public void addComment(Data<PersistentObject, DataComment> comment);
    
    public List<Data<PersistentObject, DataComment>> getComments();
    
    public void removeComment(Data<PersistentObject, DataComment> comment);
    
    public void setEditableLayer(int layer);
    
    public int getEditableLayer();
    
    public void addTriangleWidgetListener(TriangleWidgetListener listener);
    
    public void removeTriangleWidgetListener(TriangleWidgetListener listener);
    
    public List<TriangleWidgetListener> getTriangleWidgetListeners();
    
    public void copyStateFrom(TriangleModel model);
    
    public <T extends PersistentObject> List<Data<T, Double>> getLayer(T owner, int layerIndex);
    
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
