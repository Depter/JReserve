package org.jreserve.project.system.management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jreserve.persistence.DeleteUtil;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class PersistentSavable<T> extends AbstractProjectElementSavable<T> {
    
    protected Session session;
    private Transaction tx;
    
    protected final static Logger logger = Logger.getLogger(PersistentSavable.class.getName());

    public PersistentSavable(ProjectElement<T> element) {
        super(element);
    }
    
    @Override
    protected void saveElement() throws IOException {
        try {
            beginTransaction();
            saveEntity();
            commit();
        } catch (Exception ex) {
            rollBack();
            logger.log(Level.SEVERE, "Unable to save entities, transaction is rolled back.", ex);
            throw new IOException(ex);
        } finally {
            element.removeFromLookup(this);
            closeSession();
        }
    }

    private void beginTransaction() {
        session = SessionFactory.openSession();
        tx = session.beginTransaction();
    }
    
    protected void saveEntity() {
        session.update(element.getValue());
    }
        
    protected <T extends PersistentObject> void addEntities(DeleteUtil deleter, Class<T> clazz, String property) {
        List<T> removed = new ArrayList<T>((List<T>) originalProperties.get(property));
        removed.removeAll((List<T>) element.getProperty(property));
        if(!removed.isEmpty())
            deleter.addEntities(clazz, removed);
    }
    
    private void commit() {
        if(tx != null) {
            tx.commit();
            tx = null;
        }
    }
    
    private void rollBack() {
        if(tx != null) {
            tx.rollback();
            tx = null;
        }
    }
    
    private void closeSession() {
        if(session != null) {
            session.close();
            session = null;
        }
    }
}
