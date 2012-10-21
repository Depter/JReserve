package org.jreserve.data.query;

import org.hibernate.Session;
import org.jreserve.data.Criteria;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DataQuery<T> {
    
    public T query(Session session, Criteria criteria);
}
