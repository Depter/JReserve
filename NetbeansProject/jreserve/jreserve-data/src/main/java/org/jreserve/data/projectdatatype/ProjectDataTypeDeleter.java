package org.jreserve.data.projectdatatype;

import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ProjectSystemDeletionListener;

/**
 *
 * @author Peter Decsi
 */
public class ProjectDataTypeDeleter implements ProjectSystemDeletionListener {

    private final static String SQL = 
            "delete from ProjectDataType dt where dt.project.id= :projectId";
    
    private final static Logger logger = Logging.getLogger(ProjectDataTypeDeleter.class.getName());
    
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
            logger.error(ex, "Unable to delete data types from: %s", child.getValue());
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
        logger.debug("Deleting data types from project '%s'.", project.getName());
        query.executeUpdate();
    }
}
