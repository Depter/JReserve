package org.jreserve.data.query;

import org.hibernate.Session;
import org.jreserve.data.DataCriteria;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClearDataQuery<O extends PersistentObject> extends AbstractQuery implements DataQuery<O, Integer> {
    
    public ClearDataQuery(Class entityClass) {
        super(entityClass);
    }
    
    @Override
    public Integer query(Session session, DataCriteria<O> criteria) {
        String hql = super.createHQL(criteria);
        hql = "DELETE "+hql;
        return session.createQuery(hql).executeUpdate();
    }
}
