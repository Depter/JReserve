package org.jreserve.data.query;

import org.hibernate.Session;
import org.jreserve.data.DataCriteria;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DataQuery<V> {
    
    public V query(Session session, DataCriteria criteria);
}
