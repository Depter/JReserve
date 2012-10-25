package org.jreserve.triangle.mvc.layer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LayerData {

    private Layer firstLayer;
    private int size;
    
    public void clear() {
        size = 0;
        
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int getSize() {
        return size;
    }
    
    public Layer getLayer(int position) {
        checkPosition(position);
        Layer result = firstLayer;
        for(int i=0; i<position; i++)
            result = result.next();
        return result;
    }
    
    private void checkPosition(int position) {
        checkNotNegative(position);
        if(position >= size) {
            String msg = String.format("Position %d >= %d (size)!", position, size);
            throw new IndexOutOfBoundsException(msg);
        }
    }
    
    private void checkNotNegative(int position) {
        if(position < 0) {
            String msg = String.format("Position %d < 0!", position);
            throw new IndexOutOfBoundsException(msg);
        }
    }
    
    public void addLayer(Layer layer) {
        addLayer(size, layer);
    }
    
    public void addLayer(int position, Layer layer) {
        checkLayer(layer);
        setNext(position==0? null : getLayer(position-1), layer);
        setNext(layer, position==size? null: getLayer(position));
        size++;
    }
    
    private void checkLayer(Layer layer) {
        if(layer == null)
            throw new NullPointerException("Layer is null!");
        if(layer.previous() != null)
            throw new IllegalArgumentException("Layer already has a previous layer!");
        if(layer.next() != null)
            throw new IllegalArgumentException("Layer already has a next layer!");
    }
    
    private void setNext(Layer previous, Layer next) {
        if(next != null)
            next.setPrevious(previous);
        if(previous != null)
            previous.setNext(next);
    }
    
    public void setLayer(int position, Layer layer) {
        checkLayer(layer);
        swapLayers(layer, getLayer(position));
        size++;
    }
    
    private void swapLayers(Layer newLayer, Layer oldLayer) {
        setNext(newLayer, oldLayer.next());
        setNext(oldLayer.previous(), newLayer);
    }
    
    public void removeLayer(int position) {
        Layer removed = getLayer(position);
        setNext(removed.previous(), removed.next());
        size--;
    }
    
    public void removeLayer(Layer layer) {
        int position = getPosition(layer);
        if(position >= 0)
            removeLayer(position);
    }
    
    public int getPosition(Layer layer) {
        Layer l = firstLayer;
        for(int i=0; i<size; i++) {
            if(l.equals(layer))
                return i;
            l = l.next();
        }
        return -1;
    }
}
