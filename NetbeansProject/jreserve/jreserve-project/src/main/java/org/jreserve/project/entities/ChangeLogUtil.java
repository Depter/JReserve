package org.jreserve.project.entities;

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
public class ChangeLogUtil {

    private final static Logger logger = Logging.getLogger(ChangeLogUtil.class.getName());
    
    private final static String ERR_PROJECT_NOT_PERSISTED = 
        "Project '%s' must be peristed first, before log can be added to it!";
    private final static String QUERY = 
        "SELECT c FROM ChangeLog c WHERE c.project.id = :projectId";
    
    private final static Comparator<ChangeLog> COMPARATOR = new Comparator<ChangeLog>() {
        @Override
        public int compare(ChangeLog o1, ChangeLog o2) {
            Date d1 = o1.getCreationTime();
            Date d2 = o2.getCreationTime();
            return compare(d1, d2);
        }
        
        private int compare(Date d1, Date d2) {
            if(d1.before(d2))
                return -1;
            return d1.after(d2)? 1 : 0;
        }
    };
    
    private static ChangeLogUtil DEFAULT = null;
    
    public static synchronized ChangeLogUtil getDefault() {
        if(DEFAULT == null)
            DEFAULT = new ChangeLogUtil();
        return DEFAULT;
    }
    
    private Map<Long, Set<ChangeLog>> saveCache = new HashMap<Long, Set<ChangeLog>>();
    private Map<Long, Set<ChangeLogListener>> listeners = new HashMap<Long, Set<ChangeLogListener>>();
    
    private ChangeLogUtil() {
    }
    
    public synchronized void addChangeLogListener(Project project, ChangeLogListener listener) {
        checkProject(project);
        Set<ChangeLogListener> pl = getListeners(project);
        pl.add(listener);
    }
    
    private Set<ChangeLogListener> getListeners(Project project) {
        Set<ChangeLogListener> pl = listeners.get(project.getId());
        if(pl == null) {
            pl = new HashSet<ChangeLogListener>();
            listeners.put(project.getId(), pl);
        }
        return pl;
    }
    
    public synchronized void removeChangeLogListener(Project project, ChangeLogListener listener) {
        Set<ChangeLogListener> pl = listeners.get(project.getId());
        if(pl != null) {
            pl.remove(listener);
            if(pl.isEmpty())
                listeners.remove(project.getId());
        }
    }
    
    private void checkProject(Project project) {
        if(project.getId() > 0)
            return;
        String msg = String.format(ERR_PROJECT_NOT_PERSISTED, project.getName());
        throw new IllegalArgumentException(msg);
    }
    
    public synchronized void addChange(Project project, ChangeLog.Type type, String log) {
        checkProject(project);
        ChangeLog change = new ChangeLog(project, type, log);
        getProjectCache(project).add(change);
        fireAddChange(project, change);
    }
    
    private Set<ChangeLog> getProjectCache(Project project) {
        Set<ChangeLog> cache = saveCache.get(project.getId());
        if(cache == null) {
            cache = new HashSet<ChangeLog>();
            saveCache.put(project.getId(), cache);
        }
        return cache;
    }
    
    private void fireAddChange(Project project, ChangeLog log) {
        Set<ChangeLogListener> pl = listeners.get(project.getId());
        if(pl == null)
            return;
        for(ChangeLogListener l : new ArrayList<ChangeLogListener>(pl))
            l.changeLogAdded(log);
    }
    
    public synchronized void saveLogs() {
        Session session = null;
        try {
            session = openSession(true);
            
            for(Long projectId : saveCache.keySet())
                saveLogs(projectId, session);
            
            session.comitTransaction();
        } catch (RuntimeException ex) {
            session.rollBackTransaction();
            logger.error(ex, "Unable to save ChangeLogs!");
            throw ex;
        }
    }
    
    private Session openSession(boolean isTransaction) {
        PersistenceUnit pu = PersistenceUtil.getLookup().lookup(PersistenceUnit.class);
        Session session = pu.getSession();
        if(isTransaction)
            session.beginTransaction();
        return session;
    }
    
    public synchronized void saveLogs(Project project) {
        checkProject(project);
        Session session = null;
        try {
            session = openSession(true);
            saveLogs(project.getId(), session);
            session.comitTransaction();
        } catch (RuntimeException ex) {
            session.rollBackTransaction();
            logger.error(ex, "Unable to save ChangeLogs for project '%s'!", project.getName());
            throw ex;
        }
    }
    
    private void saveLogs(Long projectId, Session session) {
        Set<ChangeLog> cache = saveCache.get(projectId);
        if(cache != null && !cache.isEmpty())
            saveLogs(cache, session);
        saveCache.remove(projectId);
    }
    
    private void saveLogs(Set<ChangeLog> logs, Session session) {
        for(ChangeLog log : logs) {
            session.persist(log);
            logger.trace("Saving log: %s", log);
        }
    }
    
    public synchronized List<ChangeLog> getChanges(Project project) {
        checkProject(project);
        List<ChangeLog> result = queryChanges(project);
        result.addAll(getCachedChanges(project));
        Collections.sort(result, COMPARATOR);
        return result;
    }
    
    private List<ChangeLog> queryChanges(Project project) {
        Session session = null;
        
        try {
            session = openSession(false);
            Query query = session.createQuery(QUERY);
            query.setParameter("projectId", project.getId());
            return query.getResultList();
        } catch (RuntimeException ex) {
            logger.error(ex, "Unable to load ChangeLogs for project '%s'!", project.getName());
            throw ex;
        } finally {
            session.close();
        }
    }
    
    private List<ChangeLog> getCachedChanges(Project project) {
        Set<ChangeLog> cache = saveCache.get(project.getId());
        if(cache == null || cache.isEmpty())
            return new ArrayList<ChangeLog>();
        return new ArrayList<ChangeLog>(cache);
    }
}