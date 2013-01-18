package org.jreserve.triangle.project;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.hibernate.Query;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.AbstractProjectElementFactory;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.ProjectElementFactory;
import org.jreserve.project.system.container.ProjectElementContainer;
import org.jreserve.triangle.entities.Triangle;
import org.jreserve.triangle.util.ProjectDataContainerFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ProjectElementFactory.Registration(100)
public class TriangleLoader extends AbstractProjectElementFactory<Triangle> {

    private final static String QUERY = 
          "SELECT t FROM Triangle t WHERE t.project.id = :projectId";
    
    private final static Comparator<Triangle> COMPARATOR = new Comparator<Triangle>() {
        @Override
        public int compare(Triangle o1, Triangle o2) {
            String n1 = o1.getName();
            String n2 = o2.getName();
            return n1.compareToIgnoreCase(n2);
        }
    };
    
    @Override
    public boolean isInterested(ProjectElement parent) {
        if(parent == null)
            return false;
        return isInterested(parent.getValue());
    }
    
    private boolean isInterested(Object value) {
        if(value instanceof ProjectElementContainer)
            return ((ProjectElementContainer)value).getPosition() == ProjectDataContainerFactory.POSITION;
        return false;
    }

    @Override
    protected List<Triangle> getChildValues(ProjectElement parent) {
        Project project = ((ProjectElementContainer) parent.getValue()).getProject();
        List<Triangle> triangles = getTriangles(project);
        Collections.sort(triangles, COMPARATOR);
        return triangles;
    }
    
    private List<Triangle> getTriangles(Project project) {
        Query query = SessionFactory.getCurrentSession().createQuery(QUERY);
        query.setParameter("projectId", project.getId());
        return query.list();
    }

    @Override
    protected ProjectElement createProjectElement(Triangle value) {
        return new TriangleProjectElement(value);
    }
}
