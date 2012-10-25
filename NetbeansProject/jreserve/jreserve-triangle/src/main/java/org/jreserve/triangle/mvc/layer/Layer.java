package org.jreserve.triangle.mvc.layer;

import java.util.Date;
import org.jreserve.triangle.mvc.model.TriangleTable;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Layer<V> {

    private Layer previousLayer;
    private Layer nextLayer;
    private boolean editable;
    private boolean visible;
    
    private TriangleTable<V> table;
    
    public Layer(TriangleTable<V> table) {
        this.table = table;
    }
    
    Layer next() {
        return nextLayer;
    }
    
    void setNext(Layer layer) {
        this.nextLayer = layer;
    }
    
    Layer previous() {
        return previousLayer;
    }
    
    void setPrevious(Layer layer) {
        this.previousLayer = layer;
    }
    
    public boolean hasValueAt(Date accident, Date development) {
        return table.getCell(accident, development) != null;
    }
    
    public Object getValue(LayerCriteria criteria) {
        return null;
    }
    
    public void setValue(LayerCriteria criteria, Object value) {
    }
    
    public boolean isEditable() {
        return editable;
    }
    
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
