package org.jreserve.smoothing.exponential;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.smoothing.SmoothingMethod;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.smoothing.core.SmoothingCell;
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
    displayName="#LBL.ExponentialSmoothingMethod.DisplayName"
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
        double[] input = getInput(cells);
        CreatorPanel panel = CreatorPanel.create(owner, input, widget.getVisibleDigits());
        ExponentialSmoothing smoothing = panel.isCancelled()? null : new ExponentialSmoothing(owner, panel.getSmoothingName(), panel.getAlpha());
        fillSmoothing(smoothing, cells, panel.getApplied());
        return smoothing;
    }
    
    private double[] getInput(TriangleCell[] cells) {
        Arrays.sort(cells);
        int size = cells.length;
        double[] input = new double[size];
        for(int i=0; i<size; i++)
            input[i] = cells[i].getDisplayValue();
        return input;
    }
    
    private void fillSmoothing(ExponentialSmoothing smoothing, TriangleCell[] cells, boolean applied[]) {
        if(smoothing == null) return;
        for(int i=0, size=cells.length; i<size; i++)
            smoothing.addCell(createCell(smoothing, cells[i], applied[i]));
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
