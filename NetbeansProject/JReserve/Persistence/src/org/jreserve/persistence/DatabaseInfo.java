package org.jreserve.persistence;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DatabaseInfo {
    
    public String getDriverName();
    
    public String getConnectionString();
    
    public String getUserName();
    
    public char[] getPassword();
    
    public String getDatabeseName();
}
