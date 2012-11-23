package org.jreserve.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DeleteUtil extends SessionTask.AbstractTask<Void> implements Runnable {
    
    private final static Logger logger = Logger.getLogger(DeleteUtil.class.getName());
    
    private Map<Class, List<PersistentObject>> entities = new HashMap<Class, List<PersistentObject>>();
    
    public <T extends PersistentObject> void addEntities(Class<T> clazz, List<T> entities) {
        List<PersistentObject> list = this.entities.get(clazz);
        if(list == null) {
            list = new ArrayList<PersistentObject>();
            this.entities.put(clazz, list);
        }
        list.addAll(entities);
    }
    
    public void delete() {
        RequestProcessor.getDefault().execute(this);
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
        for(Class clazz : entities.keySet()) {
            for(PersistentObject entity : entities.get(clazz))
                delete(clazz, entity, session);
        }
    }
        
    public void delete(Session session) {
        for(Class clazz : entities.keySet())
            for(PersistentObject entity : entities.get(clazz))
                delete(clazz, entity, session);
    }
    
    private void delete(Class clazz, PersistentObject entity, Session session) {
        try {
            logger.log(Level.FINER, "Deleting entity: {0}", entity);
            Object persisted = session.get(clazz, entity.getId());
            if(persisted != null)
                session.delete(persisted);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to delete smoothing: " + entity, ex);
        }
    }
}
