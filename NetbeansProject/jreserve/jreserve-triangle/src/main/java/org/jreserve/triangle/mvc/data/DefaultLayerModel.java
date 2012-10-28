package org.jreserve.triangle.mvc.data;

import java.util.Iterator;
import java.util.List;
import org.jreserve.data.Data;

/**
 *
 * @author Peter Decsi
 */
public class DefaultLayerModel implements TriangleLayerModel {

    private Layer top;
    private int size;
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Layer get(int position) {
        checkPosition(position);
        Layer layer = top;
        for(int i=0; i<size; i++) {
            if(i == position)
                return layer;
            layer = layer.bottom();
        }
        return null;
    }
    
    private void checkPosition(int position) {
        if(position < 0)
            throw new IllegalArgumentException("Positiion less then 0! "+position);
        if(position >= size)
            throw new IllegalArgumentException("Position "+position + " > "+size);
    }
    
    @Override
    public void add(Layer layer) {
        add(size, layer);
    }

    @Override
    public void add(int position, Layer layer) {
        checkLayer(layer);
        if(position == 0) {
            Layer.setOrder(layer, top);
            this.top = layer;
        } else {
            Layer lTop = get(position-1);
            Layer bottom = lTop.bottom();
            Layer.setOrder(lTop, layer);
            Layer.setOrder(layer, bottom);
        }
        size++;
    }
    
    private void checkLayer(Layer layer) {
        if(layer == null)
            throw new NullPointerException("Layer is null!");
        if(layer.bottom()!=null || layer.top()!=null)
            throw new IllegalArgumentException("Layer is already added to a model!");
    }
    
    @Override
    public Layer set(int position, Layer layer) {
        checkLayer(layer);
        Layer old = get(position);
        Layer.setOrder(old.top(), layer);
        Layer.setOrder(layer, old.bottom());
        if(position == 0)
            this.top = layer;
        return old;
    }
    
    @Override
    public void remove(Layer layer) {
        Layer lTop = layer.top();
        Layer bottom = layer.bottom();
        Layer.setOrder(lTop, bottom);
        if(lTop == null)
            this.top = bottom;
        size--;
    }
    
    @Override
    public Layer remove(int position) {
        Layer layer = get(position);
        remove(layer);
        return layer;
    }
    
    @Override
    public int getPosition(Layer layer) {
        Layer current = top;
        for(int i=0; i<size; i++) {
            if(current.equals(layer))
                return i;
            current = current.bottom();
        }
        return -1;
    }

    @Override
    public boolean isCellEditable(LayerCriteria criteria) {
        if(top == null)
            return false;
        return top.isEditable();
    }

    @Override
    public List<Data> getValueAt(LayerCriteria criteria) {
        if(top == null)
            return null;
        return top.getLayerValue(criteria);
    }

    @Override
    public void setValueAt(Object value, LayerCriteria criteria) {
        if(top != null)
            top.setValue(value, criteria);
    }
}
