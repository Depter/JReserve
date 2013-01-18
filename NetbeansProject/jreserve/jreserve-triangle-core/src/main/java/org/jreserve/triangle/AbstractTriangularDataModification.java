package org.jreserve.triangle;

import java.util.Date;
import java.util.List;
import org.jreserve.triangle.entities.TriangleComment;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractTriangularDataModification implements TriangularDataModification {

    protected TriangularData source = TriangularData.EMPTY;
    
    protected AbstractTriangularDataModification() {
    }
    
    protected AbstractTriangularDataModification(TriangularData source) {
        this.source = source==null? TriangularData.EMPTY : source;
    }
    
    @Override
    public void setSource(TriangularData source) {
        this.source = source==null? TriangularData.EMPTY : source;
    }

    @Override
    public int getAccidentCount() {
        return source.getAccidentCount();
    }

    @Override
    public int getDevelopmentCount() {
        return source.getDevelopmentCount();
    }

    @Override
    public int getDevelopmentCount(int accident) {
        return source.getDevelopmentCount(accident);
    }

    @Override
    public Date getAccidentName(int accident) {
        return source.getAccidentName(accident);
    }

    @Override
    public Date getDevelopmentName(int accident, int development) {
        return source.getDevelopmentName(accident, development);
    }

    @Override
    public double[][] toArray() {
        int accidents = getAccidentCount();
        double[][] result = new double[accidents][];
        for(int a=0; a<accidents; a++)
            result[a] = toArray(a);
        return result;
    }
    
    private double[] toArray(int accident) {
        int developments = getDevelopmentCount(accident);
        double[] result = new double[developments];
        for(int d=0; d<developments; d++)
            result[d] = getValue(accident, d);
        return result;
    }

    @Override
    public List<TriangleComment> getComments(int accident, int development) {
        return source.getComments(accident, development);
    }

    @Override
    public List<TriangularData> getLayers() {
        List<TriangularData> layers = source.getLayers();
        layers.add(this);
        return layers;
    }

    @Override
    public void close() {
        if(source != null)
            source.close();
    }
    
    
}
