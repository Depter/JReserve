package org.jreserve.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DatabaseUtil {
    
    private final static Logger logger = Logging.getLogger(DatabaseUtil.class.getName());
    
    private final static String DB_EXTENSION = "database";
    private final static String HOME_DIR = "Databases";
    
    private static FileObject HOME;
    private static List<AbstractDatabase> databases = null;
    
    public synchronized static List<AbstractDatabase> getOpenedDatabases() {
        return getAllByProperty(AbstractDatabase.IS_OPENED, AbstractDatabase.TRUE);
    }
    
    public synchronized static List<AbstractDatabase> getAllByProperty(String property, String value) {
        List<AbstractDatabase> result = new ArrayList<AbstractDatabase>();
        for(AbstractDatabase db : getLoadedDatabases())
            if(hasProperty(db, property, value))
                result.add(db);
        return result;
    }
    
    private synchronized static List<AbstractDatabase> getLoadedDatabases() {
        loadIfNeeded();
        return databases;
    }
    
    private static void loadIfNeeded() {
        if(databases != null)
            return;
        refresh();
    }
    
    public static void refresh() {
        if(HOME == null)
            initHomeDirectory();
        logger.info("Loading databases...");
        loadDatabasees();
    }
    
    private static void initHomeDirectory() {
        try {
            FileObject configHome = FileUtil.getConfigRoot();
            HOME = FileUtil.createFolder(configHome, HOME_DIR);
            logger.info("Database home directory set to '%s'.", HOME.getPath());
        } catch (IOException ex) {
            logger.error(ex, "Unable to create directory '%s' in user dir!", HOME_DIR);
            Exceptions.printStackTrace(ex);
        }
    }
    
    private static void loadDatabasees() {
        databases = new ArrayList<AbstractDatabase>();
        for(FileObject file : HOME.getChildren())
            if(file.isData() && file.getMIMEType().equals("jreserve/database"))
                addFileToList(file);
    }
    
    private static void addFileToList(FileObject file) {
        try {
            logger.info(String.format("Loading database file '%s'.", file.getPath()));
            DataObject obj = DataObject.find(file);
            if(!(obj instanceof AbstractDatabase))
                return;
            databases.add((AbstractDatabase) obj);
        } catch (DataObjectNotFoundException ex) {
            logger.error("Unable to load file!", ex);
        }
    }
    
    private static boolean hasProperty(AbstractDatabase db, String proeprty, String value) {
        String propValue = db.getProperty(proeprty);
        if(propValue == null)
            return value == null;
        return propValue.equalsIgnoreCase(value);
    }
    
    public synchronized static List<AbstractDatabase> getClosedDatabases() {
        return getAllByProperty(AbstractDatabase.IS_OPENED, AbstractDatabase.FALSE);
    }
    
    public synchronized static AbstractDatabase getDatabaseByName(String name) {
        List<AbstractDatabase> result = getAllByProperty(AbstractDatabase.SHORT_NAME, name);
        return result.isEmpty()? null : result.get(0);
    }
    
    public synchronized static FileObject getFileForName(String name) {
        if(HOME == null)
            initHomeDirectory();
        FileObject file = HOME.getFileObject(name, DB_EXTENSION);
        return file!=null? file : createFileForName(name);
    }
    
    private static FileObject createFileForName(String name) {
        try {
            return HOME.createData(name, DB_EXTENSION);
        } catch (IOException ex) {
            logger.error(ex, "Unable to create file '%s.%s' in '%s'", 
                name, DB_EXTENSION, HOME.getPath());
            return null;
        }
    }
    
    private DatabaseUtil() {}
}
