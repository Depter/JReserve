package org.jreserve.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jreserve.persistence.connection.HibernateUtil;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EntityLoader {
    
    private final Result<Session> result;
    
    private final LookupListener listener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent le) {
            resultCanged();
        }
    };
    
    public EntityLoader() {
        result = HibernateUtil.getLookup().lookupResult(Session.class);
        result.addLookupListener(listener);
        initialize();
    }
    
    private void initialize() {
        Session session = Lookup.getDefault().lookup(Session.class);
        if(session != null)
            persistenceUnitOpened(session);
    }
    
    private void resultCanged() {
        Collection<? extends Session> sessions = result.allInstances();
        if(sessions.isEmpty())
            persistenceUnitClosed();
        else
            persistenceUnitOpened(sessions);
    }
    
    protected void persistenceUnitClosed() {
    }
    
    private void persistenceUnitOpened(Collection<? extends Session> sessions) {
        List<Session> sessionList = new ArrayList<Session>(sessions);
        persistenceUnitOpened(sessionList.get(0));
    }
    
    protected void persistenceUnitOpened(Session session) {
    }
}
