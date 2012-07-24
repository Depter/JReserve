package org.jreserve.database;

import java.io.IOException;
import java.util.Properties;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.Node;

/**
 * This class represents the possible database connections to use.
 * 
 * The most important properties to implements are the:
 * <ul>
 *   <li>{@link AbstractDatabase#DRIVER_CLASS DRIVER_CLASS}</li>
 *   <li>{@link AbstractDatabase#DRIVER_CLASS DRIVER_CLASS}</li>
 *   <li>{@link AbstractDatabase#DRIVER_CLASS DRIVER_CLASS}</li>
 * </ul>
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class AbstractDatabase extends MultiDataObject implements PersistenceDatabase {

    public final static String DRIVER_CLASS = "driver.class";
    
    public final static String SHORT_NAME = "short.name";
    public final static String USER_NAME = "user.name";
    public final static String IS_OPENED = "opened";
    public final static String IS_USED = "used";

    public final static String TRUE = "true";
    public final static String FALSE = "false";

    protected Properties properties;
    
    /**
     * Creates a new instance.
     * 
     * @param file the file to load.
     * @param loader the loader used.
     * @throws DataObjectExistsException
     * @throws IOException when one of the required properties is missing.
     */
    protected AbstractDatabase(FileObject file, AbstractDatabaseLoader loader) throws DataObjectExistsException, IOException {
        super(file, loader);
        initProperties(file);
    }
    
    private void initProperties(FileObject file) throws IOException {
        PropertyReader reader = new PropertyReader(file);
        properties = reader.readProperties();
        checkProperties();
    }
    
    /**
     * Checks that the required properties are all set. The method get's
     * called after the datbaase file is loaded. If you have important
     * properties, which are not allowed to be null, override this method, and
     * after calling the super method check them here.
     */
    protected void checkProperties() throws IOException {
        checkPropertySet(DRIVER_CLASS);
        checkPropertySet(SHORT_NAME);
    }
    
    /**
     * Checks wether a property is set or not. If it is not set an
     * {@link java.io.IOException IOException} is thrown.
     * 
     * @throws IOException when the property is not set.
     */
    protected void checkPropertySet(String property) throws IOException {
        if(getProperty(property) != null)
            return;
        String msg = "Property '%s' not set in database file '%s'";
        throw new IOException(String.format(msg, property, getPrimaryFile().getPath()));
    }
    
    /**
     * Returns the name of the jdbc-driver for this database.
     * Can not be null;
     */
    @Override
    public String getDriverClass() {
        return getProperty(DRIVER_CLASS);
    }
    
    /**
     * Returns the representation name for this database. This value will be
     * used to display this database. Can not be null.
     */
    @Override
    public String getShortName() {
        return getProperty(SHORT_NAME);
    }
    
    /**
     * Sets the representation name for this database. This value will be
     * used to display this database. Can not be null.
     */
    public void setShortName(String shortName) {
        if(shortName == null)
            throw new NullPointerException("Databae name can not be null!");
        setProperty(SHORT_NAME, shortName);
    }
    
    /**
     * Returns the user name, which is the default value in the login
     * dialogs. Null value also possible.
     */
    @Override
    public String getUserName() {
        return getProperty(USER_NAME);
    }
    
    /**
     * Sets the user name, which is the default value in the login
     * dialogs. Null value also possible.
     */
    public void setUserName(String user) {
        setProperty(USER_NAME, user);
    }
    
    /**
     * Returns wether this database is opened/closed. Only opened databases 
     * are shown in the datbase explorer.
     */
    public boolean isOpened() {
        return getBooleanProperty(IS_OPENED);
    }
    
    /**
     * Marks that this database is opened/closed. Only opened databases are 
     * shown in the datbase explorer.
     */
    public void setOpened(boolean opened) {
        setBooleanProperty(IS_OPENED, opened);
    }
    
    /**
     * Returuns wether this database is the one that is currently
     * used.
     */
    public boolean isUsed() {
        return getBooleanProperty(IS_USED);
    }
    
    /**
     * Marks that this databae is the one that is currently used. Never 
     * set this property directly.
     */
    public void setUsed(boolean used) {
        setBooleanProperty(IS_USED, used);
    }
    
    /**
     * Returns the value for the given property, or <i>null</i>, if
     * it is not found.
     */
    protected String getProperty(String property) {
        return properties.getProperty(property);
    }
    
    /**
     * Sets the value of the given property to the given value
     * or deletes the property if value is <i>null</i>
     */
    protected void setProperty(String property, String value) {
        if(value == null)
            properties.remove(property);
        else
            properties.setProperty(property, value);
        setModified(true);
    }
    
    /**
     * Returns the properties value as a boolean. If the property is
     * not set, or it's not the same value (case ignored) as
     * {@link AbstractDatabase#TRUE TRUE} then <b>false</b> is returned.
     * 
     * @return The value, or false if not set.
     */
    protected boolean getBooleanProperty(String property) {
        String value = getProperty(property);
        return value!=null && TRUE.equalsIgnoreCase(value);
    }
    
    /**
     * Converts the boolean property to <i>null</i>, 
     * {@link AbstractDatabase#TRUE true} or 
     * {@link AbstractDatabase#FALSE false} and calls
     * {@link AbstractDatabase#setProperty(java.lang.String, java.lang.String) setProperty()}.
     * 
     * @param property the name of the property.
     * @param value the new value. Null values are allowed.
     */
    protected void setBooleanProperty(String property, Boolean value) {
        String str = value==null? null : value? TRUE : FALSE;
        setProperty(property, str);
    }
    
    /**
     * Create a node delegate for the database. Because a database
     * can be added more then one view (ie database explorer and
     * open database dialog) it is recommended the return a new
     * instance every time this method called.
     */
    @Override
    protected Node createNodeDelegate() {
        return new DatabaseNode(this);
    }
    
    /**
     * Saves the database to it's '.database' file.
     * 
     * @throws IOException when something goes wrong with the 
     *                     "ConfigHome/Databases/db.database" file.
     */
    public void save() throws IOException {
        PropertyWriter writer = new PropertyWriter(getPrimaryFile());
        writer.writeProperties(properties);
        getCookieSet().remove(this);
        setModified(false);
    }
    
    /**
     * Deletes the database. If you want to perform extra clean up, 
     * then override {@link AbstractDatabase#beforeDelete() beforeDelete()}.
     * 
     * @throws IOException when something goes wrong with the 
     *                     "ConfigHome/Databases/db.database" file.
     */
    public void deleteDatabase() throws IOException {
        beforeDelete();
        super.delete();
    }
    
    /**
     * This method gets called, before the database file on the disk gets
     * deleted. If you want to delete other resources (for example the
     * database directory for a derby db), than you should do it here.
     */
    protected void beforeDelete() {
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof AbstractDatabase)
            return equals((AbstractDatabase) o);
        return false;
    }
    
    private boolean equals(AbstractDatabase database) {
        String name = getShortName();
        String o = database.getShortName();
        return name.equalsIgnoreCase(o);
    }
    
    @Override
    public int hashCode() {
        return getShortName().hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("Database [%s]", getShortName());
    }
}