package org.jreserve.triangle.mvc.layer;

import java.util.List;
import org.jreserve.data.Data;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 */
public class DoubleLayer<O extends PersistentObject> extends  Layer<O, Double> {

    public DoubleLayer() {
        init();
    }
    
    public DoubleLayer(boolean editable, boolean visible) {
        super.setEditable(editable);
        super.setVisible(visible);
    }

    public DoubleLayer(List<Data<O, Double>> datas) {
        super(datas);
        init();
    }
    
    private void init() {
        super.setEditable(false);
        super.setVisible(true);
    }
    
    @Override
    public Object getValue(LayerCriteria criteria) {
        double sum = 0d;
        for(Data<O, Double> data : datas) {
            if(criteria.acceptsData(data)) {
                sum = add(sum, data);
                if(Double.isNaN(sum))
                    break;
            }
        }
        return sum;
    }
    
    private double add(double sum, Data<O, Double> data) {
        Double value = data.getValue();
        if(value == null)
            return sum;
        double v = value;
        if(Double.isNaN(v))
            return Double.NaN;
        return sum + v;
    }

    @Override
    public void setValue(LayerCriteria criteria, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
