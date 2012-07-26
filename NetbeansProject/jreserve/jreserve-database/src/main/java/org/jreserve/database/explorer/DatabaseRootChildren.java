package org.jreserve.database.explorer;

import java.util.List;
import org.jreserve.database.AbstractDatabase;
import org.jreserve.database.DatabaseUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DatabaseRootChildren extends DatabaseChildren {

    private static DatabaseRootChildren INSTANCE = null;
    
    public static DatabaseRootChildren getInstance() {
        if(INSTANCE == null)
            INSTANCE = new DatabaseRootChildren();
        return INSTANCE;
    }
    
    private DatabaseRootChildren() {
    }

    @Override
    protected List<AbstractDatabase> getDatabases() {
        return DatabaseUtil.getOpenedDatabases();
    }
    
}
