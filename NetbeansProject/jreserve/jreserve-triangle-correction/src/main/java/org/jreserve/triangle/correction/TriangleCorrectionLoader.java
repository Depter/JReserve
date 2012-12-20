package org.jreserve.triangle.correction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.triangle.ModificationLoader;
import org.jreserve.triangle.ModifiedTriangularData;
import org.jreserve.triangle.correction.entities.TriangleCorrection;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 */
@ServiceProvider(service=ModificationLoader.class)
public class TriangleCorrectionLoader implements ModificationLoader {

    private final static Logger logger = Logger.getLogger(TriangleCorrectionLoader.class.getName());
    private final static String HQL = "from TriangleCorrection c where c.ownerId = :ownerId";

    @Override
    public List<ModifiedTriangularData> loadModifications(PersistentObject owner) {
        List<ModifiedTriangularData> mods = new ArrayList<ModifiedTriangularData>();
        for(TriangleCorrection correction : loadCorrections(owner))
            mods.add(new TriangleCorrectionModification(correction));
        return mods;
    }
    
    private List<TriangleCorrection> loadCorrections(PersistentObject owner) {
        logger.log(Level.FINE, "Loading TriangleCorrections for: {0}", owner);
        Query query = SessionFactory.getCurrentSession().createQuery(HQL);
        query.setParameter("ownerId", owner.getId());
        return query.list();
    }
}
