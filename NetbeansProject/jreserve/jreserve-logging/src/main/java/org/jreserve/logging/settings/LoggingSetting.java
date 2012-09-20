package org.jreserve.logging.settings;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.jreserve.logging.view.Log4jGuiAppender;

/**
 *
 * @author Peter Decsi
 */
public class LoggingSetting {
    
    private final static Logger logger = Logger.getLogger(LoggingSetting.class.getName());
    
    private final static String PACKAGE = "org.jreserve.logging";
    private final static String CONFIG_FILE_PATH = "logging/logging.properties";
    private final static String CONFIG_FILE_PROP = "java.util.logging.config.file";
    
    private final static String LAYOUT = "%d %-5p %m%n";
    private static Log4jGuiAppender guiAppender;
    
    static void initialize() {
        Logger.getLogger("org.jreserve").setLevel(Level.ALL);
//        Properties props = LoggerProperties.getProperties();
        Logger l = logger;
        while(l.getParent() != null)
            l = l.getParent();
    }
    
    private static String toString(Handler handler) {
        StringBuilder sb = new StringBuilder();
        sb.append("Handler: ").append(handler.toString())
          .append("\n\tErrorManager: ").append(handler.getErrorManager())
          .append("\n\tFormatter: ").append(handler.getFormatter())
          .append("\n\tFilter: ").append(handler.getFilter())
          .append("\n\tLevel: ").append(handler.getLevel().getName());
        return sb.toString();
    }
    
    private static void logProperties(Properties props) {
        logger.warning("Logging properties:");
        for(String prop : props.stringPropertyNames())
            logger.warning(String.format("Loggin: %s => %s", prop, props.getProperty(prop)));
    }
    

    private static InputStream getPropsAsStream(Properties props) {
        StringWriter writer = new StringWriter();
        try {props.store(writer, null);} catch (IOException ex) {}
        String str = writer.toString();
        return new ByteArrayInputStream(str.getBytes());
    }
    
    static void setLevel(Level level) {
        Logger root = LogManager.getLogManager().getLogger("");
        root.setLevel(level);
        LoggerProperties.setLevel(level);
        LoggerProperties.save();
    }
    
    static void appendGuiAppender() {
    }
    
    static void deleteGuiAppender() {
    }
    
//    private static void appendFileLogger() {
//        org.apache.log4j.Logger root = LogManager.getRootLogger();
//        try {
//            root.addAppender(new FileAppender(new PatternLayout(LAYOUT), getLogFile(), false));
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//    }
//    
//    private static String getLogFile() {
//        File home = new File(System.getProperty("netbeans.user"));
//        home = new File(home, "Logging");
//        if(!home.exists())
//            home.mkdir();
//        return new File(home, "jreserve.log").getPath();
//    }
//
//    static void appendGuiAppender() {
//        if(guiAppender != null)
//            return;
//        guiAppender = new Log4jGuiAppender();
//        guiAppender.setLayout(new PatternLayout(LAYOUT));
//        LogManager.getRootLogger().addAppender(guiAppender);
//    }
//    
//    static void deleteGuiAppender() {
//        if(guiAppender == null)
//            return;
//        LogManager.getRootLogger().removeAppender(guiAppender);
//        guiAppender = null;
//    }
//    
//    static void setLevel(Logger.Level level) {
//        org.apache.log4j.Logger root = LogManager.getRootLogger();
//        switch(level) {
//            case FATAL:
//                root.setLevel(Level.FATAL);
//                break;
//            case ERROR:
//                root.setLevel(Level.ERROR);
//                break;
//            case WARN:
//                root.setLevel(Level.WARN);
//                break;
//            case INFO:
//                root.setLevel(Level.INFO);
//                break;
//            case DEBUG:
//                root.setLevel(Level.DEBUG);
//                break;
//            case TRACE:
//                root.setLevel(Level.TRACE);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknwon level: "+level);
//        }
//    }
}
