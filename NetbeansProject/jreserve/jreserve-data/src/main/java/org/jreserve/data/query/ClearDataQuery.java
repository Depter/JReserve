package org.jreserve.data.query;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.data.Criteria;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClearDataQuery extends AbstractQuery implements DataQuery<Integer> {

    @Override
    public Integer query(Session session, Criteria criteria) {
        String sql = buildCriteria("DELETE FROM ClaimValue c", criteria);
        Query query = session.createQuery(sql);
        return query.executeUpdate();
    }
}
