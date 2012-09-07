package org.jreserve.project.system.management;

import javax.swing.SwingUtilities;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Session;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 */
public abstract class PersistentDeletable implements Deletable {
    
    private final static Logger logger = Logging.getLogger(PersistentDeletable.class.getName());
    
    protected final ProjectElement element;
    protected Session session;

    protected PersistentDeletable(ProjectElement element) {
        this.element = element;
    }
    
    @Override
    public void delete() {
        initSession();
        deleteEntity();
        deleteProjectElement();
    }
    
    private void initSession() {
        PersistenceUnit pu = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
        session = pu.getSession();
        session.beginTransaction();
    }
    
    protected abstract void cleanUpEntity();
    
    protected void deleteEntity() {
        Object entity = element.getValue();
        logger.info("Deleting entity: %s.", entity);
        try {
            session.delete(element.getValue());
            session.comitTransaction();
        } catch (RuntimeException ex) {
            session.rollBackTransaction();
            logger.error(ex, "Unable to delete entity '%s'!", entity);
            throw ex;
        }
    }
    
    protected void deleteProjectElement() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                element.removeFromParent();
            }
        });
    }
}
