package org.jreserve.triangle;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.jreserve.rutil.RCode;
import org.jreserve.rutil.RUtil;
import org.jreserve.triangle.entities.TriangleComment;

/**
 * Triangular data represents a calculation with a triangular form.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangularData extends CalculationData {
   
    /**
     * Returns the number of accident periods in the triangle.
     */
    public int getAccidentCount();
    
    /**
     * Returns the number of development periods in the triangle.
     */
    public int getDevelopmentCount();
    
    /**
     * Returns the number of developmen periods within the given 
     * accident period.
     */
    public int getDevelopmentCount(int accident);
    
    /**
     * Returns the begin date for the given accident period.
     */
    public Date getAccidentName(int accident);
    
    /**
     * Returns the begin date for the given development period within
     * the given accident period.
     */
    public Date getDevelopmentName(int accident, int development);
    
    /**
     * Returns the value for the given cell of the triangle. If no
     * such cell exists then Double.NaN should be returned.
     */
    public double getValue(int accident, int development);

    /**
     * Returns the triangle as an array. The method must not return a null 
     */
    public double[][] toArray();
    
    /**
     * Returns the comments for the given accident and development
     * period.
     */
    public List<TriangleComment> getComments(int accident, int development);
    
    /**
     * Returns tha layer-id of the value, returned by {@link #getValue(int, int) getValue()}.
     * 
     * <p>If the given calculation layer computes a value for the given
     * cell it should return it's own layer id, otherwise the layer id
     * returned by the source should be returned.</p>
     * 
     * <p>This value may be used by TableWidgetRenderers to decide whether
     * they should render a cell or not.</p>
     */
    public String getLayerTypeId(int accident, int development);
    
    /**
     * Adds the source to the r-code, then adds itself to it.
     */
    public void createRTriangle(String triangleName, RCode rCode);
    
    @Override
    public TriangularData getSource();
    
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
        public List<TriangleComment> getComments(int accident, int development) {
            return Collections.EMPTY_LIST;
        }
        
        @Override
        public void createRTriangle(String triangleName, RCode rCode) {
            String values = RUtil.createArray(new double[0][0]);
            rCode.addSource(String.format("%s <- %s%n", triangleName, values));
        }
        
        @Override
        public String getLayerTypeId(int accident, int development) {
            return "EMPTY";
        }

        @Override
        public TriangularData getSource() {
            return null;
        }

        @Override
        public void detach() {
        }

        @Override
        public void recalculate() {
        }

        @Override
        public void addChangeListener(ChangeListener listener) {
        }

        @Override
        public void removeChangeListener(ChangeListener listener) {
        }
    };
}
