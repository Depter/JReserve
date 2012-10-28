package org.jreserve.triangle.mvc.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.jreserve.data.Data;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 */
public class Layer<O extends PersistentObject, V> {

    static void setOrder(Layer top, Layer bottom) {
        if(top != null)
            top.bottom = bottom;
        if(bottom != null)
            bottom.top = top;
    }
    
    private Layer top;
    private Layer bottom;
    private O owner;
    private List<Data<O, V>> datas = new ArrayList<Data<O, V>>();
    
    public Layer(O owner) {
        this.owner = owner;
    }
    
    Layer top() {
        return top;
    }
    
    Layer bottom() {
        return bottom;
    }
    
    public boolean isEditable() {
        if(isLayerEditable())
            return true;
        return bottom==null? false : bottom.isEditable();
    }
    
    protected boolean isLayerEditable() {
        return false;
    }
    
    public boolean isLayerVisible() {
        return true;
    }
    
    public List<Data<O, V>> getLayerValue(LayerCriteria criteria) {
        criteria.incrementCounter();
        if(!isLayerVisible())
            return getResultFromBottom(criteria);
        List<Data<O, V>> result = filterDatas(criteria);
        return result.isEmpty()? getResultFromBottom(criteria) : result;
    }
    
    private List<Data<O, V>> filterDatas(LayerCriteria criteria) {
        List<Data<O, V>> result = new ArrayList<Data<O, V>>();
        for(Data<O, V> data : datas)
            if(criteria.acceptsData(data))
                result.add(data);
        return result;
    }
    
    private List<Data<O, V>> getResultFromBottom(LayerCriteria criteria) {
        if(bottom == null)
            return null;
        return bottom.getLayerValue(criteria);
    }
    
    List<Data<O, V>> getRealDatas() {
        return datas;
    }
    
    public List<Data<O, V>> getDatas() {
        return new ArrayList<Data<O, V>>(datas);
    }
    
    public void setDatas(List<Data<O, V>> datas) {
        this.datas.clear();
        if(datas != null)
            this.datas.addAll(datas);
    }
    
    public void setValue(V value, LayerCriteria criteria) {
        if(isEditable())
            setMyValue(value, criteria);
        else if(bottom != null)
            bottom.setValue(value, criteria);
    }
    
    private void setMyValue(V value, LayerCriteria criteria) {
        removeDatas(criteria);
        createData(value, criteria);
    }
    
    private void removeDatas(LayerCriteria criteria) {
        for(Iterator<Data<O, V>> it = datas.iterator(); it.hasNext(); )
            if(criteria.acceptsData(it.next()))
                it.remove();
    }
    
    private void createData(V value, LayerCriteria criteria) {
        Date accident = criteria.getAccidentFrom();
        Date development = criteria.getDevelopmentFrom();
        Data<O, V> data = new Data<O, V>(owner, accident, development, value);
        datas.add(data);
    }
}
