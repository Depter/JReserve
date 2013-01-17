package org.jreserve.triangle;

import org.jreserve.persistence.PersistentObject;
import org.jreserve.triangle.TriangularData;
import org.jreserve.triangle.TriangularDataModification;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface TriangleModification extends PersistentObject, Comparable<TriangleModification> {

    public int getOrder();
    
    public TriangularDataModification createModification(TriangularData source);
}
