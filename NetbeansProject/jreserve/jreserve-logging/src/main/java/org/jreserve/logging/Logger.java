package org.jreserve.logging;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class Logger {
    
    public void fatal(Object message) {
        log(Level.FATAL, message);
    }

    public void fatal(Throwable t, Object message) {
        log(Level.FATAL, t, message);
    }
    
    public void fatal(String msgFormat, Object... params) {
        log(Level.FATAL, msgFormat, params);
    }
    
    public void fatal(Throwable t, String msgFormat, Object... params) {
        log(Level.FATAL, t, msgFormat, params);
    }

    public void error(Object message) {
        log(Level.ERROR, message);
    }
    
    public void error(Throwable t, Object message) {
        log(Level.ERROR, t, message);
    }
    
    public void error(String msgFormat, Object... params) {
        log(Level.ERROR, msgFormat, params);
    }
    
    public void error(Throwable t, String msgFormat, Object... params) {
        log(Level.ERROR, t, msgFormat, params);
    }

    public void warn(Object message) {
        log(Level.WARN, message);
    }
    
    public void warn(Throwable t, Object message) {
        log(Level.WARN, t, message);
    }
    
    public void warn(String msgFormat, Object... params) {
        log(Level.WARN, msgFormat, params);
    }
    
    public void warn(Throwable t, String msgFormat, Object... params) {
        log(Level.WARN, t, msgFormat, params);
    }

    public void info(Object message) {
        log(Level.INFO, message);
    }
    
    public void info(Throwable t, Object message) {
        log(Level.INFO, t, message);
    }
    
    public void info(String msgFormat, Object... params) {
        log(Level.INFO, msgFormat, params);
    }
    
    public void info(Throwable t, String msgFormat, Object... params) {
        log(Level.INFO, t, msgFormat, params);
    }

    public void debug(Object message) {
        log(Level.DEBUG, message);
    }
    
    public void debug(Throwable t, Object message) {
        log(Level.DEBUG, t, message);
    }
    
    public void debug(String msgFormat, Object... params) {
        log(Level.DEBUG, msgFormat, params);
    }
    
    public void debug(Throwable t, String msgFormat, Object... params) {
        log(Level.DEBUG, t, msgFormat, params);
    }

    public void trace(Object message) {
        log(Level.TRACE, message);
    }
    
    public void trace(Throwable t, Object message) {
        log(Level.TRACE, t, message);
    }
    
    public void trace(String msgFormat, Object... params) {
        log(Level.TRACE, msgFormat, params);
    }
    
    public void trace(Throwable t, String msgFormat, Object... params) {
        log(Level.TRACE, t, msgFormat, params);
    }
    
    public void log(Level level, String format, Object... params) {
        log(level, String.format(format, params));
    }
    
    public abstract void log(Level level, Object message);
    
    public void log(Level level, Throwable t, String format, Object... params) {
        log(level, t, String.format(format, params));
    }
    
    public abstract void log(Level level, Throwable t, Object message);
    
    public static enum Level {
        FATAL,
        ERROR,
        WARN,
        INFO,
        DEBUG,
        TRACE
    }
}
