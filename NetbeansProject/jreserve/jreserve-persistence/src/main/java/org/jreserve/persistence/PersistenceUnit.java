package org.jreserve.persistence;

/**
 * Session provider. When the application is connected to
 * a database, an instance of this class will be put to
 * the lookup of the {@link PersistenceUtil PersistenceUtil} 
 * class. When a connection is closed, it will be removed 
 * form the lookup. Listen for the given lookup for this 
 * interface, if you would like to get notified about 
 * connection events.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface PersistenceUnit {

    /**
     * Returns a new session.
     */
    public Session getSession();
}
