package org.jreserve.project.system.container;

import java.awt.Image;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class ProjectDataElementContainerFactory {
    
    public ProjectElementContainer createContainer(Project project) {
        return new ProjectElementContainer(project, getPosition());
    }
    
    public ProjectElement createProjectElement(Project project) {
        ProjectElementContainer container = createContainer(project);
        return createProjectElement(container);
    }
    
    public ProjectElement createProjectElement(ProjectElementContainer container) {
        ProjectElementContainerElement element = new ProjectElementContainerElement(container, getName());
        element.setNodeImage(getImage());
        element.addActionPathes(getActionPathes());
        container.setMyElement(element);
        return element;
    }
    
    protected abstract String getName();
    
    protected abstract int getPosition();
    
    protected abstract Image getImage();
    
    protected abstract String[] getActionPathes();
}
