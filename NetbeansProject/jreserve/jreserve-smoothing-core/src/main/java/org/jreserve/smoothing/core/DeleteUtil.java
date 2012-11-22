package org.jreserve.smoothing.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.jreserve.persistence.SessionTask;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DeleteUtil {

    private final static Logger logger = Logger.getLogger(DeleteUtil.class.getName());
    
    public static void delete(List<Smoothing> smoothings) {
        Deleter deleter = new Deleter(smoothings);
        RequestProcessor.getDefault().execute(deleter);
    }
    
    private static class Deleter extends SessionTask.AbstractTask<Void> implements Runnable {
        
        private final List<Smoothing> smoothings = new ArrayList<Smoothing>();
        
        private Deleter(List<Smoothing> smoothings) {
            this.smoothings.addAll(smoothings);
        }
        
        @Override
        public void run() {
            try {
                SessionTask.withOpenSession(this);
            } catch (Exception ex) {
            }
        }
        
        @Override
        public void doWork(Session session) throws Exception {
            for(Smoothing smoothing : smoothings)
                delete(smoothing, session);
        }
        
        private void delete(Smoothing smoothing, Session session) {
            try {
                Object persisted = session.get(Smoothing.class, smoothing.getId());
                if(persisted != null)
                    session.delete(persisted);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Unable to delete smoothing: "+smoothing.getName(), ex); 
            }
        }
    }
}
