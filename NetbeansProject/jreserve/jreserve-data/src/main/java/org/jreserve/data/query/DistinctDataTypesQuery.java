package org.jreserve.data.query;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.data.Criteria;
import org.jreserve.data.DataType;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DistinctDataTypesQuery implements DataQuery<List<DataType>> {
    
    private final static String DATA_TYPES = 
        "SELECT DISTINCT c.dataTypeId FROM ClaimValue c WHERE c.claimType.id = :claimTypeId ORDER BY c.dataTypeId";

    
    @Override
    public List<DataType> query(Session session, Criteria criteria) {
        Query query = session.createQuery(DATA_TYPES);
        query.setParameter("claimTypeId", criteria.getClaimType().getId());
        return getDataTypes(query.getResultList());
    }
    
    private List<DataType> getDataTypes(List<Number> ids) {
        List<DataType> dts = new ArrayList<DataType>(ids.size());
        for(Number n : ids)
            dts.add(DataType.parse(n.intValue()));
        return dts;
    }
}