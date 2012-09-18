package org.jreserve.project.changelog;

import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ProjectSystemDeletionListener;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 */
@ServiceProvider(service=ProjectSystemDeletionListener.class)
public class ChangeLogDeleter implements ProjectSystemDeletionListener {

    private final static String SQL = 
            "delete from ChangeLog c where c.project.id= :projectId";
    
    private final static Logger logger = Logging.getLogger(ChangeLogDeleter.class.getName());
    
    private Session session;
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Project);
    }

    @Override
    public void deleted(ProjectElement parent, ProjectElement child) {
        try {
            initSession();
            deleteChangeLogs((Project) child.getValue());
            session.comitTransaction();
        } catch(RuntimeException ex) {
            logger.error(ex, "Unable to delete change logs for: %s", child.getValue());
            session.rollBackTransaction();
        } finally {
            session = null;
        }
    }
    
    private void initSession() {
        PersistenceUnit pu = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
        session = pu.getSession();
        session.beginTransaction();
    }
    
    private void deleteChangeLogs(Project project) {
        Query query = session.createQuery(SQL);
        query.setParameter("projectId", project.getId());
        logger.debug("Deleted %d change logs for project '%s'.", query.executeUpdate(), project.getName());
    }
}
