package org.jreserve.triangle;

import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiableTriangle {

    public PersistentObject getOwner();
    
    public int getMaxModificationOrder();
}
