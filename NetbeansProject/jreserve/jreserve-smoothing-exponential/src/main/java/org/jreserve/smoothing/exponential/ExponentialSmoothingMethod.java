package org.jreserve.smoothing.exponential;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.smoothing.Smoothing;
import org.jreserve.smoothing.SmoothingMethod;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@SmoothingMethod.Registration(
    id=ExponentialSmoothingMethod.ID,
    displayName="#LBL.GeometricSmoothingMethod.DisplayName"
)
@Messages({
    "LBL.ExponentialSmoothingMethod.DisplayName=Exponential smoothing"
})
public class ExponentialSmoothingMethod implements SmoothingMethod {
    
    private final static String SELECT_QUERY = 
        "SELECT s FROM ExponentialSmoothing s WHERE s.ownerId = :ownerId";
    
    public final static int ID = 200;

    @Override
    public Smoothing createSmoothing(PersistentObject owner, TriangleWidget widget, TriangleCell[] cells) {
        return null;
    }

    @Override
    public List<Smoothing> getSmoothings(PersistentObject owner) {
        Session session = SessionFactory.getCurrentSession();
        Query query = session.createQuery(SELECT_QUERY);
        query.setString("ownerId", owner.getId());
        return query.list();
    }
}
