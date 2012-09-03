package org.jreserve.logging.settings;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.jreserve.logging.Logger;
import org.jreserve.logging.view.Log4jGuiAppender;
import org.openide.util.Exceptions;

/**
 *
 * @author Peter Decsi
 */
public class LoggingSetting {
    
    private final static String LAYOUT = "%d %-5p %m%n";
    private static Log4jGuiAppender guiAppender;
    
    static void initialize() {
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

    static void appendGuiAppender() {
        if(guiAppender != null)
            return;
        guiAppender = new Log4jGuiAppender();
        guiAppender.setLayout(new PatternLayout(LAYOUT));
        LogManager.getRootLogger().addAppender(guiAppender);
    }
    
    static void deleteGuiAppender() {
        if(guiAppender == null)
            return;
        LogManager.getRootLogger().removeAppender(guiAppender);
        guiAppender = null;
    }
    
    static void setLevel(Logger.Level level) {
        org.apache.log4j.Logger root = LogManager.getRootLogger();
        switch(level) {
            case FATAL:
                root.setLevel(Level.FATAL);
                break;
            case ERROR:
                root.setLevel(Level.ERROR);
                break;
            case WARN:
                root.setLevel(Level.WARN);
                break;
            case INFO:
                root.setLevel(Level.INFO);
                break;
            case DEBUG:
                root.setLevel(Level.DEBUG);
                break;
            case TRACE:
                root.setLevel(Level.TRACE);
                break;
            default:
                throw new IllegalArgumentException("Unknwon level: "+level);
        }
    }
}
