package org.jreserve.triangle.smoothing.exponential.factory;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.persistence.SessionTask;
import org.jreserve.triangle.ModifiableTriangularData;
import org.jreserve.triangle.smoothing.SmoothingCell;
import org.jreserve.triangle.smoothing.TriangleSmoothing;
import org.jreserve.triangle.smoothing.exponential.ExponentialSmoothing;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ExponentialSmoothingFactory extends SessionTask.AbstractTask<Void> {

    private final static Logger logger = Logger.getLogger(ExponentialSmoothingFactory.class.getName());
    
    private final ModifiableTriangularData triangle;
    private final String name;
    private final int[][] cells;
    private final boolean[] applied;
    private final double alpha;
    
    public ExponentialSmoothingFactory(ModifiableTriangularData triangle, String name, int[][] cells, boolean[] applied, double alpha) {
        this.triangle = triangle;
        this.name = name;
        this.cells = cells;
        this.applied = applied;
        this.alpha = alpha;
    }

    @Override
    public void doWork(Session session) throws Exception {
        ExponentialSmoothing smoothing = createSmoothing();
        addCells(smoothing);
        session.persist(smoothing);
        logger.log(Level.INFO, String.format("ExponentialSmoothing created: %s", name));
        triangle.addModification(new TriangleSmoothing(smoothing));
    }
    
    private ExponentialSmoothing createSmoothing() {
        PersistentObject owner = triangle.getOwner();
        int order = triangle.getMaxModificationOrder() + 1;
        return new ExponentialSmoothing(owner, order, name, alpha);
    }
    
    private void addCells(ExponentialSmoothing smoothing) {
        for(int i=0, size=cells.length; i<size; i++)
            smoothing.addCell(new SmoothingCell(smoothing, cells[i][0], cells[i][1], applied[i]));
    }
}
