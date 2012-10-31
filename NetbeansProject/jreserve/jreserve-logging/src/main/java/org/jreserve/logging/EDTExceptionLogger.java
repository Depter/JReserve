package org.jreserve.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Decsi
 */
class EDTExceptionLogger implements Thread.UncaughtExceptionHandler {

    static void install() {
        Thread.setDefaultUncaughtExceptionHandler(new EDTExceptionLogger());
        System.setProperty("sun.awt.exception.handler", EDTExceptionLogger.class.getName());
    }
    
    private final static Logger logger = Logger.getLogger(EDTExceptionLogger.class.getName());
    
    public void handle(Throwable e) {
        handleException(Thread.currentThread(), e);
    }
    
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        handleException(t, e);
    }
    
    private void handleException(Thread t, Throwable e) {
        String msg = "Uncaught exception at thread: %s";
        msg = String.format(msg, t.getName());
        logger.log(Level.SEVERE, msg, e);
    }
}
