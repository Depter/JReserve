package org.jreserve.logging;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.openide.util.Exceptions;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Logging {
    
    private final static String LAYOUT = "%d %-5p %m%n";
    
    private static boolean initialized = false;
    
    public static synchronized Logger getLogger(String name) {
        if(!initialized)
            initialize();
        return new Log4jLogger(getLog4jLogger(name));
    }
    
    private static void initialize() {
        initialized = true;
        appendFileLogger();
    }
    
    private static void appendFileLogger() {
        org.apache.log4j.Logger root = LogManager.getRootLogger();
        try {
            root.addAppender(new FileAppender(new PatternLayout(LAYOUT), getLogFile(), false));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private static String getLogFile() {
        File home = new File(System.getProperty("netbeans.user"));
        home = new File(home, "Logging");
        if(!home.exists())
            home.mkdir();
        return new File(home, "jreserve.log").getPath();
    }
    
    private static org.apache.log4j.Logger getLog4jLogger(String name) {
        return org.apache.log4j.Logger.getLogger(name);
    }
    
}
