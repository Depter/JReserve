package org.jreserve.data;

import org.jreserve.persistence.Session;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
interface DataQuery<T> {
    
    
    public T query(Session session, Criteria criteria);
}
