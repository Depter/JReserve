package org.jreserve.triangle;

import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiableTriangle extends TriangularData.Provider {

    public PersistentObject getOwner();
    
    public int getMaxModificationOrder();
    
    public void addModification(ModifiedTriangularData modification);
}
