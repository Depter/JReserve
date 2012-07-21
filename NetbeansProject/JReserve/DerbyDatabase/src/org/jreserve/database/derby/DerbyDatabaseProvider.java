package org.jreserve.database.derby;

import org.jreserve.database.DatabaseProvider;
import org.jreserve.database.derby.create.CreateDatabaseWizard;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=DatabaseProvider.class)
public class DerbyDatabaseProvider implements DatabaseProvider {
    
    public final static String DRIVER_NAME = "org.apache.derby.jdbc.EmbeddedDriver";
    public final static Class<?> DRIVER = org.apache.derby.jdbc.EmbeddedDriver.class;

    @Override
    public String getName() {
        return "Derby";
    }

    @Override
    public boolean createDatabase() {
        CreateDatabaseWizard wizard = new CreateDatabaseWizard();
        return wizard.createDatabase();
    }
}
