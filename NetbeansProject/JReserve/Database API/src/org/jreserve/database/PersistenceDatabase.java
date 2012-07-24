package org.jreserve.database;

/**
 * This interface defines the contract between the Database API and
 * the Persistence API.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface PersistenceDatabase {
    
    /**
     * Returns the name of the jdbc-driver for this database.
     * Can not be null;
     */
    public String getDriverClass();
    
    /**
     * Returns the connection string for this database. Do not include
     * username and password here. The user will get a login dialog
     * when needed.
     */
    public abstract String getConnectionUrl();
    
    /**
     * Returns the sql dialect for hibernate to use. A <i>null</i>
     * return value allowed. In such cases Hibernate will try to guess it.
     * 
     * @see <a href="http://docs.jboss.org/hibernate/orm/3.3/reference/en/html/session-configuration.html#configuration-optional-dialects">Hibernate documentation</a>
     */
    public abstract String getDialect();
    
    /**
     * Returns the user name, which is the default value in the login
     * dialogs. Null value also possible.
     */
    public String getUserName();
    
    /**
     * Returns the representation name for this database. This value will be
     * used to display this database. Can not be null.
     */
    public String getShortName();
}
