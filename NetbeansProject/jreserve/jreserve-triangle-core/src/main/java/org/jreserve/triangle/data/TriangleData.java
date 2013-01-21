package org.jreserve.triangle.data;

import java.util.*;
import org.jreserve.rutil.RCode;
import org.jreserve.rutil.RUtil;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.entities.TriangleComment;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TriangleData implements TriangularData {
    
    public final static String LAYER_TYPE_ID = "INPUT_DATA";
    
    private int accidentCount;
    private int developmentCount;
    private Date[] accidentDates;
    private Date[][] developmentDates;
    private double[][] values;
    private List<TriangleComment> comments = Collections.EMPTY_LIST;
    
    TriangleData(Date[] accidentDates, Date[][] developmentDates, double[][] values) {
        this.accidentCount = values.length;
        this.developmentCount = accidentCount==0? 0 : values[0].length;
        this.accidentDates = accidentDates;
        this.developmentDates = developmentDates;
        this.values = values;
    }
    
    @Override
    public int getAccidentCount() {
        return values.length;
    }

    @Override
    public int getDevelopmentCount() {
        return developmentCount;
    }

    @Override
    public int getDevelopmentCount(int accident) {
        if(accident >= accidentCount)
            return 0;
        return values[accident].length;
    }

    @Override
    public Date getAccidentName(int accident) {
        return accidentDates[accident];
    }

    @Override
    public Date getDevelopmentName(int accident, int development) {
        return developmentDates[accident][development];
    }

    @Override
    public double getValue(int accident, int development) {
        return values[accident][development];
    }

    @Override
    public double[][] toArray() {
        int accidents = values.length;
        double[][] copy = new double[accidents][];
        for(int a=0; a<accidents; a++) {
            int developments = values[a].length;
            copy[a] = new double[developments];
            System.arraycopy(values[a], 0, copy[a], 0, developments);
        }
        return copy;
    }

    @Override
    public void createRTriangle(String triangleName, RCode rCode) {
        String data = RUtil.createArray(values);
        rCode.addSource(String.format("%s <- %s%n", triangleName, data));
    }

    @Override
    public List<TriangleComment> getComments(int accident, int development) {
        List<TriangleComment> result = new ArrayList<TriangleComment>();
        for(TriangleComment comment : comments)
            if(comment.getTriangleCell().equals(accident, development))
                result.add(comment);
        return result;
    }
    
    void setComments(List<TriangleComment> comments) {
        this.comments = comments;
    }
    
    @Override
    public String getLayerTypeId(int accident, int development) {
        return LAYER_TYPE_ID;
    }
    
    @Override
    public String toString() {
        return String.format("TriangleInput [%d; %d]", accidentCount, developmentCount);
    }

    @Override
    public List<TriangularData> getLayers() {
        return Arrays.asList((TriangularData)this);
    }
    
    @Override
    public void close() {
        accidentCount = 0;
        developmentCount = 0;
        accidentDates = null;
        developmentDates = null;
        values = null;
        comments = null;
    }
}
