package org.jreserve.database.explorer;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.database.DatabaseProvider;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ProviderRootChildren extends Children.Keys<DatabaseProvider>{

    private final static Logger logger = Logger.getLogger(ProviderRootChildren.class.getName());
    
    private final static Comparator<DatabaseProvider> PROVIDER_COMPARATOR = new Comparator<DatabaseProvider>() {

        @Override
        public int compare(DatabaseProvider o1, DatabaseProvider o2) {
            String n1 = o1.getName();
            String n2 = o2.getName();
            return compare(n1, n2);
        }
        
        private int compare(String n1, String n2) {
            if(n1 == null)
                return n2==null? 0 : 1;
            return n2==null? -1 : n1.compareToIgnoreCase(n2);
        }
    };
    
    ProviderRootChildren() {
        List<DatabaseProvider> providers = new ArrayList<DatabaseProvider>(lookupProviders());
        Collections.sort(providers, PROVIDER_COMPARATOR);
        logProviders(providers);
        setKeys(providers);
    }
    
    private Collection<? extends DatabaseProvider> lookupProviders() {
        logger.info("Loading database providers...");
        return Lookup.getDefault().lookupAll(DatabaseProvider.class);
    }
    
    private void logProviders(List<DatabaseProvider> providers) {
        for(DatabaseProvider provider : providers)
            logger.log(Level.INFO, "DatabseProvider found: \"{0}\"", provider.getName());
    }
    
    @Override
    protected Node[] createNodes(DatabaseProvider key) {
        return new Node[]{new DatabaseProviderNode(key)};
    }

}
