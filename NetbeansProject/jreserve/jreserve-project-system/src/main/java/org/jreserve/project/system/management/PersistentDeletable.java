package org.jreserve.project.system.management;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Session;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 */
public class PersistentDeletable extends AbstractProjectElementDeletable {
    
    protected final static Logger logger = Logger.getLogger(PersistentDeletable.class.getName());

    private boolean managesSession = false;

    public PersistentDeletable(ProjectElement element) {
        super(element);
    }
    
    @Override
    protected void handleSave(Session session) {
        session = initSession(session);
        try {
            super.handleSave(session);
            deleteEntity(session);
            commit(session);
        } catch (RuntimeException ex) {
            rollBack(session);
            if(managesSession)
                logger.log(Level.SEVERE, String.format("Unable to delete: ", element.getValue()), ex);
            throw ex;
        }
    }
    
    private Session initSession(Session session) {
        managesSession = session == null;
        if(managesSession)
            session = createSession();
        return session;
    }
    
    private Session createSession() {
        PersistenceUnit pu = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
        Session session = pu.getSession();
        session.beginTransaction();
        return session;
    }
    
    private void deleteEntity(Session session) {
        Object entity = element.getValue();
        logger.log(Level.INFO, "Deleting entity: \"{0}\".", entity);
        try {
            cleanUpBeforeEntity(session);
            session.delete(element.getValue());
            cleanUpAfterEntity(session);
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, String.format("Unable to delete entity '%s'!", entity), ex);
            throw ex;
        }
    }
    
    protected void cleanUpBeforeEntity(Session session) {
    }
    
    protected void cleanUpAfterEntity(Session session) {
    }
    
    private void commit(Session session) {
        if(managesSession)
            session.comitTransaction();
    }
    
    private void rollBack(Session session) {
        if(managesSession) {
            session.rollBackTransaction();
        }
    }
}
