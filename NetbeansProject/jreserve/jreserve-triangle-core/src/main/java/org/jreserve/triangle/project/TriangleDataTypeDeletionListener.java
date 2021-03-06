package org.jreserve.triangle.project;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.data.ProjectDataType;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.container.ProjectElementContainer;
import org.jreserve.project.system.management.Deletable;
import org.jreserve.project.system.management.Deleter;
import org.jreserve.project.system.management.ProjectSystemDeletionListener;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.util.ProjectDataContainerFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=ProjectSystemDeletionListener.class)
public class TriangleDataTypeDeletionListener implements ProjectSystemDeletionListener {

    @Override
    public boolean isInterested(Object value) {
        return (value instanceof ProjectDataType);
    }

    @Override
    public void deleted(ProjectElement parent, ProjectElement child) {
        ProjectDataType dt = (ProjectDataType) child.getValue();
        List<Deletable> deletables = getDeletables(parent, dt);
        Deleter deleter = new Deleter(deletables);
        deleter.delete();        
    }
    
    private List<Deletable> getDeletables(ProjectElement parent, ProjectDataType dt) {
        List<Deletable> deletables = new ArrayList<Deletable>();
        List<ProjectElement> children = parent.getChildren(Project.class);
        for(ProjectElement element : children)
            addDeletables(deletables, element, dt);
        return deletables;
    }

    private void addDeletables(List<Deletable> deletables, ProjectElement project, ProjectDataType dt) {
        int position = ProjectDataContainerFactory.POSITION;
        ProjectElementContainer container = (ProjectElementContainer) project.getFirstChildValue(position, ProjectElementContainer.class);
        deletables.addAll(getDeletables(container, dt));
    }
    
    private List<Deletable> getDeletables(ProjectElementContainer container, ProjectDataType dt) {
        List<Deletable> deletables = new ArrayList<Deletable>();
        for(ProjectElement element : getProjectElements(container, dt))
            addDeletableIfExists(deletables, element);
        return deletables;
    }

    private List<ProjectElement> getProjectElements(ProjectElementContainer container, ProjectDataType dt) {
        List<ProjectElement> result = new ArrayList<ProjectElement>();
        for(ProjectElement<Triangle> element : container.getProjectElements(Triangle.class))
            if(dt.equals(element.getValue().getDataType()))
                result.add(element);
        return result;
    }
    
    private void addDeletableIfExists(List<Deletable> deletables, ProjectElement element) {
        Deletable d = element.getLookup().lookup(Deletable.class);
        if(d != null)
            deletables.add(d);
    }
}
