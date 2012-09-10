package org.jreserve.persistence.connection;

import java.util.List;
import org.hibernate.Session;
import org.jreserve.persistence.Query;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class HibernateSession implements org.jreserve.persistence.Session {
    
    private final HibernatePersistenceUnit pu;
    private final Session session;
    private final int id;
    private boolean isClosed = false;
    
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
        close();
    }

    @Override
    public void rollBackTransaction() {
        session.getTransaction().rollback();
        close();
    }

    @Override
    public void close() {
        setToClosedState();
        closeHibernateSession();
        pu.sessionClosed(this);
    }
    
    private void setToClosedState() {
        if(isClosed)
            throw new IllegalStateException("Session is already closed!");
        isClosed = true;
    }
    
    private void closeHibernateSession() {
        if(session.getTransaction().isActive())
            throw new IllegalStateException("Transaction is still active! Have to call commit or rollBack first!");
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

    @Override
    public void delete(Object entity) {
        session.delete(entity);
    }

    @Override
    public void delete(Object... entities) {
        for(Object entity : entities)
            delete(entity);
    }

    @Override
    public void update(Object entity) {
        session.update(entity);
    }

    @Override
    public void update(Object... entities) {
        for(Object entity : entities)
            update(entity);
    }
    
    @Override
    public Query createQuery(String query) {
        return new HibernateQuery(session.createQuery(query));
    }
    
    @Override
    public Query createNamedQuery(String name) {
        return new HibernateQuery(session.getNamedQuery(name));
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
