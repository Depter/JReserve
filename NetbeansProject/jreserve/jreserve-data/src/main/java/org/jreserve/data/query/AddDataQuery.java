package org.jreserve.data.query;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jreserve.data.Data;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.data.entities.ClaimValuePk;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.ClaimType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AddDataQuery {
    
    private Map<Long, ClaimType> claimTypes = new HashMap<Long, ClaimType>();
    private Map<Long, ProjectDataType> dataTypes = new HashMap<Long, ProjectDataType>();
    
    public AddDataQuery() {
    }
    
    public void add(Session session, List<Data> datas) {
        for(Data data : datas) {
            loadPersistence(session, data);
            add(session, data);
        }
        clearPersistence();
    }
    
    private void loadPersistence(Session session, Data data) {
        ProjectDataType dataType = data.getDataType();
        ClaimType claimType = dataType.getClaimType();
        loadPersistentClaimType(session, claimType);
        loadPersistentDataType(session, dataType);
    }
    
    private void loadPersistentClaimType(Session session, ClaimType claimType) {
        Long id = claimType.getId();
        if(!claimTypes.containsKey(id))
            claimTypes.put(id, session.find(ClaimType.class, id));
    }
    
    private void loadPersistentDataType(Session session, ProjectDataType dataType) {
        Long id = dataType.getId();
        if(!dataTypes.containsKey(id))
            dataTypes.put(id, session.find(ProjectDataType.class, id));
    }
    
    private void add(Session session, Data data) {
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
        Date accident = data.getAccidentDate();
        Date development = data.getDevelopmentDate();
        ClaimValuePk id = new ClaimValuePk(data.getDataType(), accident, development);
        return session.find(ClaimValue.class, id);
    }
    
    private ClaimValue createClaimValue(Data data) {
        Date accident = data.getAccidentDate();
        Date development = data.getDevelopmentDate();
        ClaimType claimType = getPersistentClaimType(data);
        ProjectDataType dataType = getPersistentDataType(data);
        return new ClaimValue(claimType, dataType, accident, development);
    }
    
    private ClaimType getPersistentClaimType(Data data) {
        long id = data.getDataType().getClaimType().getId();
        return claimTypes.get(id);
    }
    
    private ProjectDataType getPersistentDataType(Data data) {
        long id = data.getDataType().getId();
        return dataTypes.get(id);
    }
    
    private void saveClaimValue(boolean update, Session session, ClaimValue cv) {
        if(update) {
            session.update(cv);
        } else {
            session.persist(cv);
        }
    }
    
    private void clearPersistence() {
        claimTypes.clear();
        dataTypes.clear();
    }
}