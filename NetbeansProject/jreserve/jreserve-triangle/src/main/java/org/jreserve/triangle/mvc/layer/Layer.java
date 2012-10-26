package org.jreserve.triangle.mvc.layer;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.data.Data;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class Layer<O extends PersistentObject, V> {

    private Layer previousLayer;
    private Layer nextLayer;
    private boolean editable;
    private boolean visible;
    
    protected List<Data<O, V>> datas = new ArrayList<Data<O, V>>();
    
    public Layer() {
        this.datas.addAll(datas);
    }
    
    public Layer(List<Data<O, V>> datas) {
        this.datas.addAll(datas);
    }
    
    public List<Data<O, V>> getData() {
        return new ArrayList<Data<O, V>>(datas);
    }
    
    public void setData(List<Data<O,V>> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
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
    
    public Object getValue(LayerCriteria criteria) {
        List<Data<O, V>> filtered = filterData(criteria);
        if(filtered.isEmpty())
            return null;
        return getValue(filtered);
    }
    
    private List<Data<O, V>> filterData(LayerCriteria criteria) {
        List<Data<O, V>> result = new ArrayList<Data<O, V>>();
        for(Data<O, V> data : datas)
            if(criteria.acceptsData(data))
                result.add(data);
        return result;
    }
    
    protected abstract Object getValue(List<Data<O, V>> filtered);
    
    public abstract void setValue(LayerCriteria criteria, Object value);
    
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
