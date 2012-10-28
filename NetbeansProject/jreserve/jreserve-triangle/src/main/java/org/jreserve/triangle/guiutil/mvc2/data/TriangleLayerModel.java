package org.jreserve.triangle.guiutil.mvc2.data;

import java.util.List;
import org.jreserve.data.Data;

/**
 *
 * @author Peter Decsi
 */
public interface TriangleLayerModel {

    public int size();
    
    public boolean isEmpty();
    
    public Layer get(int position);
    
    public void add(Layer layer);
    
    public void add(int position, Layer layer);
    
    public Layer set(int position, Layer layer);
    
    public void remove(Layer layer);
    
    public Layer remove(int position);
    
    public int getPosition(Layer layer);
    
    public boolean isCellEditable(LayerCriteria criteria);
    
    public List<Data> getValueAt(LayerCriteria criteria);
    
    public void setValueAt(Object value, LayerCriteria criteria);
}
