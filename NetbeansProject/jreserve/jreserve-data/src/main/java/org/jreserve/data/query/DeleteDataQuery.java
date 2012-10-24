package org.jreserve.data.query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import org.jreserve.data.Data;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.ClaimValue;

/**
 *
 * @author Peter Decsi
 */
public class DeleteDataQuery extends AbstractQuery {
    
    private Set<ProjectDataType> dataTypes = new HashSet<ProjectDataType>();
    
    public DeleteDataQuery() {
        super(ClaimValue.class);
    }
    
    public void delete(Session session, List<Data<ProjectDataType, Double>> datas) {
        for(Data<ProjectDataType, Double> data : datas)
            delete(session, data);
        logDeletion(session);
    }
    
    private void delete(Session session, Data<ProjectDataType, Double> data) {
        dataTypes.add(data.getOwner());
        ClaimValue cv = getPersistedClaimValue(session, data);
        if(cv != null)
            session.delete(cv);
    }
    
    private ClaimValue getPersistedClaimValue(Session session, Data data) {
        DataCriteria criteria = createCriteria(data);
        return (ClaimValue) super.queryUniqueResult(session, criteria);
    }
    
    private DataCriteria createCriteria(Data data) {
        return new DataCriteria(data.getOwner())
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
