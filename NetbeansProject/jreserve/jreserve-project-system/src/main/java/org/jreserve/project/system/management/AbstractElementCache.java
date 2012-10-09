package org.jreserve.project.system.management;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;
import org.jreserve.persistence.SessionFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractElementCache<T, E, K> {

    protected final static Logger logger = Logger.getLogger(AbstractElementCache.class.getName());
    
    private Map<K, Set<E>> saveCache = new HashMap<K, Set<E>>();
    private Map<K, Set<E>> deleteCache = new HashMap<K, Set<E>>();
    
    public synchronized List<E> getValues(T key) {
        Set<E> cache = getCache(key);
        List<E> result = new ArrayList<E>(cache);
        Collections.sort(result, getComparator());
        return result;
    }
    
    protected Set<E> getCache(T key) {
        checkKey(key);
        Set<E> cache = saveCache.get(getId(key));
        return cache!=null? cache : initCache(key);
    }
    
    protected abstract void checkKey(T key);
    
    protected abstract K getId(T key);
    
    private Set<E> initCache(T key) {
        Session session = null;
        try {
            session = openSession(false);
            session.merge(key);
            Query query = createQuery(session, key);
            Set<E> cache = new HashSet<E>(query.getResultList());
            saveCache.put(getId(key), cache);
            return cache;
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, String.format("%s unable to load elements for '%s'!", getName(), keyToString(key)), ex);
            throw ex;
        } finally {
            session.close();
        }
    }
    
    protected Session openSession(boolean isTransaction) {
        if(isTransaction)
            return SessionFactory.beginTransaction();
        return SessionFactory.createSession();
    }
    
    protected abstract Query createQuery(Session session, T key);
    
    protected abstract String getName();
    
    protected abstract Comparator<E> getComparator();
    
    private String keyToString(T key) {
        return key==null? "null" : key.toString();
    }
    
    public synchronized void saveValues() {
        Session session = null;
        try {
            session = openSession(true);
            
            for(K keyId : getIds())
                updateDatabase(keyId, session);
            
            session.comitTransaction();
        } catch (RuntimeException ex) {
            session.rollBackTransaction();
            throw ex;
        }
    }
    
    protected Set<K> getIds() {
        Set<K> ids = new HashSet<K>(saveCache.keySet());
        ids.addAll(deleteCache.keySet());
        return ids;
    }
    
    protected void updateDatabase(K id, Session session) {
        deleteValues(id, session);
        saveValues(id, session);
    }
    
    protected void deleteValues(K id, Session session) {
        Set<E> cache = deleteCache.remove(id);
        if(cache != null && !cache.isEmpty())
            deleteValues(cache, session);
    }
    
    private void deleteValues(Set<E> cache, Session session) {
        for(E entity : cache)
            if(!isNew(entity))
                session.delete(entity);
    }
    
    protected void saveValues(K id, Session session) {
        Set<E> cache = saveCache.get(id);
        if(cache != null && !cache.isEmpty())
            saveValues(cache, session);
    }
    
    private void saveValues(Set<E> entities, Session session) {
        for(E entity : entities) {
            persists(session, entity);
            logger.log(Level.FINER, "{0} saving value: \"{1}\"", new Object[]{getName(), entryToString(entity)});
        }
    }
    
    protected void persists(Session session, E entity) {
        try {
            if(isNew(entity))
                session.persist(entity);
            else
                session.merge(entity);
        } catch (RuntimeException ex) {
            logger.log(Level.SEVERE, String.format("%s unable to save value: %s", getName(), entity.toString()), ex);
            throw ex;
        }
    }
    
    protected abstract boolean isNew(E entity);
    
    private String entryToString(E entity) {
        return entity==null? "null" : entity.toString();
    }
    
    public synchronized void saveValues(T key) {
        checkKey(key);
        Session session = null;
        try {
            session = openSession(true);
            session.merge(key);
            updateDatabase(getId(key), session);
            session.comitTransaction();
        } catch (RuntimeException ex) {
            session.rollBackTransaction();
            logger.log(Level.SEVERE, String.format("%s unable to save values for '%s'!", getName(), keyToString(key)), ex);
            throw ex;
        }
    }
    
    protected void addValue(T key, E entry) {
        checkKey(key);
        getCache(key).add(entry);
    }
    
    protected void deleteValue(T key, E entry) {
        checkKey(key);
        if(getCache(key).remove(entry))
            getDeleteCache(key).add(entry);
    }
    
    private Set<E> getDeleteCache(T key) {
        K id = getId(key);
        Set<E> cache = deleteCache.get(id);
        if(cache == null) {
            cache = new HashSet<E>();
            deleteCache.put(id, cache);
        }
        return cache;
    }
    
    public synchronized void clearCache() {
        saveCache.clear();
        deleteCache.clear();
    }
    
    public synchronized void clearCache(T key) {
        checkKey(key);
        K id = getId(key);
        saveCache.remove(id);
        deleteCache.remove(id);
    }
}
