package org.jreserve.persistence.connection;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SessionFactoryBuilder {
    
    private static enum Properties {
        
        DRIVER(Environment.DRIVER),                                 //init
        URL(Environment.URL),                                       //database
        USER_NAME(Environment.USER),                                //Login
        PASSWORD(Environment.PASS),                                 //Login
        DIALECT(Environment.DIALECT),                               //database
        SCHEMA_GENERATION(Environment.HBM2DDL_AUTO),                //init
        C3PO_MIN_SIZE(Environment.C3P0_MIN_SIZE),                   //init
        C3PO_MAX_SIZE(Environment.C3P0_MAX_SIZE),                   //init
        C3PO_TIMEOUT(Environment.C3P0_TIMEOUT),                     //init
        C3PO_MAX_STATEMENT(Environment.C3P0_MAX_STATEMENTS),        //init
        SECOND_LEVEL_CACHE(Environment.CACHE_PROVIDER_CONFIG),      //init
        ECHO_SQL(Environment.SHOW_SQL),                             //init
        SESSION_CONTEXT(Environment.CURRENT_SESSION_CONTEXT_CLASS), //init
        CONNECTION_PROVIDER(Environment.CONNECTION_PROVIDER);       //init
        
        private final String propertyName;
        
        private Properties(String propName) {
            this.propertyName = propName;
        }
        
        public String getPropertyName() {
            return propertyName;
        }
    }
    
    private final static Logger logger = Logger.getLogger(SessionFactoryBuilder.class.getName());
    
    private final static String DRIVER_CLASS = ProxyDriver.class.getName();
    
    private final static int C3PO_MIN_SIZE = 5;
    private final static int C3PO_MAX_SIZE = 5;
    private final static int C3PO_TIMEOUT = 1800;
    private final static int C3PO_MAX_STATEMENTS = 50;
    private final static String CONNECTION_PROVIDER = "org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider";
    
    //No second-level cache
    private final static String SECOND_LEVEL_CACHE = "org.hibernate.cache.internal.NoCacheProvider";
    private final static boolean ECHO_SQL = false;
    private final static String SESSION_CONTEXT = "thread";
    private final static String UPDATE_SCHMEA = "update";
    
    private final Configuration config;
    
    public SessionFactoryBuilder() {
        config = new Configuration();
        initConfiguration();
        addEntities();
    }
    
    private void initConfiguration() {
        setProperty(Properties.DRIVER, DRIVER_CLASS);
        setProperty(Properties.C3PO_MIN_SIZE, C3PO_MIN_SIZE);
        setProperty(Properties.C3PO_MAX_SIZE, C3PO_MAX_SIZE);
        setProperty(Properties.C3PO_TIMEOUT, C3PO_TIMEOUT);
        setProperty(Properties.C3PO_MAX_STATEMENT, C3PO_MAX_STATEMENTS);
        setProperty(Properties.CONNECTION_PROVIDER, CONNECTION_PROVIDER);
        setProperty(Properties.SECOND_LEVEL_CACHE, SECOND_LEVEL_CACHE);
        setProperty(Properties.SESSION_CONTEXT, SESSION_CONTEXT);
        setProperty(Properties.ECHO_SQL, ECHO_SQL);
        setProperty(Properties.SCHEMA_GENERATION, UPDATE_SCHMEA);
    }
    
    private void setProperty(Properties property, int value) {
        setProperty(property, ""+value);
    }
    
    private void setProperty(Properties property, String value) {
        String propName = property.getPropertyName();
        if(value == null) {
            config.getProperties().remove(propName);
        } else {
            config.setProperty(propName, value);
        }
        logger.log(Level.FINE, "Hibernate property set: %s => %s", new Object[]{propName, value});
    }
    
    private void setProperty(Properties property, boolean value) {
        String str = value? "true" : "false";
        setProperty(property, str);
    }
    
    public void setDatabase(String url, String dialect) {
        try {
            setProperty(Properties.URL, url);
            if(dialect!=null)
                setProperty(Properties.DIALECT, dialect);
        } catch (IllegalArgumentException ex) {
            logger.log(Level.SEVERE, "Unable to initialize configuration!", ex);
        }
    }
    
    private void addEntities() {
        EntityFactory entityFactory = new EntityFactory();
        for(Class<?> clazz : entityFactory.getEntityClasses()) {
            logger.log(Level.FINE, "Entity class '%s' added to configuration!", clazz.getName());
            config.addAnnotatedClass(clazz);
        }
    }
    
    public SessionFactory createSessionFactory(String userName, char[] password) {
        setLogin(userName, password);
        ServiceRegistry reg = createRegistry();
        return config.buildSessionFactory(reg);
        //return config.buildSessionFactory();
    }
    
    private void setLogin(String userName, char[] password) {
        setProperty(Properties.USER_NAME, userName);
        setPassword(escapePassword(password));
    }
    
    private void setPassword(String password) {
        String propName = Properties.PASSWORD.getPropertyName();
        if(password == null) {
            config.getProperties().remove(propName);
        } else {
            config.setProperty(propName, password);
        }
        logger.log(Level.FINE, "Hibernate property set: %s => ****", propName);
    }
    
    private String escapePassword(char[] password) {
        if(password == null || password.length==0)
            return null;
        return new String(password);
    }
    
    private ServiceRegistry createRegistry() {
        ServiceRegistryBuilder builder = new ServiceRegistryBuilder();
        builder.applySettings(config.getProperties());
        return builder.buildServiceRegistry(); 
    }
}
