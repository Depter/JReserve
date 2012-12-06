package org.jreserve.data.query;

import org.hibernate.Session;
import org.jreserve.data.DataCriteria;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClearDataQuery extends AbstractQuery implements DataQuery<Integer> {
    
    @Override
    public Integer query(Session session, DataCriteria criteria) {
        String hql = super.createHQL(criteria);
        hql = "DELETE "+hql;
        return session.createQuery(hql).executeUpdate();
    }
}
