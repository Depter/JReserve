package org.jreserve.triangle.smoothing.exponential;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.persistence.SessionFactory;
import org.jreserve.triangle.value.ModificationLoader;
import org.jreserve.triangle.smoothing.AbstractSmoothingLoader;
import org.jreserve.triangle.smoothing.Smoothing;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=ModificationLoader.class)
public class ExponentialSmoothingLoader extends AbstractSmoothingLoader {
    
    private final static Logger logger = Logger.getLogger(ExponentialSmoothingLoader.class.getName());
    private final static String HQL = "from ExponentialSmoothing s where s.ownerId = :ownerId";

    @Override
    protected List<Smoothing> loadSmoothings(PersistentObject owner) {
        logger.log(Level.FINE, "Loading ExponentialSmoothings for: {0}", owner);
        Query query = SessionFactory.getCurrentSession().createQuery(HQL);
        query.setParameter("ownerId", owner.getId());
        return query.list();
    }
}
