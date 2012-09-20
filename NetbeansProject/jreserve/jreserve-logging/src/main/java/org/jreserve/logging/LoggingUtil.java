package org.jreserve.logging;

import java.util.logging.Level;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 */
@Messages({
    "LBL.LoggingUtil.severe=ERROR",
    "LBL.LoggingUtil.warning=WARNING",
    "LBL.LoggingUtil.config=CONFIG",
    "LBL.LoggingUtil.info=INFO",
    "LBL.LoggingUtil.fine=DEBUG",
    "LBL.LoggingUtil.finer=TRACE",
    "LBL.LoggingUtil.unknown=UNKNOWN"
})
public class LoggingUtil {

    public final static int MAX_USER_NAME = 7;
    
    public static Level escapeLevel(Level level) {
        int intLevel = level.intValue();
        if(Level.FINER.intValue() >= intLevel)
            return Level.FINER;
        else if(Level.FINE.intValue() >= intLevel)
            return Level.FINE;
        else if(Level.CONFIG.intValue() >= intLevel)
            return Level.CONFIG;
        else if(Level.INFO.intValue() >= intLevel)
            return Level.INFO;
        else if(Level.WARNING.intValue() >= intLevel)
            return Level.WARNING;
        else
            return Level.SEVERE;
    }
    
    public static String getUserName(Level level) {
        level = escapeLevel(level);
        if(Level.SEVERE.equals(level))
            return Bundle.LBL_LoggingUtil_severe();
        else if(Level.WARNING.equals(level))
            return Bundle.LBL_LoggingUtil_warning();
        else if(Level.INFO.equals(level))
            return Bundle.LBL_LoggingUtil_info();
        else if(Level.CONFIG.equals(level))
            return Bundle.LBL_LoggingUtil_config();
        else if(Level.FINE.equals(level))
            return Bundle.LBL_LoggingUtil_fine();
        else if(Level.FINER.equals(level))
            return Bundle.LBL_LoggingUtil_finer();
        else
            return Bundle.LBL_LoggingUtil_unknown();
    }
}
