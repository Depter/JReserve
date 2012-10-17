package org.jreserve.triangle.mvc.model;

import java.util.Date;
import java.util.List;
import org.jreserve.data.Data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ValueCell extends AbstractCell {
    
    private final static double DEFAULT_VALUE = 0d;
    
    private double value = DEFAULT_VALUE;
    
    ValueCell(Date developmentBegin, Date developmentEnd) {
        super(developmentBegin, developmentEnd);
    }
    
    public double getValue() {
        return value;
    }
    
    void setValues(List<Data> datas) {
        value = DEFAULT_VALUE;
        for(Data data : datas)
            if(myData(data))
                addData(data);
    }
    
    private void addData(Data data) {
        double dataValue = data.getValue();
        if(!Double.isNaN(dataValue))
            value += dataValue;
    }
    
    public double getCummulatedValue() {
        double previous = getPreviousValue();
        if(Double.isNaN(previous))
            return value;
        return previous + value;
    }
    
    private double getPreviousValue() {
        if(row == null)
            return Double.NaN;
        ValueCell previous = (ValueCell) row.getPreviousCell(this);
        return previous==null? Double.NaN : previous.getCummulatedValue();
    }
}
