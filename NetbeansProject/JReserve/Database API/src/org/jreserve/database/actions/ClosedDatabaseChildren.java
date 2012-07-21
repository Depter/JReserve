package org.jreserve.database.actions;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.database.AbstractDatabase;
import org.jreserve.database.DatabaseUtil;
import org.jreserve.database.explorer.DatabaseChildren;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class ClosedDatabaseChildren extends DatabaseChildren {
    
    private String filter;
    
    void setNameFilter(String filter) {
        this.filter = filter==null? null : filter.toLowerCase();
        refilter();
    }
    
    private void refilter() {
        super.refreshDatabases();
    }

    @Override
    protected List<AbstractDatabase> getDatabases() {
        List<AbstractDatabase> dbs = DatabaseUtil.getClosedDatabases();
        List<AbstractDatabase> result = new ArrayList<AbstractDatabase>();
        for(AbstractDatabase db : dbs)
            if(filterName(db.getName()))
                result.add(db);
        return result;
    }
    
    private boolean filterName(String name) {
        if(filter == null)
            return true;
        return name.toLowerCase().indexOf(filter) >= 0;
    }
}
