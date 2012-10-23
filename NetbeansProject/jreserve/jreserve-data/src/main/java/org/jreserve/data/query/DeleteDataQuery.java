package org.jreserve.data.query;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.data.Data;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.data.entities.ClaimValuePk;
import org.jreserve.data.entities.DataLog;

/**
 *
 * @author Peter Decsi
 */
public class DeleteDataQuery {
    
    private Set<ProjectDataType> dataTypes = new HashSet<ProjectDataType>();
    
    public DeleteDataQuery() {
    }
    
    public void delete(Session session, List<Data> datas) {
        for(Data data : datas)
            delete(session, data);
        logDeletion(session);
    }
    
    private void delete(Session session, Data data) {
        ClaimValue cv = getPersistedClaimValue(session, data);
        dataTypes.add(cv.getDataType());
        if(cv != null)
            session.delete(cv);
    }
    
    private ClaimValue getPersistedClaimValue(Session session, Data data) {
        Date accident = data.getAccidentDate();
        Date development = data.getDevelopmentDate();
        ClaimValuePk id = new ClaimValuePk(data.getDataType(), accident, development);
        return (ClaimValue) session.load(ClaimValue.class, id);
    }
    
    
    private void logDeletion(Session session) {
        for(ProjectDataType dataType : dataTypes)
            DataLogUtil.logDeletion(session, dataType);
    }
}
