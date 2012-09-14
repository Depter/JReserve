package org.jreserve.data;

import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClearDataQuery extends AbstractQuery implements DataQuery<Integer> {

    @Override
    public Integer query(Session session, Criteria criteria) {
        String sql = buildCriteria("DELETE FROM ClaimValue c", criteria);
        Query query = session.createQuery(sql);
        return query.executeUpdate();
    }
}
