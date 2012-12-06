package org.jreserve.triangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleModification {
    
    public int getOrder();
    
    public String getOwnerId();
    
    public ModifiedTriangularData createModification();
}
