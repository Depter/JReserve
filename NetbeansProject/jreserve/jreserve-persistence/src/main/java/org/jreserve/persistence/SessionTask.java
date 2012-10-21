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
    
    private void beginTransaction() {
        session = SessionFactory.openSession();
        tx = session.beginTransaction();
    }
    
    protected abstract T doTask() throws Exception;
    
    private void comit() {
        if(tx != null) {
            tx.commit();
            tx = null;
        }
    }
    
    private void rollback() {
        if(tx != null) {
            tx.rollback();
            tx = null;
        }
    }
    
    private void close() {
        if(session != null) {
            session.close();
            session = null;
        }
    }
}
