package org.jreserve.dbexplorer;

import java.util.List;
import org.jreserve.branding.msg.MessageUtil;
import org.jreserve.database.api.Database;
import org.jreserve.database.api.DatabaseProvider;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "#0 - the DatabaseProvider",
    "ERR_loadDatabase=Database provide {0} unable to load databases!"
})
class DbChildren extends Children.Keys<Database>{
    
    private DatabaseProvider provider;

    DbChildren(DatabaseProvider provider) {
        this.provider = provider;
    }
    
    @Override
    protected Node[] createNodes(Database database) {
        return new Node[] {new DbNode(provider, database)};
    }
    
    @Override
    protected void addNotify() {
        List<? extends Database> dbs = getDatabases();
        if(dbs != null)
            setKeys(dbs);
    }
    
    void refreshDatabases() {
        List<? extends Database> dbs = getDatabases();
        if(dbs != null) {
            setKeys(dbs);
            super.refresh();
        }
    }
    
    private List<? extends Database> getDatabases() {
        try {
            return provider.getDatabases();
        } catch(Exception ex) {
            MessageUtil.error(Bundle.ERR_loadDatabase(provider.getName()), ex);
            return null;
        }
    }
}
