package org.jreserve.triangle;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.hibernate.Query;
import org.jreserve.data.container.ProjectDataContainer;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.AbstractProjectElementFactory;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.ProjectElementFactory;
import org.jreserve.triangle.entities.Vector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ProjectElementFactory.Registration(200)
public class VectorLoader extends AbstractProjectElementFactory<Vector> {

    private final static String QUERY = 
          "SELECT v FROM Vector v WHERE v.project.id = :projectId";
    
    private final static Comparator<Vector> COMPARATOR = new Comparator<Vector>() {
        @Override
        public int compare(Vector o1, Vector o2) {
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
    protected List<Vector> getChildValues(Object value) {
        Project project = ((ProjectDataContainer) value).getProject();
        List<Vector> vectors = getVectors(project);
        Collections.sort(vectors, COMPARATOR);
        return vectors;
    }
    
    private List<Vector> getVectors(Project project) {
        Query query = SessionFactory.getCurrentSession().createQuery(QUERY);
        query.setParameter("projectId", project.getId());
        return query.list();
    }

    @Override
    protected ProjectElement createProjectElement(Vector value) {
        return new VectorProjectElement(value);
    }
}
