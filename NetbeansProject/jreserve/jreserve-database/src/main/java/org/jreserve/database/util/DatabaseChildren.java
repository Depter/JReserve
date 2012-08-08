package org.jreserve.database.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jreserve.database.AbstractDatabase;
import org.jreserve.database.DatabaseNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * Super class for all {@link Children Children} instances that would like
 * to provide database nodes for views.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class DatabaseChildren extends Children.Keys<AbstractDatabase>{
    
    public static DatabaseChildren getFixedChildren(final List<AbstractDatabase> databases) {
        return new DatabaseChildren() {
            @Override
            protected List<AbstractDatabase> getDatabases() {
                return databases;
            }
        };
    }
    
    private final static Comparator<AbstractDatabase> DB_COMPARATOR = new Comparator<AbstractDatabase>() {
        @Override
        public int compare(AbstractDatabase o1, AbstractDatabase o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    };
    
    private String filter = null;
    
    protected DatabaseChildren() {
    }

    @Override
    protected void addNotify() {
        refreshDatabases();
    }
    
    /**
     * Refreshes the list of database children.
     */
    public final void refreshDatabases() {
        removeOldNodes();
        setKeys(getFilteredDatabases());
        super.refresh();
    }
    
    private void removeOldNodes() {
        if(getNodesCount() > 0)
            setKeys(new AbstractDatabase[0]);        
    }
    
    private List<AbstractDatabase> getFilteredDatabases() {
        List<AbstractDatabase> dbs = filterDatabases(getDatabases());
        Collections.sort(dbs, DB_COMPARATOR);
        return dbs;
    }
    
    /**
     * Filters the databases. The default implementation filters the 
     * databases by name.
     * 
     * @param dbs the list returned by {@link #getDatabases() getDatabases()}. 
     * Do not modify it.
     * @return a new list, containing the filtered databases.
     */
    protected List<AbstractDatabase> filterDatabases(List<AbstractDatabase> dbs) {
        List<AbstractDatabase> result = new ArrayList<AbstractDatabase>(dbs.size());
        for(AbstractDatabase db : dbs)
            if(filterName(db.getShortName()))
                result.add(db);
        return result;
    }
    
    private boolean filterName(String name) {
        if(filter == null)
            return true;
        return name.toLowerCase().indexOf(filter) >= 0;
    }
    
    /**
     * Return here the interesting databases. The returned list 
     * will not be modified.
     */
    protected abstract List<AbstractDatabase> getDatabases();
    
    @Override
    protected Node[] createNodes(AbstractDatabase key) {
        Node node = new DatabaseNode(key);
        return new Node[]{node};
    }
    
    void setNameFilter(String filter) {
        this.filter = filter==null? null : filter.toLowerCase();
        refreshDatabases();
    }
}
