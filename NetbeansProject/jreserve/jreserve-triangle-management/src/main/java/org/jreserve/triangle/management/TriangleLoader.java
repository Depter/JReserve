package org.jreserve.triangle.management;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.hibernate.Query;
import org.jreserve.data.container.ProjectDataContainerFactoy;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.AbstractProjectElementFactory;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.ProjectElementFactory;
import org.jreserve.project.system.container.ProjectElementContainer;
import org.jreserve.triangle.entities.Triangle;

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
    public boolean isInterested(Object value) {
        if(value instanceof ProjectElementContainer)
            return ((ProjectElementContainer)value).getPosition() == ProjectDataContainerFactoy.POSITION;
        return false;
    }

    @Override
    protected List<Triangle> getChildValues(Object value) {
        Project project = ((ProjectElementContainer) value).getProject();
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
