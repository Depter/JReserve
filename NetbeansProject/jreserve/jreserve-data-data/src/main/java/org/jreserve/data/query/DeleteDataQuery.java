package org.jreserve.data.query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.ClaimValue;

/**
 *
 * @author Peter Decsi
 */
public class DeleteDataQuery extends AbstractQuery {
    
    private Set<ProjectDataType> dataTypes = new HashSet<ProjectDataType>();
    
    public void delete(Session session, List<ClaimValue> datas) {
        for(ClaimValue data : datas)
            delete(session, data);
        logDeletion(session);
    }
    
    private void delete(Session session, ClaimValue data) {
        dataTypes.add(data.getDataType());
        ClaimValue cv = getPersistedClaimValue(session, data);
        if(cv != null)
            session.delete(cv);
    }
    
    private ClaimValue getPersistedClaimValue(Session session, ClaimValue data) {
        DataCriteria criteria = createCriteria(data);
        return (ClaimValue) super.queryUniqueResult(session, criteria);
    }
    
    private DataCriteria createCriteria(ClaimValue data) {
        return new DataCriteria(data.getDataType())
               .setFromAccidentDate(data.getAccidentDate())
               .setFromAccidentEqt(DataCriteria.EQT.EQ)
               .setFromDevelopmentDate(data.getDevelopmentDate())
               .setFromDevelopmentEqt(DataCriteria.EQT.EQ);
    }
    
    
    private void logDeletion(Session session) {
        for(ProjectDataType dataType : dataTypes)
            DataLogUtil.logDeletion(session, dataType);
    }
}
