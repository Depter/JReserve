package org.jreserve.data.query;

import java.util.Date;
import java.util.List;
import org.jreserve.data.Data;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.data.entities.ClaimValuePk;
import org.jreserve.persistence.Session;

/**
 *
 * @author Peter Decsi
 */
public class DeleteDataQuery {
    
    public DeleteDataQuery() {
    }
    
    public void delete(Session session, List<Data> datas) {
        for(Data data : datas)
            delete(session, data);
    }
    
    private void delete(Session session, Data data) {
        ClaimValue cv = getPersistedClaimValue(session, data);
        if(cv != null)
            session.delete(cv);
    }
    
    private ClaimValue getPersistedClaimValue(Session session, Data data) {
        Date accident = data.getAccidentDate();
        Date development = data.getDevelopmentDate();
        ClaimValuePk id = new ClaimValuePk(data.getDataType(), accident, development);
        return session.find(ClaimValue.class, id);
    }
}
