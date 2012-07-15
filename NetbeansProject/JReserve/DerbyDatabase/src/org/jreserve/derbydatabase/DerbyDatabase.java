package org.jreserve.derbydatabase;

import java.awt.Image;
import java.io.File;
import org.jreserve.database.api.Database;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DerbyDatabase implements Database, Comparable<DerbyDatabase> {
    
    public final static String URL = "jdbc:derby:%s";

    private String path;
    private String name;
    private boolean selected = false;
    
    public DerbyDatabase(String name, File path, boolean selected) {
        this.path = path.getAbsolutePath();
        this.name = name;
        this.selected = selected;
    }
    
    public DerbyDatabase(String name, String path, boolean selected) {
        this.name = name;
        this.path = path;
        this.selected = selected;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public String getPath() {
        return path;
    }
    
    @Override
    public String getConnectionString() {
        return String.format(URL, path);
    }

    @Override
    public Image getIcon() {
        return DerbyProvider.getDerbyImage();
    }

    @Override
    public String getToolTip() {
        return path;
    }

    @Override
    public boolean close() throws Exception {
        return DerbyHome.closeDatabase(this);
    }
    
    @Override
    public boolean delete() throws Exception {
        return DerbyHome.deleteDatabase(this);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) throws Exception {
        if(this.selected == selected)
            return;
        this.selected = selected;
        DerbyHome.saveDatabases();
    }

    @Override
    public int compareTo(DerbyDatabase o) {
        if(o == null)
            return -1;
        return comparePath(o.path);
    }
    
    private int comparePath(String o) {
        File f1 = path==null? null : new File(path);
        File f2 = o==null? null : new File(o);
        if(f1==null)
            return f2==null? 0 : 1;
        return f2==null? -1 : f1.compareTo(f2);
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof DerbyDatabase))
            return false;
        return compareTo((DerbyDatabase)o) == 0;
    }
    
    @Override
    public int hashCode() {
        return path==null? 0 : path.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("DerbyDatabase [%s]", path);
    }
}
