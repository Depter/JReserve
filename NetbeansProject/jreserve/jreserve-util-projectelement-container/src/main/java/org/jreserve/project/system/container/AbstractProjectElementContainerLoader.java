package org.jreserve.project.system.container;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.AbstractProjectElementFactory;
import org.jreserve.project.system.ProjectElement;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractProjectElementContainerLoader extends AbstractProjectElementFactory<ProjectElementContainer> {
    
    @Override
    public boolean isInterested(ProjectElement parent) {
        return parent != null &&
              (parent.getValue() instanceof Project);
    }

    @Override
    protected List<ProjectElementContainer> getChildValues(ProjectElement parent) {
        List<ProjectElementContainer> result = new ArrayList<ProjectElementContainer>(1);
        Project project = (Project) parent.getValue();
        result.add(getFactory().createContainer(project));
        return result;
    }
    
    @Override
    protected ProjectElement createProjectElement(ProjectElementContainer container) {
        return getFactory().createProjectElement(container);
    }
    
    protected abstract ProjectDataElementContainerFactory getFactory();
}
