package org.jreserve.triangle.data;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleModification extends TriangularData, Comparable<TriangleModification> {

    public int getOrder();
    
    public void setSource(TriangularData source);
}
