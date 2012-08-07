package org.jreserve.persistence.connection;

import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class HibernateSession implements org.jreserve.persistence.Session {
    
    private final HibernatePersistenceUnit pu;
    private final Session session;
    private final int id;
    
    public HibernateSession(Session session, HibernatePersistenceUnit pu, int id) {
        this.pu = pu;
        this.session = session;
        this.id = id;
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
        pu.sessionClosed(this);
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
    
    int getId() {
        return id;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof HibernateSession)
            return id == ((HibernateSession)o).id;
        return false;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public String toString() {
        return String.format("HibernateSession [%d]", id);
    }
}
