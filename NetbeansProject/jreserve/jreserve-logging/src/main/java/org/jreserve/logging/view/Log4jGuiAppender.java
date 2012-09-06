package org.jreserve.logging.view;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.jreserve.logging.Logger;

/**
 *
 * @author Peter Decsi
 */
public class Log4jGuiAppender extends AppenderSkeleton {

    @Override
    protected void append(LoggingEvent le) {
        String msg = getMsg(le);
        Logger.Level level = getLevel(le.getLevel());
        GuiAppender.append(level, msg);
    }

    private String getMsg(LoggingEvent le) {
        String msg = super.getLayout().format(le);
        if(hasStackTrace(le))
            msg += getStackTrace(le.getThrowableStrRep());
        return msg;
    }
    
    private boolean hasStackTrace(LoggingEvent le) {
        ThrowableInformation ti = le.getThrowableInformation();
        if(ti == null)
            return false;
        String[] stack = ti.getThrowableStrRep();
        return stack != null && stack.length > 0;
    }
    
    private String getStackTrace(String[] stack) {
        StringBuilder sb = new StringBuilder();
        for(int i=0, length=stack.length; i<length; i++) {
            if(i > 0)
                sb.append("\n");
            sb.append(stack[i]);
        }
        return sb.toString();
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
