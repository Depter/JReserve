package org.jreserve.logging.settings;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Exceptions;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class LoggerProperties {
    
    private final static Logger logger = Logger.getLogger(LoggerProperties.class.getName());
    private final static String PACKAGE = "org.jreserve.logging";
    private static Properties PROPERTIES = null;
    
    static Properties getProperties() {
        if(PROPERTIES == null)
            readProperties();
        return PROPERTIES;
    }
    
    private static void readProperties() {
        InputStream is = null;
        try {
            is = new FileInputStream(getPropertiesFile());
            PROPERTIES = new Properties();
            PROPERTIES.load(is);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to read logging properties!", ex);
        } finally {
            if(is != null) {
                try{is.close();} catch (IOException ex) {
                    logger.log(Level.SEVERE, "Unable to close InputStream for logging properties!", ex);
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        PROPERTIES.put("java.util.logging.FileHandler.pattern", getLogFileName());
    }
    
    private static File getPropertiesFile() {
        File child = new File(getConfigRoot(), "logging.properties");
        if(!child.exists())
            createPropertiesFile(child);
        else
            logger.warning("File already exists! "+child);
        return child;
    }
    
    private static File getConfigRoot() {
        InstalledFileLocator locator = InstalledFileLocator.getDefault();
        File root = locator.locate("logging", PACKAGE, true);
        logger.warning("Logging setting root: "+root);
        return root;
    }
    
    private static void createPropertiesFile(File file) {
        logger.warning("Creating file: "+file);
        Properties props = createDefaultProperties();
        save(props);
    }
    
    private static Properties createDefaultProperties() {
        Properties props = new Properties();
        props.put(".level", LoggingPanel.DEFAULT_LEVEL.getName());
        props.put("handlers", "java.util.logging.FileHandler");
        props.put("java.util.logging.FileHandler.pattern", getLogFileName());
        props.put("java.util.logging.FileHandler.limit", "50000");
        props.put("java.util.logging.FileHandler.count", "1");
        props.put("java.util.logging.FileHandler.formatter", "java.util.logging.SimpleFormatter");
        return props;
    }
    
    private static String getLogFileName() {
        FileObject file = FileUtil.getConfigRoot();
        try {
            file = FileUtil.createFolder(file, "Logging");
            return getPath(file);
            //return FileUtil.toFile(file).getPath()+"/jreserve_%u.log";
            //return FileUtil.toFile(file).getAbsolutePath()+"/jreserve_%u.log";
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            return "%h/jreserve%u.log";
        }
    }
    
    private static String getPath(FileObject fo) {
        File file = FileUtil.toFile(fo);
        String path = file.getAbsolutePath();
        if(File.separatorChar == '\\')
            path = path.replace('\\', '/');
        return path + "/jreserve_%u.log";
    }
    
    private static void save(Properties properties) {
        Writer writer = null;
        try {
            writer = new FileWriter(getPropertiesFile(), false);
            properties.store(writer, "Logging properties");
            writer.flush();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to save logging properties!", ex);
            Exceptions.printStackTrace(ex);
        } finally {
            if(writer != null) {
                try {writer.close();} catch (IOException ex) {
                    logger.log(Level.SEVERE, "Unable to close writer for logging properties!", ex);
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }
    
    static void save() {
        save(getProperties());
    }
    
    static void setLevel(Level level) {
        Properties props = getProperties();
        props.setProperty(".level", level.getName());
    }
}
