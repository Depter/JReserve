package org.jreserve.database.explorer;

import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DbExplorerRootChildFactory extends ChildFactory<String> {
    
    private final static String PROVIDERS = "providers";
    private final static String DATABASES = "databases";

    @Override
    protected boolean createKeys(List<String> keys) {
        keys.add(PROVIDERS);
        keys.add(DATABASES);
        return true;
    }

    @Override
    protected Node createNodeForKey(String key) {
        if(PROVIDERS.equalsIgnoreCase(key))
            return new ProvidersRootNode();
        return new DatabaseRootNode();
    }
    
    
}
