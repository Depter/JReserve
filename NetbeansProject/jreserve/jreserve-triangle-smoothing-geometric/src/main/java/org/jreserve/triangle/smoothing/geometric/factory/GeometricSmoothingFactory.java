package org.jreserve.triangle.smoothing.geometric.factory;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.persistence.SessionTask;
import org.jreserve.triangle.ModifiableTriangularData;
import org.jreserve.triangle.smoothing.SmoothingCell;
import org.jreserve.triangle.smoothing.TriangleSmoothing;
import org.jreserve.triangle.smoothing.geometric.GeometricSmoothing;

/**
 *
 * @author Peter Decsi
 */
public class GeometricSmoothingFactory extends SessionTask.AbstractTask<Void> {

    private final static Logger logger = Logger.getLogger(GeometricSmoothingFactory.class.getName());
    
    private final ModifiableTriangularData triangle;
    private final String name;
    private final int[][] cells;
    private final boolean[] applied;
    
    public GeometricSmoothingFactory(ModifiableTriangularData triangle, String name, int[][] cells, boolean[] applied) {
        this.triangle = triangle;
        this.name = name;
        this.cells = cells;
        this.applied = applied;
    }

    @Override
    public void doWork(Session session) throws Exception {
        GeometricSmoothing smoothing = createSmoothing();
        addCells(smoothing);
        session.persist(smoothing);
        logger.log(Level.INFO, String.format("GeometricSmoothing created: %s", name));
        triangle.addModification(new TriangleSmoothing(smoothing));
    }
    
    private GeometricSmoothing createSmoothing() {
        PersistentObject owner = triangle.getOwner();
        int order = triangle.getMaxModificationOrder() + 1;
        return new GeometricSmoothing(owner, order, name);
    }
    
    private void addCells(GeometricSmoothing smoothing) {
        for(int i=0, size=cells.length; i<size; i++)
            smoothing.addCell(new SmoothingCell(smoothing, cells[i][0], cells[i][1], applied[i]));
    }
}
