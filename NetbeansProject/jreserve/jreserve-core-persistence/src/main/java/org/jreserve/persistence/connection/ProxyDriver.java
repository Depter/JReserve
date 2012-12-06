package org.jreserve.persistence.connection;

import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ProxyDriver implements Driver {
    
    private final static Logger logger = Logger.getLogger(ProxyDriver.class.getName());
    
    private static boolean initialized = false;
    private static ProxyDriver INSTANCE = null;
    
    public static void registerDriver(String driverName) throws Exception {
        initialize();
        if(isRegistered(driverName))
            return;
        removeProxyDriver();
        addProxyDriver(driverName);
    }
    
    private static void initialize() throws SQLException {
        if(initialized)
            return;
        initialized = true;
        removeRegisteredDrivers();
    }
    
    private static void removeRegisteredDrivers() throws SQLException {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while(drivers.hasMoreElements())
            deregisterDriver(drivers.nextElement());
    }
    
    private static void deregisterDriver(Driver driver) throws SQLException {
        try {
            logger.log(Level.FINE, "Deregistering driver: \"{0}\"", driver.toString());
            DriverManager.deregisterDriver(driver);
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, String.format("Unable to deregister driver: %s", driver.toString()), ex);
            throw ex;
        }
    }
    
    private static boolean isRegistered(String driverName) {
        return INSTANCE != null &&
               INSTANCE.driver.getClass().getName().equalsIgnoreCase(driverName);
    }
    
    private static void removeProxyDriver() throws SQLException {
        if(INSTANCE == null)
            return;
        DriverManager.deregisterDriver(INSTANCE);
        INSTANCE = null;
    }
    
    private static void addProxyDriver(String driverName) throws Exception {
        Driver driver = getDriver(driverName);
        INSTANCE = new ProxyDriver(driver);
        DriverManager.registerDriver(INSTANCE);
    }
    
    private static Driver getDriver(String driverName) throws Exception {
        ClassLoader loader = Lookup.getDefault().lookup(ClassLoader.class);
        Class driverClass = loader.loadClass(driverName);
        return (Driver) driverClass.newInstance();
    }
    
    private final Driver driver;
    
    private ProxyDriver(Driver driver) {
        this.driver = driver;
    }
    
    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return driver.connect(url, info);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return driver.acceptsURL(url);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return driver.getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return driver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return driver.getMinorVersion();
    }

    @Override
    public boolean jdbcCompliant() {
        return driver.jdbcCompliant();
    }

    @Override
    public String toString() {
        return String.format("ProxyDriver [%s]", driver.toString());
    }
}
