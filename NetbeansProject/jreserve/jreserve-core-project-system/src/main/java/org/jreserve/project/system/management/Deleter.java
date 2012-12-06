package org.jreserve.project.system.management;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jreserve.persistence.SessionFactory;

/**
 *
 * @author Peter Decsi
 */
public class Deleter {

    private final static Logger logger = Logger.getLogger(Deleter.class.getName());
    
    private boolean used = false;
    private List<Deletable> deletables;
    private Session session;
    private boolean initiatedTransaction;
    
    public Deleter(List<Deletable> deletables) {
        if(deletables == null)
            throw new NullPointerException("Deletables was null!");
        this.deletables = new ArrayList<Deletable>(deletables);
    }
    
    public void delete() {
        checkState();
        try {
            beginTransaction();
            deleteElements();
            commit();
        } catch (RuntimeException ex) {
            rollBack();
            logger.log(Level.SEVERE, "Unable to delete elements! Transaction was rolled back.", ex);
        }
    }
    
    private void beginTransaction() {
        session = SessionFactory.getCurrentSession();
        Transaction tx = session.getTransaction();
        initiatedTransaction = !tx.isActive();
        if(initiatedTransaction) 
            session.beginTransaction();
    }
    
    private void checkState() {
        if(used) 
            throw new IllegalArgumentException("delete() method was already called!");
        used = true;
    }
    
    private void deleteElements() {
        for(Deletable deletable : deletables)
            deletable.delete();
        deletables = null;
    }
    
    private void rollBack() {
        if(session != null && initiatedTransaction)
            session.getTransaction().rollback();
        session = null;
    }
    
    private void commit() {
        if(initiatedTransaction)
            session.getTransaction().commit();
        session = null;
    }
}
