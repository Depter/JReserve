package org.jreserve.database;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;
import org.openide.loaders.UniFileLoader;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractDatabaseLoader extends UniFileLoader {
    
    private final static Logger logger = Logger.getLogger(AbstractDatabaseLoader.class.getName());
    private final static String DRIVER_NAME_ERROR = 
        "Unable to load property '%s' from file '%s'! "
      + "File will not be recognized as a database file!";
    
    private final static String MIME_TYPE = "jreserve/database";
    private final Class<?> driverClass;
    
    protected AbstractDatabaseLoader(Class driverClass) {
        super(AbstractDatabase.class.getName());
        this.driverClass = driverClass;
    }

    @Override
    protected FileObject findPrimaryFile(FileObject fo) {
        if(!fo.getMIMEType().equals(MIME_TYPE))
            return null;
        return findPrimaryFileByDriver(fo);
    }
    
    private FileObject findPrimaryFileByDriver(FileObject fo) {
        String driverName = getDriverName(fo);
        if(driverClass.getName().equalsIgnoreCase(driverName))
            return fo;
        return null;
    }
    
    private String getDriverName(FileObject fo) {
        try {
            PropertyReader reader = new PropertyReader(fo);
            Properties properties = reader.readProperties();
            return properties.getProperty(AbstractDatabase.DRIVER_CLASS);
        } catch (IOException ex) {
            logger.log(Level.WARNING, String.format(DRIVER_NAME_ERROR, AbstractDatabase.DRIVER_CLASS, fo.getPath()), ex);
            return null;
        }
    }
}
