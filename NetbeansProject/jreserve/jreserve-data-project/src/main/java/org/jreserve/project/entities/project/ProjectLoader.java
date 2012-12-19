package org.jreserve.project.entities.project;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.AbstractProjectElementFactory;
import org.jreserve.project.system.ProjectElementFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ProjectElementFactory.Registration(ProjectElementFactory.MAX_PRIORITY)
public class ProjectLoader extends AbstractProjectElementFactory<Project> {
    
    private final static Comparator<Project> COMPARATOR = new Comparator<Project>() {
        @Override
        public int compare(Project p1, Project p2) {
            String n1 = p1.getName();
            String n2 = p2.getName();
            return n1.compareToIgnoreCase(n2);
        }
    };

    @Override
    public boolean isInterested(org.jreserve.project.system.ProjectElement parent) {
        return parent != null &&
              (parent.getValue() instanceof ClaimType);
    }

    @Override
    protected List<Project> getChildValues(org.jreserve.project.system.ProjectElement parent) {
        List<Project> projects = ((ClaimType)parent.getValue()).getProjects();
        Collections.sort(projects, COMPARATOR);
        return projects;
    }
    
    @Override
    protected org.jreserve.project.system.ProjectElement createProjectElement(Project project) {
        return new ProjectElement(project);
    }
}
