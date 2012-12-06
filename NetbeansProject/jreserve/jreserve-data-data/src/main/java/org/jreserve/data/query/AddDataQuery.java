package org.jreserve.data.query;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.entities.ClaimValue;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AddDataQuery extends AbstractQuery {
    
    private Map<String, ProjectDataType> dataTypes = new HashMap<String, ProjectDataType>();
    
    public void add(Session session, List<ClaimValue> datas) {
        for(ClaimValue data : datas) {
            loadPersistence(session, data);
            add(session, data);
        }
        logImport(session);
        clearPersistence();
    }
    
    private void loadPersistence(Session session, ClaimValue data) {
        String id = data.getDataType().getId();
        if(!dataTypes.containsKey(id))
            dataTypes.put(id, getDataType(session, id));
    }
    
    private ProjectDataType getDataType(Session session, String id) {
        return (ProjectDataType) session.get(ProjectDataType.class, id);
    }
    
    private void add(Session session, ClaimValue data) {
        boolean update = true;
        ClaimValue cv = getPersistedClaimValue(session, data);
        if(cv == null) {
            cv = createClaimValue(data);
            update = false;
        }
        cv.setClaimValue(data.getClaimValue());
        saveClaimValue(update, session, cv);
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
    
    private ClaimValue createClaimValue(ClaimValue data) {
        Date accident = data.getAccidentDate();
        Date development = data.getDevelopmentDate();
        ProjectDataType owner = data.getDataType();
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