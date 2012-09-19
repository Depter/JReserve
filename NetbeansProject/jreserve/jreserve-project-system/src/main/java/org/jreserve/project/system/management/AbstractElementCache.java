package org.jreserve.project.system.management;

import java.util.*;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.persistence.PersistenceUnit;
import org.jreserve.persistence.PersistenceUtil;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractElementCache<T, E> {

    protected final static Logger logger = Logging.getLogger(AbstractElementCache.class.getName());
    
    private Map<Long, Set<E>> saveCache = new HashMap<Long, Set<E>>();
    
    protected Set<E> getCache(T key) {
        long id = getId(key);
        return getCache(id);
    }
    
    protected Set<E> getCache(long id) {
        Set<E> cache = saveCache.get(id);
        if(cache == null) {
            cache = new HashSet<E>();
            saveCache.put(id, cache);
        }
        return cache;
    }
    
    public synchronized void saveValues() {
        Session session = null;
        try {
            session = openSession(true);
            
            for(Long keyId : saveCache.keySet())
                saveLogs(keyId, session);
            
            session.comitTransaction();
        } catch (RuntimeException ex) {
            session.rollBackTransaction();
            logger.error(ex, "%s unable to save values!", getName());
            throw ex;
        }
    }
    
    public synchronized void saveValues(T key) {
        checkKey(key);
        Session session = null;
        try {
            session = openSession(true);
            session.update(key);
            saveLogs(getId(key), session);
            session.comitTransaction();
        } catch (RuntimeException ex) {
            session.rollBackTransaction();
            logger.error(ex, "%s unable to save values for '%s'!", getName(), keyToString(key));
            throw ex;
        }
    }
    
    protected abstract String getName();
    
    protected Session openSession(boolean isTransaction) {
        PersistenceUnit pu = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
        Session session = pu.getSession();
        if(isTransaction)
            session.beginTransaction();
        return session;
    }
    
    protected void saveLogs(long projectId, Session session) {
        Set<E> cache = saveCache.get(projectId);
        if(cache != null && !cache.isEmpty())
            saveLogs(cache, session);
        saveCache.remove(projectId);
    }
    
    private void saveLogs(Set<E> entities, Session session) {
        for(E entity : entities) {
            session.persist(entity);
            logger.trace("%s saving value: %s", getName(), entryToString(entity));
        }
    }
    
    protected String entryToString(E entity) {
        return entity==null? "null" : entity.toString();
    }
    
    public synchronized List<E> getValues(T key) {
        checkKey(key);
        List<E> result = queryChanges(key);
        result.addAll(getCachedChanges(key));
        Collections.sort(result, getComparator());
        return result;
    }
    
    protected abstract void checkKey(T key);
    
    private List<E> queryChanges(T key) {
        Session session = null;
        try {
            session = openSession(false);
            session.update(key);
            Query query = createQuery(session, key);
            return query.getResultList();
        } catch (RuntimeException ex) {
            logger.error(ex, "%s unable to load elements for '%s'!", getName(), keyToString(key));
            throw ex;
        } finally {
            session.close();
        }
    }
    
    protected abstract Query createQuery(Session session, T key);
    
    protected String keyToString(T key) {
        return key==null? "null" : key.toString();
    }
    
    protected abstract Comparator<E> getComparator();
    
    protected List<E> getCachedChanges(T key) {
        long id = getId(key);
        return getCachedChanges(id);
    }
    
    protected abstract long getId(T key);
    
    private List<E> getCachedChanges(long id) {
        Set<E> cache = saveCache.get(id);
        if(cache == null || cache.isEmpty())
            return new ArrayList<E>();
        return new ArrayList<E>(cache);
    }
    
    protected void addValue(T key, E entry) {
        checkKey(key);
        getCache(key).add(entry);
    }
    
    protected void deleteValue(T key, E entry) {
        checkKey(key);
        getCache(key).remove(entry);
    }
}
