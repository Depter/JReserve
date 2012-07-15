package org.jreserve.database.api;

import java.awt.Image;
import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface DatabaseProvider {
    
    public String getName();
    
    public List<? extends Database> getDatabases() throws Exception;
    
    public boolean createDatabase() throws Exception;
    
    public boolean openDatabase() throws Exception;
    
    public Image getIcon();
}
