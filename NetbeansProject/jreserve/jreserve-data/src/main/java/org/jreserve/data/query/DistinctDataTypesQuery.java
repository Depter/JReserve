package org.jreserve.data.query;

import java.util.List;
import org.jreserve.data.Criteria;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DistinctDataTypesQuery implements DataQuery<List<ProjectDataType>> {
    
    private final static String DATA_TYPES = 
        "SELECT dt " + 
        "FROM ProjectDataType dt " + 
        "WHERE dt.claimType.id = ':claimTypeId' " + 
        "ORDER BY dt.dbId";

    
    @Override
    public List<ProjectDataType> query(Session session, Criteria criteria) {
        Query query = session.createQuery(DATA_TYPES);
        query.setParameter("claimTypeId", criteria.getClaimType().getId());
        return query.getResultList();
    }
}