package org.jreserve.data.container;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.AbstractProjectElementFactory;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.ProjectElementFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ProjectElementFactory.Registration(500)
public class ProjectDataContainerLoader extends AbstractProjectElementFactory<ProjectDataContainer> {

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof Project);
    }

    @Override
    protected List<ProjectDataContainer> getChildValues(Object value, Session session) {
        List<ProjectDataContainer> result = new ArrayList<ProjectDataContainer>(1);
        Project project = (Project) value;
        result.add(new ProjectDataContainer(project));
        return result;
    }
    
    @Override
    protected ProjectElement createProjectElement(ProjectDataContainer container) {
        ProjectElement element = new ProjectDataContainerElement(container);
        container.setMyElement(element);
        return element;
    }

}
