package org.jreserve.logging;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class Log4jLogger implements Logger {

    private final org.apache.log4j.Logger logger;
    
    Log4jLogger(org.apache.log4j.Logger logger) {
        this.logger = logger;
    }
    
    @Override
    public void fatal(Object message) {
        logger.fatal(message);
    }

    @Override
    public void fatal(Throwable t, Object message) {
        logger.fatal(message, t);
    }

    @Override
    public void fatal(String msgFormat, Object... params) {
        logger.fatal(String.format(msgFormat, params));
    }

    @Override
    public void fatal(Throwable t, String msgFormat, Object... params) {
        logger.fatal(String.format(msgFormat, params), t);
    }

    @Override
    public void error(Object message) {
        logger.error(message);
    }

    @Override
    public void error(Throwable t, Object message) {
        logger.error(message, t);
    }

    @Override
    public void error(String msgFormat, Object... params) {
        logger.error(String.format(msgFormat, params));
    }

    @Override
    public void error(Throwable t, String msgFormat, Object... params) {
        logger.error(String.format(msgFormat, params), t);
    }

    @Override
    public void warn(Object message) {
        logger.warn(message);
    }

    @Override
    public void warn(Throwable t, Object message) {
        logger.warn(message, t);
    }

    @Override
    public void warn(String msgFormat, Object... params) {
        logger.warn(String.format(msgFormat, params));
    }

    @Override
    public void warn(Throwable t, String msgFormat, Object... params) {
        logger.warn(String.format(msgFormat, params), t);
    }

    @Override
    public void info(Object message) {
        logger.info(message);
    }

    @Override
    public void info(Throwable t, Object message) {
        logger.info(message, t);
    }

    @Override
    public void info(String msgFormat, Object... params) {
        logger.info(String.format(msgFormat, params));
    }

    @Override
    public void info(Throwable t, String msgFormat, Object... params) {
        logger.info(String.format(msgFormat, params), t);
    }

    @Override
    public void debug(Object message) {
        logger.debug(message);
    }

    @Override
    public void debug(Throwable t, Object message) {
        logger.debug(message, t);
    }

    @Override
    public void debug(String msgFormat, Object... params) {
        logger.debug(String.format(msgFormat, params));
    }

    @Override
    public void debug(Throwable t, String msgFormat, Object... params) {
        logger.debug(String.format(msgFormat, params), t);
    }

    @Override
    public void trace(Object message) {
        logger.trace(message);
    }

    @Override
    public void trace(Throwable t, Object message) {
        logger.trace(message, t);
    }

    @Override
    public void trace(String msgFormat, Object... params) {
        logger.trace(String.format(msgFormat, params));
    }

    @Override
    public void trace(Throwable t, String msgFormat, Object... params) {
        logger.trace(String.format(msgFormat, params), t);
    }
    
}
