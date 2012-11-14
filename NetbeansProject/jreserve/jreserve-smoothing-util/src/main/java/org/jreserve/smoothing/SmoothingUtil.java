package org.jreserve.smoothing;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.jreserve.persistence.PersistentObject;
import org.jreserve.persistence.SessionTask;
import org.jreserve.smoothing.core.Smoothing;
import org.jreserve.smoothing.util.SmoothingMethodRegistry;

/**
 *
 * @author Peter Decsi
 */
public class SmoothingUtil {

    private final static SmoothingMethodRegistry registry = new SmoothingMethodRegistry();
    
    public synchronized static List<SmoothingFactory> getFactories() {
        return new ArrayList<SmoothingFactory>(registry.getValues());
    }
    
    public synchronized static List<Smoothing> getSmoothings(PersistentObject owner) {
        try {
            SmoothingLoader loader = new SmoothingLoader(owner);
            return SessionTask.withOpenCurrentSession(loader);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static class SmoothingLoader extends SessionTask.AbstractTask<List<Smoothing>> {
        
        private PersistentObject owner;

        public SmoothingLoader(PersistentObject owner) {
            this.owner = owner;
        }
        
        @Override
        public void doWork(Session session) throws Exception {
            result = new ArrayList<Smoothing>();
            for(SmoothingFactory factory : registry.getValues())
                result.addAll(factory.getSmoothings(owner));
        }
    }
}
