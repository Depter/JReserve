package org.jreserve.database.oracle.create;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.jreserve.database.DatabaseFactory;
import org.jreserve.database.oracle.OracleDatabase;
import org.jreserve.database.oracle.OracleDatabaseProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class OracleDatabaseFactory {
    
    private static Object derbyInstance = null;
    
    private static void loadDriver() throws ClassNotFoundException{
        if(derbyInstance == null)
            derbyInstance = Class.forName(OracleDatabaseProvider.DRIVER_NAME);
    }

    private final DummyDatabase db;
    private final String userName;
    private final String password;
    
    OracleDatabaseFactory(DummyDatabase db, String userName, String password) {
        this.db = db;
        this.userName = userName;
        this.password = password;
    }
    
    void createDb() throws ClassNotFoundException, SQLException, IOException {
        loadDriver();
        DriverManager.getConnection(getUrl(), userName, password).close();
        createDatabaseFile();
    }
    
    private String getUrl() {
        String host = db.getHost();
        int port = db.getPort();
        String sid = db.getSid();
        return String.format(OracleDatabase.URL, host, port, sid);
    }
    
    private void createDatabaseFile() throws IOException {
        DatabaseFactory factory = new DatabaseFactory(db.getSid(), OracleDatabaseProvider.DRIVER_NAME);
        setOracleProperties(factory);
        if(userName!=null && userName.length()>0)
            factory.setUserName(userName);
        factory.createDatabase();
    }
    
    private void setOracleProperties(DatabaseFactory factory) {
        factory.setProperty(OracleDatabase.HOST, db.getHost());
        factory.setProperty(OracleDatabase.PORT, ""+db.getPort());
        factory.setProperty(OracleDatabase.SID, db.getSid());
    }
}
