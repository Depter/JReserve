package org.jreserve.estimates.chainladder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.hibernate.Query;
import org.jreserve.estimates.EstimateContainerFactory;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.entities.Project;
import org.jreserve.project.system.AbstractProjectElementFactory;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.ProjectElementFactory;
import org.jreserve.project.system.container.ProjectElementContainer;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ProjectElementFactory.Registration()
public class ChainLadderEstimateLoader extends AbstractProjectElementFactory<ChainLadderEstimate> {

    private final static String QUERY = 
          "SELECT cl FROM ChainLadderEstimate cl WHERE cl.project.id = :projectId";
    
    private final static Comparator<ChainLadderEstimate> COMPARATOR = new Comparator<ChainLadderEstimate>() {
        @Override
        public int compare(ChainLadderEstimate o1, ChainLadderEstimate o2) {
            String n1 = o1.getName();
            String n2 = o2.getName();
            return n1.compareToIgnoreCase(n2);
        }
    };
    
    @Override
    public boolean isInterested(Object value) {
        if(value instanceof ProjectElementContainer)
            return ((ProjectElementContainer)value).getPosition() == EstimateContainerFactory.POSITION;
        return false;
    }

    @Override
    protected List<ChainLadderEstimate> getChildValues(Object value) {
        Project project = ((ProjectElementContainer) value).getProject();
        List<ChainLadderEstimate> triangles = getEstimates(project);
        Collections.sort(triangles, COMPARATOR);
        return triangles;
    }
    
    private List<ChainLadderEstimate> getEstimates(Project project) {
        Query query = SessionFactory.getCurrentSession().createQuery(QUERY);
        query.setParameter("projectId", project.getId());
        return query.list();
    }

    @Override
    protected ProjectElement createProjectElement(ChainLadderEstimate value) {
        return new ChainLadderEstimateProjectElement(value);
    }
}
