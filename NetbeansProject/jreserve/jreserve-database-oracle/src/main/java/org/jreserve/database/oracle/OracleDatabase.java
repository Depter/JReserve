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
    private final static String ORACLE_DIALECT = "org.hibernate.dialect.OracleDialect";
    
    public static final String URL =  "jdbc:oracle:thin:@%s:%d:%s";
    
    public final static String HOST  = "db.host";
    public final static String PORT  = "db.port";
    public final static String SID  = "db.sid";

    OracleDatabase(FileObject file, OracleDatabaseLoader loader) throws DataObjectExistsException, IOException {
        super(file, loader);
    }
    
    @Override
    protected void checkProperties() throws IOException {
        super.checkProperties();
        checkPropertySet(HOST);
        checkPropertySet(PORT);
        checkPropertySet(SID);
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

    @Override
    public String getConnectionUrl() {
        String host = getHost();
        int port = getPort();
        String sid = getSID();
        return String.format(URL, host, port, sid);
    }

    @Override
    public String getDialect() {
        return ORACLE_DIALECT;
    }
}
