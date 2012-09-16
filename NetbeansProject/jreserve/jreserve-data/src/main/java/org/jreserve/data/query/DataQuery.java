package org.jreserve.data.query;

import org.jreserve.data.Criteria;
import org.jreserve.persistence.Session;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DataQuery<T> {
    
    public T query(Session session, Criteria criteria);
}
