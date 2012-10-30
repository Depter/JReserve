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
    
    public static class NumberAxisModel implements AxisModel {
        
        private int size;
        
        public NumberAxisModel(int size) {
            if(size < 0)
                size = 0;
            this.size = size;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public String getTitle(int index) {
            return Integer.toString(index + 1);
        }
    }
}
