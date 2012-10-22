package org.jreserve.project.system.management;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 */
public class PersistentDeletable extends AbstractProjectElementDeletable {
    
    protected final static Logger logger = Logger.getLogger(PersistentDeletable.class.getName());

    public PersistentDeletable(ProjectElement element) {
        super(element);
    }
    
    @Override
    protected void handleSave() {
        Session session = SessionFactory.getCurrentSession();
        try {
            super.handleSave();
            deleteEntity(session);
        } catch (RuntimeException ex) {
            throw ex;
        }
    }
    
    private void deleteEntity(Session session) {
        Object entity = element.getValue();
        logger.log(Level.INFO, "Deleting entity: \"{0}\".", entity);
        try {
            session.delete(element.getValue());
            cleanUpAfterEntity(session);
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, String.format("Unable to delete entity '%s'!", entity), ex);
            throw ex;
        }
    }
    
    protected void cleanUpAfterEntity(Session session) {
    }
}
