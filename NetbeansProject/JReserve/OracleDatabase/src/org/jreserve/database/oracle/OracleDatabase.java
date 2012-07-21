package org.jreserve.database.oracle;

import java.io.IOException;
import org.jreserve.database.AbstractDatabase;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class OracleDatabase extends AbstractDatabase {
    public static final String URL =  "jdbc:oracle:thin:@%s:%d:%s";
    
    public final static String HOST  = "db.host";
    public final static String PORT  = "db.port";
    public final static String SID  = "db.sid";

    OracleDatabase(FileObject file, OracleDatabaseLoader loader) throws DataObjectExistsException, IOException {
        super(file, loader);
    }
    
    public String getHost() {
        return getProperty(HOST);
    }
    
    public void setHost(String host) {
        setProperty(HOST, host);
    } 
    
    public int getPort() {
        String str = getProperty(PORT);
        try {
            return Integer.parseInt(str);
        } catch (Exception ex) {
            return -1;
        }
    }
    
    public void setPort(int port) {
        setProperty(PORT, ""+port);
    } 
    
    public String getSID() {
        return getProperty(SID);
    }
    
    public void setSID(String sid) {
        setProperty(SID, sid);
    } 
}
