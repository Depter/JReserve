package org.jreserve.triangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.jreserve.rutil.RCode;
import org.jreserve.rutil.RUtil;
import org.jreserve.triangle.comment.TriangleComment;

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

    public double[][] toArray();
    
    public List<TriangleComment> getComments(int accident, int development);
    
    public List<TriangularData> getLayers();
    
    public String getLayerTypeId(int accident, int development);
    
    public void createTriangle(String triangleName, RCode rCode);
    
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
        public void createTriangle(String triangleName, RCode rCode) {
            String values = RUtil.createArray(new double[0][0]);
            rCode.addSource(String.format("%s <- %s%n", triangleName, values));
        }
        
        @Override
        public List<TriangularData> getLayers() {
            List<TriangularData> layers = new ArrayList<TriangularData>();
            layers.add(this);
            return layers;
        }
        
        @Override
        public String getLayerTypeId(int accident, int development) {
            return "EMPTY";
        }
    };
}
