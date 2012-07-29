package org.jreserve.database.explorer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jreserve.database.AbstractDatabase;
import org.jreserve.database.DatabaseNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class DatabaseChildren extends Children.Keys<AbstractDatabase>{
    
    private final static Comparator<AbstractDatabase> DB_COMPARATOR = new Comparator<AbstractDatabase>() {
        @Override
        public int compare(AbstractDatabase o1, AbstractDatabase o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    };
    
    public DatabaseChildren() {
        setKeys(getSortedDatabases());
    }
    
    public final void refreshDatabases() {
        setKeys(new AbstractDatabase[0]);
        List<AbstractDatabase> dbs = getSortedDatabases();
        setKeys(dbs);
        super.refresh();
    }
    
    private List<AbstractDatabase> getSortedDatabases() {
        List<AbstractDatabase> databases = getDatabases();
        Collections.sort(databases, DB_COMPARATOR);
        return databases;
    }
    
    protected abstract List<AbstractDatabase> getDatabases();
    
    @Override
    protected Node[] createNodes(AbstractDatabase key) {
        Node node = new DatabaseNode(key);
        return new Node[]{node};
    }
}
