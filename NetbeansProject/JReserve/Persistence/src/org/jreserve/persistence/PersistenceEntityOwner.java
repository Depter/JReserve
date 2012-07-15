package org.jreserve.persistence;

/**
 * Those modules that wishes to provide JPA entity classes should provide an
 * implementation for this interface. The interface should return all classes
 * that should have a persistance state. All the returnes classes should be
 * annotated according to the JPA specification.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface PersistenceEntityOwner {
    
    /**
     * Returns the classes that should be managed by an EntityManager.
     */
    public Class[] getEntities();
            
}
