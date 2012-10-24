package org.jreserve.data.query;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.jreserve.data.Data;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.persistence.PersistentObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AddDataQuery extends AbstractQuery {
    
    private Map<String, ProjectDataType> dataTypes = new HashMap<String, ProjectDataType>();
    
    public AddDataQuery() {
        super(ClaimValue.class);
    }
    
    public void add(Session session, List<Data<ProjectDataType, Double>> datas) {
        for(Data<ProjectDataType, Double> data : datas) {
            loadPersistence(session, data);
            add(session, data);
        }
        logImport(session);
        clearPersistence();
    }
    
    private void loadPersistence(Session session, Data<ProjectDataType, Double> data) {
        String id = data.getOwner().getId();
        if(!dataTypes.containsKey(id))
            dataTypes.put(id, getDataType(session, id));
    }
    
    private ProjectDataType getDataType(Session session, String id) {
        return (ProjectDataType) session.get(ProjectDataType.class, id);
    }
    
    private void add(Session session, Data<ProjectDataType, Double> data) {
        boolean update = true;
        ClaimValue cv = getPersistedClaimValue(session, data);
        if(cv == null) {
            cv = createClaimValue(data);
            update = false;
        }
        cv.setClaimValue(data.getValue());
        saveClaimValue(update, session, cv);
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
    
    private ClaimValue createClaimValue(Data data) {
        Date accident = data.getAccidentDate();
        Date development = data.getDevelopmentDate();
        PersistentObject owner = data.getOwner();
        return new ClaimValue(owner, accident, development);
    }
    
    private void saveClaimValue(boolean update, Session session, ClaimValue cv) {
        if(update) {
            session.update(cv);
        } else {
            session.persist(cv);
        }
    }
    
    private void logImport(Session session) {
        for(ProjectDataType dataType : dataTypes.values())
            DataLogUtil.logImport(session, dataType);
    }
    
    private void clearPersistence() {
        dataTypes.clear();
    }
}