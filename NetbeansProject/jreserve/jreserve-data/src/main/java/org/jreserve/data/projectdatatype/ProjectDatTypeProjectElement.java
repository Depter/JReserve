package org.jreserve.data.projectdatatype;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.data.DataCriteria;
import org.jreserve.data.DataSource;
import org.jreserve.data.ProjectDataType;
import org.jreserve.data.query.DataLogUtil;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentObjectDeletable;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 */
public class ProjectDatTypeProjectElement extends ProjectElement<ProjectDataType> {
    
    private final static Logger logger = Logger.getLogger(ProjectDataTypeDeletable.class.getName());

    public final static int POSITION = 100;
    
    public ProjectDatTypeProjectElement(ProjectDataType dataType) {
        super(dataType);
        super.addToLookup(new ProjectDataTypeDeletable());
    }

    @Override
    public Node createNodeDelegate() {
        return new ProjectDataTypeNode(this);
    }

    @Override
    public boolean isVisible() {
        return false;
    }    
    
    @Override
    public int getPosition() {
        return POSITION;
    }
    
    private class ProjectDataTypeDeletable extends PersistentObjectDeletable<ProjectDataType> {
        private final static String DELETE_LOG = 
            "delete from DataLog l where l.dataType.id= :dataTypeId";
        
        private final String id;
        
        private ProjectDataTypeDeletable() {
            super(ProjectDatTypeProjectElement.this, "ProjectDataType");
            id = ProjectDatTypeProjectElement.this.getValue().getId();
        }

        @Override
        protected void cleanUpBeforeEntity(Session session) {
            DataLogUtil.logDeletion(session, ProjectDatTypeProjectElement.this.getValue());
            deleteData(session);
            logDeletion();
        }
        
        private void deleteData(Session session) {
            deleteDataLog(session);
            DataSource ds = new DataSource(session);
            DataCriteria criteria = new DataCriteria(ProjectDatTypeProjectElement.this.getValue());
            clearAllData(ds, criteria);
        }
        
        private void deleteDataLog(Session session) {
            Query query = session.createQuery(DELETE_LOG);
            query.setParameter("dataTypeId", id);
            query.executeUpdate();
        }
        
        private void clearAllData(DataSource ds, DataCriteria criteria) {
            ds.clearData(criteria);
        }
    
        private void logDeletion() {
            ProjectDataType dt = ProjectDatTypeProjectElement.this.getValue();
            String msg = "Deleted claim data/data log from '%s'/'%d - %s.";
            msg = String.format(msg, dt.getClaimType().getName(), dt.getDbId(), dt.getName());
            logger.log(Level.FINE, msg);
        }
    }
}
