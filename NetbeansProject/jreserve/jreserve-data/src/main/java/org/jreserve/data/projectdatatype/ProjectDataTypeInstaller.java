package org.jreserve.data.projectdatatype;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.data.DataType;
import org.jreserve.data.DataTypeUtil;
import org.jreserve.data.ProjectDataType;
import org.jreserve.persistence.Session;
import org.jreserve.persistence.SessionFactory;
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
public class ProjectDataTypeInstaller implements ProjectSystemCreationListener {
    
    private final static Logger logger = Logger.getLogger(ProjectDataTypeInstaller.class.getName());
    
    private Session session;
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof ClaimType);
    }

    @Override
    public void created(ProjectElement element) {
        try {
            session = SessionFactory.beginTransaction();
            ClaimType claimType = getClaimType(element);
            saveValues(element, claimType);
            session.comitTransaction();
        } catch(RuntimeException ex) {
            session.rollBackTransaction();
            logger.log(Level.SEVERE, "Unable to load data types for element: "+element, ex);
        } finally {
            session = null;
        }
    }
    
    
    private ClaimType getClaimType(ProjectElement element) {
        ClaimType claimType = (ClaimType) element.getValue();
        long id = claimType.getId();
        return session.find(ClaimType.class, id);
    }
    
    private void saveValues(ProjectElement element, ClaimType claimType) {
        List<ProjectDataType> types = createDefaultValues(claimType);
        for(ProjectDataType type : types) {
            session.persist(type);
            element.addChild(new ProjectDatTypeProjectElement(type));
        }
    }
    
    private List<ProjectDataType> createDefaultValues(ClaimType claimType) {
        List<ProjectDataType> result = new ArrayList<ProjectDataType>();
        for(DataType dt : DataTypeUtil.getDataTypes())
            result.add(new ProjectDataType(claimType, dt));
        return result;
    }
}
