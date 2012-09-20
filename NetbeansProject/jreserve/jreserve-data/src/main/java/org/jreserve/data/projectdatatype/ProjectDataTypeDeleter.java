package org.jreserve.data.projectdatatype;

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
@ServiceProvider(service=ProjectSystemDeletionListener.class, position=10)
public class ProjectDataTypeDeleter implements ProjectSystemDeletionListener {

    private final static String SQL = 
            "delete from ProjectDataType dt where dt.project.id= :projectId";
    
    private final static Logger logger = Logger.getLogger(ProjectDataTypeDeleter.class.getName());
    
    private Session session;

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Project);
    }

    @Override
    public void deleted(ProjectElement parent, ProjectElement child) {
        try {
            initSession();
            deleteProjectDataTypes((Project) child.getValue());
            session.comitTransaction();
        } catch(RuntimeException ex) {
            logger.log(Level.SEVERE, String.format("Unable to delete data types from: %s", child.getValue()), ex);
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
    
    private void deleteProjectDataTypes(Project project) {
        Query query = session.createQuery(SQL);
        query.setParameter("projectId", project.getId());
        logger.log(Level.FINE, "Deleting data types from project '%s'.", project.getName());
        query.executeUpdate();
    }
}
