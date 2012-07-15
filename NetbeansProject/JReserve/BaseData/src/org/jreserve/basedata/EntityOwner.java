package org.jreserve.basedata;

import org.jreserve.persistence.PersistenceEntityOwner;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=PersistenceEntityOwner.class)
public class EntityOwner implements PersistenceEntityOwner {
    
    private final static Class[] ENTITIES = {
        LoB.class,
        ClaimType.class
    };
    
    @Override
    public Class[] getEntities() {
        return ENTITIES;
    }
}
