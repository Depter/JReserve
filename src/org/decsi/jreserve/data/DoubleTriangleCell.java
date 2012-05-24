package org.decsi.jreserve.data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DoubleTriangleCell extends TriangleCell {

    private double value;
    private double originalValue;

    public double getOriginalValue() {
        return originalValue;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    
}
