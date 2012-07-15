package org.jreserve.dbexplorer.newdb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import org.jreserve.database.api.DatabaseProvider;
import org.jreserve.database.api.DatabaseProviderComparator;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DbProviderModel extends DefaultComboBoxModel<DatabaseProvider> {

    private List<DatabaseProvider> providers;
    
    DbProviderModel() {
        providers = new ArrayList<DatabaseProvider>(Lookup.getDefault().lookupAll(DatabaseProvider.class));
        Collections.sort(providers, DatabaseProviderComparator.INSTANCE);
        System.out.println("x");
    }

    @Override
    public int getSize() {
        return providers.size();
    }

    @Override
    public DatabaseProvider getElementAt(int index) {
        return providers.get(index);
    }
}
