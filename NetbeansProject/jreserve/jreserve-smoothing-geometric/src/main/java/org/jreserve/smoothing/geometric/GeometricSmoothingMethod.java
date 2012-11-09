package org.jreserve.smoothing.geometric;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.smoothing.core.SmoothingCell;
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
        double[] input = getInput(cells);
        NameSelectPanel panel = NameSelectPanel.createPanel(owner, input);
        if(panel.isCancelled())
            return null;
        return createGeometricSmoothing(owner, panel.getName(), cells, panel.getApplied());
    }
    
    private double[] getInput(TriangleCell[] cells) {
        Arrays.sort(cells);
        int size = cells.length;
        double[] input = new double[size];
        for(int i=0; i<size; i++)
            input[i] = cells[i].getDisplayValue();
        return input;
    }
    
    private Smoothing createGeometricSmoothing(PersistentObject owner, String name, TriangleCell[] cells, boolean[] applied) {
        GeometricSmoothing smoothing = new GeometricSmoothing(owner, name);
        for(int i=0, size=cells.length; i<size; i++)
            smoothing.addCell(createCell(smoothing, cells[i], applied[i]));
        return smoothing;
    }
    
    private SmoothingCell createCell(Smoothing smoothing, TriangleCell cell, boolean applied) {
        Date accident = cell.getAccidentBegin();
        Date development = cell.getDevelopmentBegin();
        return new SmoothingCell(smoothing, accident, development, applied);
    }
    
    @Override
    public List<Smoothing> getSmoothings(PersistentObject owner) {
        Session session = SessionFactory.getCurrentSession();
        Query query = session.createQuery(SELECT_QUERY);
        query.setString("ownerId", owner.getId());
        return query.list();
    }
}
