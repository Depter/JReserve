package org.jreserve.logging;

import org.jreserve.logging.LoggerProperties;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.jreserve.logging.view.GuiHandler;
import org.jreserve.logging.view.LogviewTopComponent;

/**
 *
 * @author Peter Decsi
 */
public class LoggingSetting {
    
    private final static Logger logger = Logger.getLogger(LoggingSetting.class.getName());
    
    private static transient GuiHandler guiHandler;
    
    public static void initialize() {
        Properties props = LoggerProperties.getProperties();
        configureGui(props);
        configureHandlers();
        applyLevels(props);
    }
    
    private static void configureGui(Properties props) {
        if(showGui(props))
           deleteGuiAppender();
        else
            appendGuiAppender();
    }
    
    private static boolean showGui(Properties props) {
        String strShow = props.getProperty("gui.show", "false");
        return "true".equalsIgnoreCase(strShow);
    }
    
    private static void configureHandlers() {
        Logger root = Logger.getLogger("");
        for(Handler handler : root.getHandlers())
            handler.setLevel(Level.ALL);
    }
    
    private static void applyLevels(Properties properties) {
        for(String prop : properties.stringPropertyNames())
            if(isLevelProperty(prop))
                setLevel(prop, properties.getProperty(prop));
    }
    
    private static boolean isLevelProperty(String property) {
        return property.toLowerCase().endsWith(".level");
    }
    
    private static void setLevel(String property, String value) {
        Logger l = getLogger(property);
        Level level = Level.parse(value);
        logger.warning(String.format("Logger level: '%s' => %s", l.getName(), level.getName()));
        l.setLevel(level);
    }
    
    private static Logger getLogger(String property) {
        int index = property.lastIndexOf('.');
        String loggerName = property.substring(0, index);
        return Logger.getLogger(loggerName);
    }
    
    static void setLevel(Level level) {
        Logger root = LogManager.getLogManager().getLogger("");
        root.setLevel(level);
        LoggerProperties.setLevel(level);
        LoggerProperties.save();
    }
    
    static void appendGuiAppender() {
        if(guiHandler != null)
            return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                guiHandler = GuiHandler.getInstance();
                Logger.getLogger("").addHandler(guiHandler);
                LogviewTopComponent.openView();
            }
        });
    }
    
    static void deleteGuiAppender() {
        if(guiHandler != null) {
            Logger.getLogger("").removeHandler(guiHandler);
            guiHandler = null;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LogviewTopComponent.closeView();
            }
        });
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
