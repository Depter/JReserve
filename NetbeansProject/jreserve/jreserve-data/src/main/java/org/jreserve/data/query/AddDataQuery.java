package org.jreserve.data.query;

import java.util.Date;
import java.util.List;
import org.jreserve.data.Data;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.data.entities.ClaimValuePk;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AddDataQuery {
    
    public AddDataQuery() {
    }
    
    public void add(Session session, Project project, List<Data> datas) {
        for(Data data : datas)
            add(session, project, data);
    }
    
    private void add(Session session, Project project, Data data) {
        boolean update = true;
        ClaimValue cv = getPersistedClaimValue(session, project, data);
        if(cv == null) {
            cv = createClaimValue(project, data);
            update = false;
        }
        cv.setClaimValue(data.getValue());
        saveClaimValue(update, session, cv);
    }
    
    private ClaimValue getPersistedClaimValue(Session session, Project project, Data data) {
        ClaimValuePk id = new ClaimValuePk(project, data);
        return session.find(ClaimValue.class, id);
    }
    
    private ClaimValue createClaimValue(Project project, Data data) {
        Date accident = data.getAccidentDate();
        Date development = data.getDevelopmentDate();
        ClaimValue cv = new ClaimValue(project, data.getDataType(), accident, development);
        return cv;
    }
    
    private void saveClaimValue(boolean update, Session session, ClaimValue cv) {
        if(update) {
            session.update(cv);
        } else {
            session.persist(cv);
        }
    }
}