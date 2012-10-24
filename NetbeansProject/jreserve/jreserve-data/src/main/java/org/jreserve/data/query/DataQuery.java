package org.jreserve.data.query;

import org.hibernate.Session;
import org.jreserve.data.DataCriteria;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DataQuery<O extends PersistentObject, V> {
    
    public V query(Session session, DataCriteria<O> criteria);
}
