package org.jreserve.database.oracle;

import org.jreserve.database.DatabaseProvider;
import org.jreserve.database.oracle.create.CreateDatabaseWizard;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ServiceProvider(service=DatabaseProvider.class)
public class OracleDatabaseProvider implements DatabaseProvider {
    public final static String DRIVER_NAME = "oracle.jdbc.OracleDriver";
    public final static Class<?> DRIVER = oracle.jdbc.OracleDriver.class;

    @Override
    public String getName() {
        return "Oracle";
    }
    
    @Override
    public boolean createDatabase() {
        return new CreateDatabaseWizard().createDatabase();
    }
}
