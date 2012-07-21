package org.jreserve.database;

import java.io.IOException;
import java.util.Properties;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractDatabase extends MultiDataObject {

    public final static String DRIVER_CLASS = "driver.class";
    
    public final static String SHORT_NAME = "short.name";
    public final static String USER_NAME = "user.name";
    public final static String IS_OPENED = "opened";
    public final static String IS_USED = "used";

    public final static String TRUE = "true";
    public final static String FALSE = "false";

    protected Properties properties;
    
    protected AbstractDatabase(FileObject file, AbstractDatabaseLoader loader) throws DataObjectExistsException, IOException {
        super(file, loader);
        initProperties(file);
    }
    
    private void initProperties(FileObject file) throws IOException {
        PropertyReader reader = new PropertyReader(file);
        properties = reader.readProperties();
    }
    
    public String getDriverClass() {
        return getProperty(DRIVER_CLASS);
    }
    
    public String getShortName() {
        return getProperty(SHORT_NAME);
    }
    
    public void setShortName(String shortName) {
        setProperty(SHORT_NAME, shortName);
    }
    
    public String getUserName() {
        return getProperty(USER_NAME);
    }
    
    public void setUserName(String user) {
        setProperty(USER_NAME, user);
    }
    
    public boolean isOpened() {
        return getBooleanProperty(IS_OPENED);
    }
    
    public void setOpened(boolean opened) {
        setBooleanProperty(IS_OPENED, opened);
    }
    
    public boolean isUsed() {
        return getBooleanProperty(IS_USED);
    }
    
    public void setUsed(boolean used) {
        setBooleanProperty(IS_USED, used);
    }
    
    protected String getProperty(String property) {
        return properties.getProperty(property);
    }
    
    protected void setProperty(String property, String value) {
        properties.setProperty(property, value);
        setModified(true);
    }
    
    protected boolean getBooleanProperty(String property) {
        String value = getProperty(property);
        return value!=null && TRUE.equalsIgnoreCase(value);
    }
    
    protected void setBooleanProperty(String property, boolean value) {
        String str = value? TRUE : FALSE;
        setProperty(property, str);
    }

    @Override
    protected Node createNodeDelegate() {
        return new DatabaseNode(this);
    }
    
    public void save() throws IOException {
        PropertyWriter writer = new PropertyWriter(getPrimaryFile());
        writer.writeProperties(properties);
        getCookieSet().remove(this);
        setModified(false);
    }
    
    public void deleteDatabase() throws IOException {
        beforeDelete();
        super.delete();
    }
    
    protected void beforeDelete() {
    }
}
