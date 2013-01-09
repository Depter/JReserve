package org.jreserve.triangle;

import java.util.Date;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangularData {
    
    public int getAccidentCount();
    
    public int getDevelopmentCount();
    
    public int getDevelopmentCount(int accident);
    
    public Date getAccidentName(int accident);
    
    public Date getDevelopmentName(int accident, int development);
    
    public double getValue(int accident, int development);
    
    public void addChangeListener(ChangeListener listener);
    
    public void removeChangeListener(ChangeListener listener);

    public double[][] toArray();
    
    public String getLayerTypeId(int accident, int development);
    
    public static interface Provider {
        public TriangularData getTriangularData();
    }
    
    public static TriangularData EMPTY = new TriangularData() {
        
        @Override
        public int getAccidentCount() {
            return 0;
        }

        @Override
        public int getDevelopmentCount() {
            return 0;
        }

        @Override
        public int getDevelopmentCount(int accident) {
            return 0;
        }

        @Override
        public Date getAccidentName(int accident) {
            return null;
        }

        @Override
        public Date getDevelopmentName(int accident, int development) {
            return null;
        }
        
        @Override
        public double getValue(int accident, int development) {
            return 0d;
        }
        
        @Override
        public double[][] toArray() {
            return new double[0][];
        }

        @Override
        public void addChangeListener(ChangeListener listener) {
        }

        @Override
        public void removeChangeListener(ChangeListener listener) {
        }
        
        @Override
        public String getLayerTypeId(int accident, int development) {
            return "EMPTY";
        }
    };
}
