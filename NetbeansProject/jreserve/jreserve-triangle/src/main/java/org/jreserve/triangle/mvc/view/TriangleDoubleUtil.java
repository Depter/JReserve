package org.jreserve.triangle.mvc.view;

import java.util.List;
import org.jreserve.data.Data;

/**
 *
 * @author Peter Decsi
 */
public class TriangleDoubleUtil {

    public static double getTableValue(Object value) {
        if(value instanceof List) {
            return sumValues((List<Data>) value);
        } else {
            return Double.NaN;
        }
    }
    
    private static double sumValues(List<Data> datas) {
        double sum = 0d;
        for(Data data : datas) {
            double v = getValue(data);
            if(Double.isNaN(v))
                return Double.NaN;
            sum += v;
        }
        return sum;
    }
    
    private static double getValue(Data data) {
        Object value = data.getValue();
        if(value instanceof Double)
            return (Double)value;
        return Double.NaN;
    }
    
}
