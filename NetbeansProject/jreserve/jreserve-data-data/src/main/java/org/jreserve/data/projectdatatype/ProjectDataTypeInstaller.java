package org.jreserve.data.projectdatatype;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.jreserve.data.DataType;
import org.jreserve.data.DataTypeUtil;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.SessionTask;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ProjectSystemCreationListener;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=ProjectSystemCreationListener.class, position=100)
public class ProjectDataTypeInstaller implements SessionTask.Task<Void>, ProjectSystemCreationListener {
    
    private final static Logger logger = Logger.getLogger(ProjectDataTypeInstaller.class.getName());
    
    private volatile ProjectElement claimTypeElement;
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof ClaimType);
    }

    @Override
    public void created(ProjectElement element) {
        this.claimTypeElement = element;
        try {
            SessionTask.openCurrentSession(this).executeTasks();
        } catch(Exception ex) {
            logger.log(Level.SEVERE, "Unable to load data types for element: "+element, ex);
        } finally {
            this.claimTypeElement = null;
        }
    }

    @Override
    public void doWork(Session session) throws Exception {
        ClaimType claimType = getClaimType(session);
        saveValues(session, claimType);
    }
    
    private ClaimType getClaimType(Session session) {
        ClaimType claimType = (ClaimType) claimTypeElement.getValue();
        String id = claimType.getId();
        return (ClaimType) session.load(ClaimType.class, id);
    }
    
    private void saveValues(Session session, ClaimType claimType) {
        List<ProjectDataType> types = createDefaultValues(claimType);
        for(ProjectDataType type : types) {
            session.persist(type);
            claimTypeElement.addChild(new ProjectDatTypeProjectElement(type));
        }
    }
    
    private List<ProjectDataType> createDefaultValues(ClaimType claimType) {
        List<ProjectDataType> result = new ArrayList<ProjectDataType>();
        for(DataType dt : DataTypeUtil.getDataTypes())
            result.add(new ProjectDataType(claimType, dt));
        return result;
    }

    @Override
    public Void getResult() {
        return null;
    }
}
