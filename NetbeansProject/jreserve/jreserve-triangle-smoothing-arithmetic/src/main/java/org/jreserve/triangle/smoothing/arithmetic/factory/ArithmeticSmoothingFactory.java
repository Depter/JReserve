package org.jreserve.triangle.smoothing.arithmetic.factory;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.persistence.SessionTask;
import org.jreserve.triangle.ModifiableTriangularData;
import org.jreserve.triangle.smoothing.SmoothingCell;
import org.jreserve.triangle.smoothing.TriangleSmoothing;
import org.jreserve.triangle.smoothing.arithmetic.ArithmeticSmoothing;

/**
 *
 * @author Peter Decsi
 */
public class ArithmeticSmoothingFactory extends SessionTask.AbstractTask<Void> {

    private final static Logger logger = Logger.getLogger(ArithmeticSmoothingFactory.class.getName());
    
    private final ModifiableTriangularData triangle;
    private final String name;
    private final int[][] cells;
    private final boolean[] applied;
    
    public ArithmeticSmoothingFactory(ModifiableTriangularData triangle, String name, int[][] cells, boolean[] applied) {
        this.triangle = triangle;
        this.name = name;
        this.cells = cells;
        this.applied = applied;
    }

    @Override
    public void doWork(Session session) throws Exception {
        ArithmeticSmoothing smoothing = createSmoothing();
        addCells(smoothing);
        session.persist(smoothing);
        logger.log(Level.INFO, String.format("ArithmeticSmoothing created: %s", name));
        triangle.addModification(new TriangleSmoothing(smoothing));
    }
    
    private ArithmeticSmoothing createSmoothing() {
        PersistentObject owner = triangle.getOwner();
        int order = triangle.getMaxModificationOrder() + 1;
        return new ArithmeticSmoothing(owner, order, name);
    }
    
    private void addCells(ArithmeticSmoothing smoothing) {
        for(int i=0, size=cells.length; i<size; i++)
            smoothing.addCell(new SmoothingCell(smoothing, cells[i][0], cells[i][1], applied[i]));
    }
}
