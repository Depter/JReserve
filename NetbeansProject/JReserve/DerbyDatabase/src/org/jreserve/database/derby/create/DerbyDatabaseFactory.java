package org.jreserve.database.derby.create;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.jreserve.database.DatabaseFactory;
import org.jreserve.database.derby.DerbyDatabase;
import org.jreserve.database.derby.DerbyDatabaseProvider;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class DerbyDatabaseFactory {
    
    private final static String SET_USER = 
        "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.user.%s','%s')";
    
    private static Object derbyInstance = null;
    
    private static void loadDriver() throws ClassNotFoundException{
        if(derbyInstance == null)
            derbyInstance = Class.forName(DerbyDatabaseProvider.DRIVER_NAME);
    }
    
    private final File dbHome;
    private final boolean create;
    private final String userName;
    private final String password;

    DerbyDatabaseFactory(File dbHome, String userName, String password) {
        this.dbHome = dbHome;
        create = !dbHome.exists();
        this.userName = userName;
        this.password = password;
    }
    
    void createDb() throws Exception {
        loadDriver();
        createDatabase();
        createDatabaseFile();
    }
    
    private void createDatabase() throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(getUrl());
            createAuthentication(conn);
        } finally {
            shutDownDb(conn);
        }
    }
    
    private String getUrl() {
        String url = String.format(DerbyDatabase.URL, dbHome.getAbsolutePath());
        if(create)
            url += ";create=true";
        return url + getLoginData();
    }
    
    private String getLoginData() {
        String loginData = userName==null? "" : ";user="+userName;
        loginData += (password==null? "" : ";password="+password);
        return loginData;
    }
    
    private void createAuthentication(Connection conn) throws SQLException {
        if(!create || userName==null || password==null)
            return;
        executeStatement(conn, "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.authentication.provider', 'BUILTIN')");
        executeStatement(conn, String.format(SET_USER, userName, password));
    }
    
    private void executeStatement(Connection conn, String statement) throws SQLException {
        CallableStatement cstm = null;
        try {
            cstm = conn.prepareCall(statement);
            cstm.execute();
        } finally {
            if(cstm != null)
                cstm.close();
        }
    }
    
    private void shutDownDb(Connection conn) {
        try {
            if(conn != null) conn.close();
        } catch (SQLException ex) {}
        try {
            DriverManager.getConnection(getShutDownUrl());
        } catch (Exception ex) {}
    }
    
    private String getShutDownUrl() {
        String url = String.format(DerbyDatabase.URL, dbHome.getAbsolutePath());
        return url + getLoginData() + ";shutdown=true";
    }
    
    private void createDatabaseFile() throws IOException {
        DatabaseFactory factory = new DatabaseFactory(dbHome.getName(), DerbyDatabaseProvider.DRIVER_NAME);
        factory.setProperty(DerbyDatabase.DB_FOLDER, dbHome.getAbsolutePath());
        if(userName!=null && userName.length()>0)
            factory.setUserName(userName);
        factory.createDatabase();
    }
}
