package org.jreserve.oracledatabase;

import java.awt.Image;
import org.jreserve.database.api.Database;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OracleDatabase implements Database, Comparable<OracleDatabase> {
  
    public static final String URL =  "jdbc:oracle:thin:@%s:%d:%s";
    
    private String server;
    private int port;
    private String sid;
    private boolean opened;
    private boolean selected = false;
    
    public OracleDatabase(String server, int port, String sid, boolean opened, boolean selected) {
        this.server = server;
        this.port = port;
        this.sid = sid;
        this.opened = opened;
        this.selected = selected;
    }
    
    public String getServer() {
        return server;
    }
    
    public int getPort() {
        return port;
    }
    
    @Override
    public String getName() {
        return sid;
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
        OracleHome.saveDatabases();
    }
    
    public boolean isOpened() {
        return opened;
    }
    
    void setOpened(boolean opened) {
        this.opened = opened;
    }
    
    @Override
    public String getConnectionString() {
        return String.format(URL, server, port, sid);
    }

    @Override
    public boolean close() throws Exception {
        return OracleHome.closeDatabase(this);
    }

    @Override
    public boolean delete() throws Exception {
        return OracleHome.deleteDatabase(this);
    }

    @Override
    public Image getIcon() {
        return OracleProvider.getImage();
    }

    @Override
    public String getToolTip() {
        String msg = "%s:%d:%s";
        return String.format(msg, server, port, sid);
    }

    @Override
    public int compareTo(OracleDatabase o) {
        if(o == null)
            return -1;
        int dif = server.compareToIgnoreCase(o.server);
        if(dif != 0) return dif;
        dif = port - o.port;
        if(dif != 0) return dif;
        return sid.compareToIgnoreCase(o.sid);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof OracleDatabase)
            return compareTo((OracleDatabase) o) == 0;
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = 31 + server.hashCode();
        hash = 17 * hash + port;
        return 17 * hash + sid.hashCode();
    }
    
    @Override
    public String toString() {
        return getToolTip();
    }
}
