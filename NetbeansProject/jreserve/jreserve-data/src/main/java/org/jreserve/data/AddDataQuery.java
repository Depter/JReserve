package org.jreserve.data;

import java.util.List;
import org.jreserve.data.entities.ClaimValue;
import org.jreserve.data.entities.ClaimValuePk;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.ClaimType;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class AddDataQuery {
    
    private boolean overwrite = false;
    
    AddDataQuery(boolean overwrite) {
        this.overwrite = overwrite;
    }
    
    void add(Session session, ClaimType ct, List<Data> datas) {
        for(Data data : datas)
            add(session, ct, data);
    }
    
    private void add(Session session, ClaimType ct, Data data) {
        if(overwrite)
            overwrite(session, ct, data);
        else
            createIfNew(session, ct, data);
    }
    
    private ClaimValue getExisting(Session session, ClaimType ct, Data data) {
        ClaimValuePk pk = new ClaimValuePk(ct, data);
        return session.find(ClaimValue.class, pk);
    }
    
    private void overwrite(Session session, ClaimType ct, Data data) {
        ClaimValue existing = getExisting(session, ct, data);
        if(existing == null) {
            create(session, ct, data);
        } else {
            existing.setClaimValue(data.getValue());
            session.update(existing);
        }
    }
    
    private void create(Session session, ClaimType ct, Data data) {
        ClaimValue cv = new ClaimValue(ct, data.getDataType(), data.getAccidentDate(), data.getDevelopmentDate());
        cv.setClaimValue(data.getValue());
        session.persist(cv);    
    }
    
    private void createIfNew(Session session, ClaimType ct, Data data) {
        ClaimValue existing = getExisting(session, ct, data);
        if(existing == null)
            create(session, ct, data);
    }
}