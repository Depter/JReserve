package org.jreserve.project.changelog;

import java.util.logging.Level;
import java.util.logging.Logger;
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
            "delete from ChangeLog c where c.project.id= ':projectId'";
    
    private final static Logger logger = Logger.getLogger(ChangeLogDeleter.class.getName());
    
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
            logger.log(Level.SEVERE, String.format("Unable to delete change logs for: %s", child.getValue()), ex);
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
        logger.log(Level.FINE, "Deleted change logs from project \"{0}\".", project.getName());
        query.executeUpdate();
    }
}
