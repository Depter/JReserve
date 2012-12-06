package org.jreserve.data.query;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.data.ProjectDataType;
import org.jreserve.project.entities.ClaimType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DistinctDataTypesQuery {
    
    private final static String DATA_TYPES = 
        "SELECT dt " + 
        "FROM ProjectDataType dt " + 
        "WHERE dt.claimType.id = :claimTypeId " + 
        "ORDER BY dt.dbId";

    
    public List<ProjectDataType> query(Session session, ClaimType claimType) {
        Query query = session.createQuery(DATA_TYPES);
        query.setParameter("claimTypeId", claimType.getId());
        return query.list();
    }
}