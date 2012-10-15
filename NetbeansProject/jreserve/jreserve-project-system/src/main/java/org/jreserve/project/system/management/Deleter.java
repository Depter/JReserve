package org.jreserve.project.system.management;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.persistence.Session;
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
    
    public Deleter(List<Deletable> deletables) {
        if(deletables == null)
            throw new NullPointerException("Deletables was null!");
        this.deletables = new ArrayList<Deletable>(deletables);
    }
    
    public void delete() {
        checkState();
        try {
            session = SessionFactory.beginTransaction();
            deleteElements();
            commit();
        } catch (RuntimeException ex) {
            rollBack();
            logger.log(Level.SEVERE, "Unable to delete elements! Transaction was rolled back.", ex);
        }
    }
    
    private void checkState() {
        if(used) 
            throw new IllegalArgumentException("delete() method was already called!");
        used = true;
    }
    
    private void deleteElements() {
        for(Deletable deletable : deletables)
            deletable.delete(session);
        deletables = null;
    }
    
    private void rollBack() {
        if(session != null) {
            session.rollBackTransaction();
            session = null;
        }
    }
    
    private void commit() {
        session.comitTransaction();
        session = null;
    }
}
