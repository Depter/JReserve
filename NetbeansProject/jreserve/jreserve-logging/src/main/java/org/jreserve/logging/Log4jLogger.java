package org.jreserve.logging;

import org.apache.log4j.Priority;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class Log4jLogger extends Logger {

    private final org.apache.log4j.Logger logger;
    
    Log4jLogger(org.apache.log4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void log(Level level, Object message) {
        Priority priority = getPriority(level);
        logger.log(priority, message);
    }
    
    private Priority getPriority(Level level) {
        switch(level) {
            case FATAL: return org.apache.log4j.Level.FATAL;
            case ERROR: return org.apache.log4j.Level.ERROR;
            case WARN: return org.apache.log4j.Level.WARN;
            case INFO: return org.apache.log4j.Level.INFO;
            case DEBUG: return org.apache.log4j.Level.DEBUG;
            case TRACE: return org.apache.log4j.Level.TRACE;
            default: throw new IllegalArgumentException("Unknown level: "+level);
        }
    }

    @Override
    public void log(Level level, Throwable t, Object message) {
        Priority priority = getPriority(level);
        logger.log(priority, message, t);
    }
}
