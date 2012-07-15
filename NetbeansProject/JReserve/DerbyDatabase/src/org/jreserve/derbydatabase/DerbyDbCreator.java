package org.jreserve.derbydatabase;

import java.io.File;
import java.sql.DriverManager;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DerbyDbCreator {
    
    private static Object derbyInstance = null;
    
    private static void loadDriver() throws ClassNotFoundException{
        if(derbyInstance == null)
            derbyInstance = Class.forName(DerbyProvider.DRIVER);
    }
    
    private final File path;
    private final String userName;
    private final String password;
    private final boolean isCreate;
    
    public DerbyDbCreator(File path, String userName, String password, boolean isCreate) {
        this.path = path;
        this.userName = userName;
        this.password = password;
        this.isCreate = isCreate;
    }
    
    public DerbyDatabase createDb() throws Exception {
        loadDriver();
        DriverManager.getConnection(getUrl()).close();
        shutDownDb();
        return new DerbyDatabase(path.getName(), path, false);
    }
    
    private String getUrl() {
        String url = String.format(DerbyDatabase.URL, path.getAbsolutePath());
        if(isCreate)
            url += ";create=true";
        return url + getLoginData();
    }
    
    private String getLoginData() {
        String loginData = userName==null? "" : ";user="+userName;
        loginData += (password==null? "" : ";password="+password);
        return loginData;
    }
    
    private void shutDownDb() {
        try {
            DriverManager.getConnection(getShutDownUrl());
        } catch (Exception ex) {
        }
    }
    
    private String getShutDownUrl() {
        String url = String.format(DerbyDatabase.URL, path.getAbsolutePath());
        return url + getLoginData() + ";shutdown=true";
    }
}
