package org.jreserve.logging;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Logging {
    
    public static synchronized Logger getLogger(String name) {
        return new Log4jLogger(getLog4jLogger(name));
    }
    
    private static org.apache.log4j.Logger getLog4jLogger(String name) {
        return org.apache.log4j.Logger.getLogger(name);
    }
}
