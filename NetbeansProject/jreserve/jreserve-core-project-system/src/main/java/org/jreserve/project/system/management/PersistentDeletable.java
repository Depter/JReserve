package org.jreserve.project.system.management;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 */
public class PersistentDeletable<T> extends AbstractProjectElementDeletable<T> {
    
    protected final static Logger logger = Logger.getLogger(PersistentDeletable.class.getName());

    public PersistentDeletable(ProjectElement<T> element) {
        super(element);
    }
    
    @Override
    protected void handleSave() {
        Session session = SessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        //Session session = SessionFactory.getCurrentSession();
        try {
            super.handleSave();
            deleteEntity(session);
            tx.commit();
            session.close();
        } catch (RuntimeException ex) {
            tx.rollback();
            session.close();
            throw ex;
        }
    }
    
    protected void deleteEntity(Session session) {
        T entity = element.getValue();
        logger.log(Level.INFO, "Deleting entity: \"{0}\".", entity);
        try {
            cleanUpBeforeEntity(session);
            deleteEntity(session, entity);
            cleanUpAfterEntity(session);
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, String.format("Unable to delete entity '%s'!", entity), ex);
            throw ex;
        }
    }
    
    protected void cleanUpBeforeEntity(Session session) {
    }
    
    protected void deleteEntity(Session session, T entity) {
        Object contained = session.merge(entity);
        session.delete(contained);
    }
    
    protected void cleanUpAfterEntity(Session session) {
    }
    
    @Override
    public String toString() {
        String msg = "PersistentDeletable [toDdelete = %s]";
        return String.format(msg, element.getValue());
    }
}
