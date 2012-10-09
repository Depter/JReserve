package org.jreserve.persistence;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class IdGenerator {
    
    public static String getId() {
        return java.util.UUID.randomUUID().toString();
    }
    
    private IdGenerator() {
    }
}
