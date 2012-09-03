package org.jreserve.logging.view;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.jreserve.logging.Logger;

/**
 *
 * @author Peter Decsi
 */
public class Log4jGuiAppender extends AppenderSkeleton {

    @Override
    protected void append(LoggingEvent le) {
        String msg = super.getLayout().format(le);
        Logger.Level level = getLevel(le.getLevel());
        GuiAppender.append(level, msg);
    }

    private Logger.Level getLevel(Level level) {
        if(level.isGreaterOrEqual(Level.OFF))
            return Logger.Level.FATAL;
        if(level.isGreaterOrEqual(Level.FATAL))
            return Logger.Level.FATAL;
        if(level.isGreaterOrEqual(Level.ERROR))
            return Logger.Level.ERROR;
        if(level.isGreaterOrEqual(Level.WARN))
            return Logger.Level.WARN;
        if(level.isGreaterOrEqual(Level.INFO))
            return Logger.Level.INFO;
        if(level.isGreaterOrEqual(Level.DEBUG))
            return Logger.Level.DEBUG;
        if(level.isGreaterOrEqual(Level.TRACE))
            return Logger.Level.TRACE;
        if(level.isGreaterOrEqual(Level.ALL))
            return Logger.Level.TRACE;
        throw new IllegalArgumentException("Unknown Log4j level: "+level);
    }
    
    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    
}
