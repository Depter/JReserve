package org.jreserve.persistence.connection;

import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class HibernateSession implements org.jreserve.persistence.Session {
    
    private final Session session;

    public HibernateSession(Session session) {
        this.session = session;
    }
    
    @Override
    public void beginTransaction() {
        session.beginTransaction();
    }

    @Override
    public void comitTransaction() {
        session.getTransaction().commit();
    }

    @Override
    public void rollBackTransaction() {
        session.getTransaction().rollback();
    }

    @Override
    public void close() {
        session.close();
    }

    @Override
    public <E> List<E> getAll(Class<E> c) {
        String name = c.getSimpleName();
        return session.createQuery("from "+name).list();
    }

    @Override
    public void persist(Object entity) {
        session.persist(entity);
    }

    @Override
    public void persist(Object... entities) {
        for(Object entity : entities)
            persist(entity);
    }
}
