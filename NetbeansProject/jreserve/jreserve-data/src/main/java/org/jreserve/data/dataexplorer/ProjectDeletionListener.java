package org.jreserve.data.dataexplorer;

import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ProjectSystemDeletionListener;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 */
@ServiceProvider(service=ProjectSystemDeletionListener.class)
public class ProjectDeletionListener implements ProjectSystemDeletionListener {

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Project);
    }

    @Override
    public void deleted(ProjectElement parent, ProjectElement child) {
        Project project = (Project) child.getValue();
        DataExplorerRegistry.closeIfExists(project);
    }
}
