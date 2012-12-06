package org.jreserve.project.system.container;

import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.management.ProjectSystemCreationListener;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractProjectElementContainerInstaller implements ProjectSystemCreationListener {
    
    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Project);
    }

    @Override
    public void created(ProjectElement element) {
        Project project = (Project) element.getValue();
        ProjectElement child = getFactory().createProjectElement(project);
        element.addChild(child);
    }
    
    protected abstract ProjectDataElementContainerFactory getFactory();
}
