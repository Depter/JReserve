package org.jreserve.persistence.connection;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.Configurable;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;

/**
 *
 * @author Peter Decsi
 */
public class HibernateDataSource implements ConnectionProvider, Configurable {

    private final static Logger logger = Logging.getLogger(HibernateDataSource.class.getName());
    
    private static HibernateDataSource INSTANCE = null;
    
    public synchronized static HibernateDataSource getInstance() {
        return INSTANCE;
    }
    
    private synchronized static void setInstance(HibernateDataSource hds) {
        if(INSTANCE != null)
            throw new IllegalStateException("DataSource already initialized!");
        INSTANCE = hds;
    }
    
    private synchronized static void clearInstance() {
        INSTANCE = null;
    }
    
    private BoneCP ds = null;
    private boolean autocommit = false;
    private Integer isolation = null;
    
    public HibernateDataSource() {
        setInstance(this);
    }

    @Override
    public void configure(Map map) {
        try {
            Class.forName((String) map.get(Environment.DRIVER));
            
            setAutoCommit(map);
            setIsolation(map);
            
            BoneCPConfig config = new BoneCPConfig();
            config.setPartitionCount(1);
            config.setAcquireIncrement(getIntValue(map, Environment.C3P0_ACQUIRE_INCREMENT, 1));
            config.setMinConnectionsPerPartition(getIntValue(map, Environment.C3P0_MIN_SIZE, 1));
            config.setMaxConnectionsPerPartition(getIntValue(map, Environment.C3P0_MAX_SIZE, 3));
            config.setStatementsCacheSize(getIntValue(map, Environment.C3P0_MAX_STATEMENTS, 50));
            config.setConnectionTimeoutInMs(getLongValue(map, Environment.C3P0_TIMEOUT, 3000));
            
            config.setJdbcUrl((String) map.get(Environment.URL));
            config.setUsername((String) map.get(Environment.USER));
            config.setPassword((String) map.get(Environment.PASS));
            
            ds = new BoneCP(config);
        } catch (Exception ex) {
            logger.error(ex, "Unable to create connection pool!");
            throw new HibernateException("Unable to create connection pool!", ex);
        }
    }
    
    private void setAutoCommit(Map map) {
        Boolean value = (Boolean) map.get(Environment.AUTOCOMMIT);
        autocommit = value==null? false : value;
    }
    
    private void setIsolation(Map map) {
        String i = (String) map.get(Environment.ISOLATION);
        if(i != null)
            isolation = new Integer(i);
    }
    
    private int getIntValue(Map map, String propName, int defaultValue) {
        String value = (String) map.get(propName);
        if(value == null)
            return defaultValue;
        return Integer.parseInt(value);
    }
    
    private long getLongValue(Map map, String propName, long defaultValue) {
        String value = (String) map.get(propName);
        if(value == null)
            return defaultValue;
        return Long.parseLong(value);
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        checkState();
        Connection conn = ds.getConnection();
        initConnection(conn);
        return conn;
    }

    private void checkState() {
        if(ds == null)
            throw new IllegalStateException("DataSource has not been configured!");
    }
    
    private void initConnection(Connection conn) throws SQLException {
        if(conn.getAutoCommit() != autocommit)
            conn.setAutoCommit(autocommit);
        if(isolation != null)
            conn.setTransactionIsolation(isolation);
    }
    
    @Override
    public void closeConnection(Connection cnctn) throws SQLException {
        cnctn.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class type) {
        return ConnectionProvider.class.equals(type) ||
               HibernateDataSource.class.isAssignableFrom(type);
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        if(ConnectionProvider.class.equals(type) || HibernateDataSource.class.isAssignableFrom(type))
            return (T) this;
	throw new UnknownUnwrapTypeException(type);
    }

    public void close() {
        logger.debug("Closing DataSource.");
        ds.shutdown();
        clearInstance();
    }
}
