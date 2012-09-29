package org.jreserve.data.projectdatatype;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.data.entities.ProjectDataType;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ProjectSystemDeletionListener;

/**
 *
 * @author Peter Decsi
 */
public class DataDeleter implements ProjectSystemDeletionListener {
    
    private final static String SQL = 
            "delete from ClaimValue c where c.dataType.id= :dataTypeId";
    
    private final static Logger logger = Logger.getLogger(DataDeleter.class.getName());
    
    private Session session;

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof ProjectDataType);
    }

    @Override
    public void deleted(ProjectElement parent, ProjectElement child) {
        try {
            initSession();
            deleteData((ProjectDataType) child.getValue());
            session.comitTransaction();
        } catch(RuntimeException ex) {
            logger.log(Level.SEVERE, String.format("Unable to delete claimvalues for: %s", child.getValue()), ex);
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
    
    private void deleteData(ProjectDataType dataType) {
        Query query = session.createQuery(SQL);
        query.setParameter("dataTypeId", dataType.getId());
        logDeletion(dataType);
        query.executeUpdate();
    }
    
    private void logDeletion(ProjectDataType dataType) {
        String msg = "Deleted claim data from '%s'/'%d - %s.";
        msg = String.format(msg, dataType.getProject().getName(), dataType.getDbId(), dataType.getName());
        logger.log(Level.FINE, msg);
    }
}
