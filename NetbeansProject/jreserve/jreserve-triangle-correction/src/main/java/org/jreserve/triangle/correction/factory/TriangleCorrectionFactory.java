package org.jreserve.triangle.correction.factory;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.jreserve.persistence.SessionTask;
import org.jreserve.triangle.ModifiableTriangularData;
import org.jreserve.triangle.correction.TriangleCorrectionModification;
import org.jreserve.triangle.correction.entities.TriangleCorrection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TriangleCorrectionFactory extends SessionTask.AbstractTask<Void> {

    private final static Logger logger = Logger.getLogger(TriangleCorrectionFactory.class.getName());
    
    private final ModifiableTriangularData triangle;
    private final int accident;
    private final int development;
    private final double value;
    
    public TriangleCorrectionFactory(ModifiableTriangularData triangle, int accident, int development, double value) {
        this.triangle = triangle;
        this.accident = accident;
        this.development = development;
        this.value = value;
    }

    @Override
    public void doWork(Session session) throws Exception {
        TriangleCorrection correction = createCorrection();
        session.persist(correction);
        logger.log(Level.INFO, String.format("TriangleCorrection created [%d; %d] = %f", accident, development, value));
        triangle.addModification(new TriangleCorrectionModification(correction));
    }
    
    private TriangleCorrection createCorrection() {
        String ownerId = triangle.getOwner().getId();
        int order = triangle.getMaxModificationOrder() + 1;
        return new TriangleCorrection(ownerId, order, accident, development, value);
    }
}
