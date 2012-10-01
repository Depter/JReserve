package org.jreserve.data.projectdatatype;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.data.DataType;
import org.jreserve.data.DataTypeUtil;
import org.jreserve.data.entities.ProjectDataType;
import org.jreserve.persistence.Session;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ProjectSystemCreationListener;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=ProjectSystemCreationListener.class, position=10)
public class ProjectDataTypeInstaller implements ProjectSystemCreationListener {
    
    private final static Logger logger = Logger.getLogger(ProjectDataTypeInstaller.class.getName());
    
    private Session session;
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Project);
    }

    @Override
    public void created(ProjectElement element) {
        try {
            session = SessionFactory.beginTransaction();
            Project project = getProject(element);
            saveValues(element, project);
            session.comitTransaction();
        } catch(RuntimeException ex) {
            session.rollBackTransaction();
            logger.log(Level.SEVERE, "Unable to load data types for element: "+element, ex);
        } finally {
            session = null;
        }
    }
    
    
    private Project getProject(ProjectElement element) {
        Project project = (Project) element.getValue();
        long id = project.getId();
        return session.find(Project.class, id);
    }
    
    private void saveValues(ProjectElement element, Project project) {
        List<ProjectDataType> types = createDefaultValues(project);
        for(ProjectDataType type : types) {
            session.persist(type);
            element.addChild(new ProjectDatTypeProjectElement(type));
        }
    }
    
    private List<ProjectDataType> createDefaultValues(Project project) {
        List<ProjectDataType> result = new ArrayList<ProjectDataType>();
        for(DataType dt : DataTypeUtil.getDataTypes())
            result.add(new ProjectDataType(project, dt));
        return result;
    }
}
