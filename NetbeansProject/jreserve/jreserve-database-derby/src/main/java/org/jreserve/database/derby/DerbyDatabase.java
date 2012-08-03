package org.jreserve.database.derby;

import java.io.File;
import java.io.IOException;
import org.jreserve.database.AbstractDatabase;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DerbyDatabase extends AbstractDatabase {
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
}
