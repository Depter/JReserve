package org.jreserve.database.api;

import java.util.Comparator;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DatabaseProviderComparator implements Comparator<DatabaseProvider> {
    
    public final static Comparator<DatabaseProvider> INSTANCE = new DatabaseProviderComparator();
    
    
    @Override
    public int compare(DatabaseProvider o1, DatabaseProvider o2) {
        if(o1 == null)
            return o2==null? 0 : 1;
        return o2==null? -1 : o1.getName().compareToIgnoreCase(o2.getName());
    }

}
