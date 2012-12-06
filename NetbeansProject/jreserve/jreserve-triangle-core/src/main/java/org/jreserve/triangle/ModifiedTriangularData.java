package org.jreserve.triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedTriangularData extends TriangularData, Comparable<ModifiedTriangularData> {

    public int getOrder();
    
    public void setSource(TriangularData source);
}
