package org.jreserve.logging;

import org.apache.log4j.Logger;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class Logging {
    
    public static Logger getLogger(String name) {
        return Logger.getLogger(name);
    }
}
