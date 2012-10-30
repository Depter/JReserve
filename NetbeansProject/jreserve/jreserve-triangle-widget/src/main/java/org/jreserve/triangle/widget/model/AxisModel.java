package org.jreserve.triangle.widget.model;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface AxisModel {
    
    public final static AxisModel EMPTY = new AxisModel() {
        @Override public int size() {return 0;}
        @Override public String getTitle(int index) {return null;}
    };

    public int size();
    
    public String getTitle(int index);
}
