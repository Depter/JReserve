package org.jreserve.data.projectdatatype;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.data.DataType;
import org.jreserve.data.DataTypeUtil;
import org.jreserve.data.entities.ProjectDataType;
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

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Project);
    }

    @Override
    public void created(ProjectElement element) {
        Project project = (Project) element.getValue();
        List<ProjectDataType> types = createDefaultValues(project);
        saveValues(project, types);
        createProjectElements(element, types);
    }
    
    private List<ProjectDataType> createDefaultValues(Project project) {
        List<ProjectDataType> result = new ArrayList<ProjectDataType>();
        for(DataType dt : DataTypeUtil.getDataTypes())
            result.add(new ProjectDataType(project, dt));
        return result;
    }
    
    private void saveValues(Project project, List<ProjectDataType> types) {
        ProjectDataTypeUtil util = ProjectDataTypeUtil.getDefault();
        for(ProjectDataType type : types)
            util.addValue(project, type);
        util.saveValues(project);
    }
    
    private void createProjectElements(ProjectElement element, List<ProjectDataType> types) {
        for(ProjectDataType type : types)
            element.addChild(new ProjectDatTypeProjectElement(type));
    }
}
