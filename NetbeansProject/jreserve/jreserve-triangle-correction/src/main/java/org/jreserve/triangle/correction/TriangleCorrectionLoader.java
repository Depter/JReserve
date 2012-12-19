package org.jreserve.triangle.correction;

import java.util.Collections;
import java.util.List;
import org.hibernate.Query;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.project.system.ProjectElement;
import org.jreserve.project.system.ProjectElementFactory;
import org.jreserve.triangle.ModifiableTriangle;
import org.jreserve.triangle.correction.entities.TriangleCorrection;
import org.jreserve.triangle.entities.Triangle;

/**
 *
 * @author Peter Decsi
 */
@ProjectElementFactory.Registration()
public class TriangleCorrectionLoader implements ProjectElementFactory {

    private final static String HQL = "from TriangleCorrection c where c.ownerId = :ownerId";
    
    @Override
    public boolean isInterested(ProjectElement parent) {
        return parent != null &&
              (parent instanceof ModifiableTriangle);
    }

    @Override
    public List<ProjectElement> createChildren(ProjectElement parent) {
        ModifiableTriangle triangle = (ModifiableTriangle) parent;
        String ownerId = ((Triangle) parent.getValue()).getId();
        for(TriangleCorrection correction : getCorrections(ownerId))
            triangle.addModification(new TriangleCorrectionModification(correction));
        return Collections.EMPTY_LIST;
    }
    
    private List<TriangleCorrection> getCorrections(String ownerId) {
        Query query = SessionFactory.getCurrentSession().createQuery(HQL);
        query.setParameter("ownerId", ownerId);
        return query.list();
    }
}
