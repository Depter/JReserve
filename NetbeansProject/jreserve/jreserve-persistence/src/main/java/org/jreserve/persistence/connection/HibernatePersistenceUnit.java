package org.jreserve.persistence.connection;

import org.hibernate.SessionFactory;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.Session;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class HibernatePersistenceUnit implements PersistenceUnit {
    
    private final SessionFactory factory;

    HibernatePersistenceUnit(SessionFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public Session getSession() {
        org.hibernate.Session session = factory.openSession();
        return new HibernateSession(session);
    }

    void close() {
        factory.close();
    }
}
