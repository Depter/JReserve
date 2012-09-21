package org.jreserve.logging;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Exceptions;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class LoggerProperties {
    
    public final static String SHOW_GUI = "gui.show";
    public final static String MAIN_LEVEL = ".level";
    
    private final static Logger logger = Logger.getLogger(LoggerProperties.class.getName());
    private final static String PACKAGE = "org.jreserve.logging";
    private static Properties PROPERTIES = null;
    
    public static Properties getProperties() {
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
    }
    
    private static File getPropertiesFile() {
        File child = new File(getConfigRoot(), "logging.properties");
        if(!child.exists())
            return null;
        return child;
    }
    
    private static File getConfigRoot() {
        InstalledFileLocator locator = InstalledFileLocator.getDefault();
        File root = locator.locate("logging", PACKAGE, true);
        logger.log(Level.FINER, "Logging setting root: \"{0}\"", root);
        return root;
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
    
    public static void save() {
        save(getProperties());
    }
    
    static void setLevel(Level level) {
        Properties props = getProperties();
        props.setProperty(".level", level.getName());
    }
}
