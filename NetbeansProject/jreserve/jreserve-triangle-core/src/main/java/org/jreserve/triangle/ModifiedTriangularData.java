package org.jreserve.triangle;

import org.hibernate.Session;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiedTriangularData extends TriangularData, Comparable<ModifiedTriangularData> {
    
    public String getOwnerId();

    public int getOrder();
    
    public void setSource(TriangularData source);
    
    public void save(Session session);
    
    public void delete(Session session);
}
