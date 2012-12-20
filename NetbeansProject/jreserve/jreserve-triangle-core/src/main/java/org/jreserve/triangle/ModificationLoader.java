package org.jreserve.triangle;

import java.util.List;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 */
public interface ModificationLoader {

    public List<ModifiedTriangularData> loadModifications(PersistentObject owner);
    
}
