package org.jreserve.data.projectdatatype;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.PersistentDeletable;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 */
class ProjectDatTypeProjectElement extends ProjectElement<ProjectDataType> {
    
    private final static Logger logger = Logger.getLogger(ProjectDataTypeDeletable.class.getName());

    public final static int POSITION = 100;
    
    ProjectDatTypeProjectElement(ProjectDataType dataType) {
        super(dataType);
        super.addToLookup(new ProjectDataTypeDeletable());
    }

    @Override
    public Node createNodeDelegate() {
        return null;
    }

    @Override
    public boolean isVisible() {
        return false;
    }    
    
    @Override
    public int getPosition() {
        return POSITION;
    }
    
    private class ProjectDataTypeDeletable extends PersistentDeletable {
        
        private final static String SQL = 
            "delete from ClaimValue c where c.dataType.id= :dataTypeId";
        
        private final long id;
        
        private ProjectDataTypeDeletable() {
            super(ProjectDatTypeProjectElement.this);
            id = ProjectDatTypeProjectElement.this.getValue().getId();
        }

        @Override
        protected void cleanUpBeforeEntity(Session session) {
            Query query = session.createQuery(SQL);
            query.setParameter("dataTypeId", id);
            logDeletion();
            query.executeUpdate();
        }
    
        private void logDeletion() {
            ProjectDataType dt = ProjectDatTypeProjectElement.this.getValue();
            String msg = "Deleted claim data from '%s'/'%d - %s.";
            msg = String.format(msg, dt.getClaimType().getName(), dt.getDbId(), dt.getName());
            logger.log(Level.FINE, msg);
        }        
    }
}
