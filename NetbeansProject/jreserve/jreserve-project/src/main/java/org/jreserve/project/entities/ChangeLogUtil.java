package org.jreserve.project.entities;

import java.util.*;
import javax.swing.SwingUtilities;
import org.jreserve.persistence.Query;
import org.jreserve.persistence.Session;
import org.jreserve.project.system.management.AbstractElementCache;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ChangeLogUtil extends AbstractElementCache<Project, ChangeLog, String> {
    
    private static ChangeLogUtil DEFAULT = null;
    
    public static synchronized ChangeLogUtil getDefault() {
        if(DEFAULT == null)
            DEFAULT = new ChangeLogUtil();
        return DEFAULT;
    }
    
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
    
    private Map<String, Set<ChangeLogListener>> listeners = new HashMap<String, Set<ChangeLogListener>>();
    
    private ChangeLogUtil() {
    }
    
    @Override
    protected String getName() {
        return "ChangeLogUtil";
    }

    @Override
    protected void checkKey(Project project) {
        if(project.getVersion() != null)
            return;
        String msg = String.format(ERR_PROJECT_NOT_PERSISTED, project.getName());
        throw new IllegalArgumentException(msg);
    }

    @Override
    protected Query createQuery(Session session, Project project) {
        Query query = session.createQuery(QUERY);
        query.setParameter("projectId", project.getId());
        return query;
    }

    @Override
    protected Comparator<ChangeLog> getComparator() {
        return COMPARATOR;
    }

    @Override
    protected String getId(Project project) {
        return project.getId();
    }
    
    public synchronized void addChange(Project project, ChangeLog.Type type, String log) {
        ChangeLog change = new ChangeLog(project, type, log);
        super.addValue(project, change);
        fireAddChange(project, change);
    }

    public synchronized void addChangeLogListener(Project project, ChangeLogListener listener) {
        checkKey(project);
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
    
    private void fireAddChange(Project project, ChangeLog log) {
        Set<ChangeLogListener> pl = listeners.get(project.getId());
        if(pl != null)
            fireAddChangeFromEDT(new ArrayList<ChangeLogListener>(pl), log);
    }
    
    private void fireAddChangeFromEDT(final List<ChangeLogListener> listeners, final ChangeLog log) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for(ChangeLogListener l : listeners)
                    l.changeLogAdded(log);
            }
        });
    }

    @Override
    protected boolean isNew(ChangeLog entity) {
        return entity.getVersion() == null;
    }
}