package org.jreserve.database;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DatabaseProvider {

    public String getName();
    
    public boolean createDatabase();
}
