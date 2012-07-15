package org.jreserve.dbexplorer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jreserve.database.api.DatabaseProvider;
import org.jreserve.database.api.DatabaseProviderComparator;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DbProviderChildren extends Children.Keys<DatabaseProvider> {

    @Override
    protected Node[] createNodes(DatabaseProvider provider) {
        return new Node[] {new DbProviderNode(provider)};
    }
    
    @Override
    protected void addNotify() {
        Collection<? extends DatabaseProvider> providers = Lookup.getDefault().lookupAll(DatabaseProvider.class);
        List<? extends DatabaseProvider> providerList = new ArrayList<DatabaseProvider>(providers);
        Collections.sort(providerList, DatabaseProviderComparator.INSTANCE);
        setKeys(providerList);
    }

}
