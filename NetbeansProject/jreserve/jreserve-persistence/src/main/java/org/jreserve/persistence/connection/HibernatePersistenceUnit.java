package org.jreserve.persistence.connection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.Session;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class HibernatePersistenceUnit implements PersistenceUnit {
    
    private final static Logger logger = Logger.getLogger(HibernatePersistenceUnit.class.getName());
    
    private int nextSessionId = 0;
    private final SessionFactory factory;
    private final Set<HibernateSession> openSessions = new HashSet<HibernateSession>();
    
    HibernatePersistenceUnit(SessionFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public synchronized Session getSession() {
        HibernateSession session = createNewSession();
        openSessions.add(session);
        logger.log(Level.FINE, "Session [{0}] created.", session.getId());
        return session;
    }

    private HibernateSession createNewSession() {
        org.hibernate.Session session = factory.openSession();
        int id = nextSessionId++;
        return new HibernateSession(session, this, id);
    }
    
    public org.hibernate.Session openHibernateSession() {
        return factory.openSession();
    }
    
    void sessionClosed(HibernateSession session) {
        openSessions.remove(session);
        logger.log(Level.FINE, "Session [{0}] closed.", session.getId());
    }
    
    void close() {
        for(HibernateSession session : new ArrayList<HibernateSession>(openSessions))
            session.close();
        factory.close();
    }
    
}
