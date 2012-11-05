package org.jreserve.smoothing.geometric;

import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.smoothing.Smoothing;
import org.jreserve.smoothing.SmoothingCell;
import org.jreserve.smoothing.SmoothingMethod;
import org.jreserve.triangle.widget.TriangleCell;
import org.jreserve.triangle.widget.TriangleWidget;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@SmoothingMethod.Registration(
    id=GeometricSmoothingMethod.ID,
    displayName="#LBL.GeometricSmoothingMethod.DisplayName"
)
@Messages({
    "LBL.GeometricSmoothingMethod.DisplayName=Geometric smoothing"
})
public class GeometricSmoothingMethod implements SmoothingMethod {
    
    private final static String SELECT_QUERY = 
        "SELECT s FROM GeometricSmoothing s WHERE s.ownerId = :ownerId";
    
    public final static int ID = 100;
    
    @Override
    public Smoothing createSmoothing(PersistentObject owner, TriangleWidget widget, TriangleCell[] cells) {
        String name = NameSelectPanel.getName(owner);
        if(name == null)
            return null;
        return createGeometricSmoothing(owner, name, cells);
    }
    
    private Smoothing createGeometricSmoothing(PersistentObject owner, String name, TriangleCell[] cells) {
        GeometricSmoothing smoothing = new GeometricSmoothing(owner, name);
        for(TriangleCell cell : cells)
            smoothing.addCell(createCell(smoothing, cell));
        return smoothing;
    }
    
    private SmoothingCell createCell(Smoothing smoothing, TriangleCell cell) {
        Date accident = cell.getAccidentBegin();
        Date development = cell.getDevelopmentBegin();
        return new SmoothingCell(smoothing, accident, development);
    }
    
    @Override
    public List<Smoothing> getSmoothings(PersistentObject owner) {
        Session session = SessionFactory.getCurrentSession();
        Query query = session.createQuery(SELECT_QUERY);
        query.setString("ownerId", owner.getId());
        return query.list();
    }
}
