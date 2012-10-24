package org.jreserve.data.entities;

import org.jreserve.data.Data;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DataFactory<O extends PersistentObject, E extends AbstractData, V> {
    
    public Data<O, V> createData(E entity);
}
