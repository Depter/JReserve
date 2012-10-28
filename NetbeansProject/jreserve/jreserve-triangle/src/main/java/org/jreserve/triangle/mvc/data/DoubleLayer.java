package org.jreserve.triangle.mvc.data;

import java.util.List;
import org.jreserve.data.Data;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 */
public class DoubleLayer<O extends PersistentObject> extends Layer<O, Double> {

    private boolean editable = false;
    
    public DoubleLayer(O owner, boolean editable) {
        super(owner);
        this.editable = editable;
    }
    
    public DoubleLayer(O owner, List<Data<O, Double>> datas) {
        super(owner);
        setDatas(datas);
    }
    
    @Override
    protected boolean isLayerEditable() {
        return editable;
    }
}
