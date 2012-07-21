package org.jreserve.database;

import java.io.IOException;
import java.util.Properties;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DatabaseFactory {
    
    protected Properties properties = new Properties();
    private String dbName;
    
    public DatabaseFactory(String name, String driver) {
        setDatabaseName(name);
        setDriverName(driver);
        setBasicProperties();
    }
    
    private void setDatabaseName(String name) {
        checkDatabaseName(name);
        setProperty(AbstractDatabase.SHORT_NAME, name);
        this.dbName = name;
    }
    
    private void checkDatabaseName(String name) {
        if(name == null)
            throw new NullPointerException("Database name is null!");
        if(DatabaseUtil.getDatabaseByName(name) != null)
            throw new IllegalArgumentException("Databse name '"+name+"' already used!");
    }
    
    private void setDriverName(String driver) {
        if(driver == null)
            throw new NullPointerException("Driver is null!");
        setProperty(AbstractDatabase.DRIVER_CLASS, driver);
    }
    
    private void setBasicProperties() {
        setBooleanProperty(AbstractDatabase.IS_OPENED, true);
        setBooleanProperty(AbstractDatabase.IS_USED, false);
    }
    
    public void setUserName(String userName) {
        setProperty(AbstractDatabase.USER_NAME, userName);
    }
    
    public void setProperty(String property, String value) {
        if(value == null)
            properties.remove(property);
        else
            properties.put(property, value);
    }
    
    public void setBooleanProperty(String property, boolean value) {
        String str = value? AbstractDatabase.TRUE : AbstractDatabase.FALSE;
        setProperty(property, str);
    }
    
    public void createDatabase() throws IOException {
        FileObject file = DatabaseUtil.getFileForName(dbName);
        PropertyWriter writer = new PropertyWriter(file);
        writer.writeProperties(properties);
    }
}
