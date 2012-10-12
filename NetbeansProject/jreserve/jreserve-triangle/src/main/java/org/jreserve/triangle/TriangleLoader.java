package org.jreserve.triangle;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jreserve.data.container.ProjectDataContainer;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.AbstractProjectElementFactory;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.ProjectElementFactory;
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
        return (value instanceof ProjectDataContainer);
    }

    @Override
    protected List<Triangle> getChildValues(Object value, Session session) {
        Project project = ((ProjectDataContainer) value).getProject();
        List<Triangle> vectors = getTriangles(project, session);
        Collections.sort(vectors, COMPARATOR);
        return vectors;
    }
    
    private List<Triangle> getTriangles(Project project, Session session) {
        Query query = session.createQuery(QUERY);
        query.setParameter("projectId", project.getId());
        return query.getResultList();
    }

    @Override
    protected ProjectElement createProjectElement(Triangle value) {
        return new TriangleProjectElement(value);
    }
}
