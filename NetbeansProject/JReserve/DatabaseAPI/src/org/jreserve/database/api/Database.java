package org.jreserve.database.api;

import java.awt.Image;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Database {
    
    public String getName();
    
    public String getConnectionString();
    
    public boolean close() throws Exception;
    
    public boolean delete() throws Exception;
    
    public Image getIcon();
    
    public boolean isSelected();
    
    public void setSelected(boolean selected) throws Exception;
    
    public String getToolTip();
}
