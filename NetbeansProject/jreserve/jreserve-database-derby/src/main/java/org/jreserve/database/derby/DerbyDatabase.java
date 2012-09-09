package org.jreserve.database.derby;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.jreserve.database.AbstractDatabase;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DerbyDatabase extends AbstractDatabase {
    
    private final static Logger logger = Logging.getLogger(DerbyDatabase.class.getName());
    
    public final static String URL = "jdbc:derby:%s";
    public final static String DB_FOLDER  = "db.location";
    
    DerbyDatabase(FileObject file, DerbyDatabaseLoader loader) throws DataObjectExistsException, IOException {
        super(file, loader);
    }
    
    @Override
    protected void checkProperties() throws IOException{
        super.checkProperties();
        checkPropertySet(DB_FOLDER);
    }
    
    public String getDatabaseFolder() {
        return super.getProperty(DB_FOLDER);
    }
    
    public void setDatabaseFolder(File file) {
        super.setProperty(DB_FOLDER, file.getAbsolutePath());
    }
    
    @Override
    protected void beforeDelete() {
        String home = getDatabaseFolder();
        DerbyDatabaseDeleter deleter = new DerbyDatabaseDeleter(home);
        deleter.deleteDatabase();
    }

    @Override
    public String getConnectionUrl() {
        return String.format(URL, getProperty(DB_FOLDER));
    }

    @Override
    public String getDialect() {
        return null;
    }
    
    @Override
    public void setUsed(boolean used) {
        super.setUsed(used);
        if(!used)
            shutDownDerby();
    }
    
    private void shutDownDerby() {
        try {
            logger.debug("Shutting down derby database: %s", getDatabaseFolder());
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            if(!ex.getSQLState().equalsIgnoreCase("XJ015"))
                logger.error(ex, "Unable to shut down derby database: %s", getDatabaseFolder());
        }
    }
}
