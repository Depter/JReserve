package org.jreserve.persistence;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Peter Decsi
 */
public abstract class SessionTask<T> {

    protected Session session;
    private Transaction tx;
    private final boolean openSession;
    
    public SessionTask() {
        this(true);
    }
    
    public SessionTask(boolean openSession) {
        this.openSession = openSession;
    }
    
    public T getResult() throws Exception {
        try {
            beginTransaction();
            T result = doTask();
            comit();
            return result;
        } catch (Exception ex) {
            rollback();
            throw ex;
        } finally {
            close();
        }
    }
    
    protected void beginTransaction() {
        initSession();
        if(openSession)
            tx = session.beginTransaction();
    }
    
    protected void initSession() {
        session = openSession? SessionFactory.openSession() : 
                SessionFactory.getCurrentSession();
    }
    
    protected abstract T doTask() throws Exception;
    
    protected void comit() {
        if(tx != null && openSession) {
            tx.commit();
            tx = null;
        }
    }
    
    protected void rollback() {
        if(tx != null && openSession) {
            tx.rollback();
            tx = null;
        }
    }
    
    protected void close() {
        if(session != null && openSession) {
            session.close();
            session = null;
        }
    }
}
