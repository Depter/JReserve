package org.jreserve.data.query;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.entities.ClaimValue;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SelectDataQuery extends AbstractQuery implements DataQuery<List<ClaimValue>> {
    
    @Override
    public List<ClaimValue> query(Session session, DataCriteria criteria) {
        List<ClaimValue> result = new ArrayList<ClaimValue>();
        for(ClaimValue value : queryList(session, criteria))
            result.add(value);
        return result;
    }
}