package org.jreserve.data.projectdatatype;

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
@ServiceProvider(service=ProjectDataTypeInstaller.class, position=10)
public class ProjectDataTypeInstaller implements ProjectSystemCreationListener {

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Project);
    }

    @Override
    public void created(ProjectElement element) {
        Project project = (Project) element.getValue();
        createDefaultTypes(project);
    }
    
    private void createDefaultTypes(Project project) {
        ProjectDataTypeUtil util = ProjectDataTypeUtil.getDefault();
        for(DataType dt : DataTypeUtil.getDataTypes())
            util.addValue(project, new ProjectDataType(project, dt));
        util.saveValues(project);
    }
}
