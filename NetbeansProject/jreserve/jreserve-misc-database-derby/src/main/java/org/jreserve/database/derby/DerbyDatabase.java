package org.jreserve.database.derby;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.database.AbstractDatabase;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "# {0} - db dile",
    "MSG.DerbyDatabase.FileNotFound=Database \"{0}\" does not exists!"
})
public class DerbyDatabase extends AbstractDatabase {
    
    private final static Logger logger = Logger.getLogger(DerbyDatabase.class.getName());
    
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
            logger.log(Level.FINE, "Shutting down derby database: \"{0}\"", getDatabaseFolder());
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ex) {
            if(!ex.getSQLState().equalsIgnoreCase("XJ015"))
                logger.log(Level.SEVERE, String.format("Unable to shut down derby database: %s", getDatabaseFolder()), ex);
        }
    }
    
    @Override
    public boolean isValidDatabase() {
        File file = new File(getDatabaseFolder());
        if(file.exists() && file.isDirectory())
            return true;
        showFileNotFoundDialog(file);
        return false;
    }
    
    private void showFileNotFoundDialog(File file) {
        String msg = Bundle.MSG_DerbyDatabase_FileNotFound(file.getAbsolutePath());
        NotifyDescriptor nd = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(nd);
    }
}
