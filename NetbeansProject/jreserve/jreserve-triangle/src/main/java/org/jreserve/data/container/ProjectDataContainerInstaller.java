package org.jreserve.data.container;

import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ProjectSystemCreationListener;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 */
@ServiceProvider(service=ProjectSystemCreationListener.class, position=100)
public class ProjectDataContainerInstaller implements ProjectSystemCreationListener {

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Project);
    }

    @Override
    public void created(ProjectElement element) {
        Project project = (Project) element.getValue();
        ProjectDataContainer container = new ProjectDataContainer(project);
        ProjectDataContainerElement child = new ProjectDataContainerElement(container);
        element.addChild(child);
    }
}
