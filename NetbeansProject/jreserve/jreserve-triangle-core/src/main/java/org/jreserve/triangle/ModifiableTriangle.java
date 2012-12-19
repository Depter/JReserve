package org.jreserve.triangle;

import java.util.List;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface ModifiableTriangle extends TriangularData.Provider {

    public PersistentObject getOwner();
    
    public int getMaxModificationOrder();
    
    public List<ModifiedTriangularData> getModifications();
    
    public void addModification(ModifiedTriangularData modification);
    
    public void removeModification(ModifiedTriangularData modification);
}
