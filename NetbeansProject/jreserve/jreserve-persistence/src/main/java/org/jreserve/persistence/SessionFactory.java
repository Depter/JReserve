package org.jreserve.persistence;

import org.hibernate.cfg.Configuration;
import org.jreserve.database.PersistenceDatabase;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.jreserve.persistence.entities.EntityFactory;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class SessionFactory {
    
    private static enum Properties {
        
        DRIVER("connection.driver_class"),                          //database
        URL("connection.url"),                                      //database
        USER_NAME("connection.username"),
        PASSWORD("connection.password"),
        DIALECT("dialect"),                                         //database
        SCHEMA_GENERATION("hbm2ddl.auto"),
        C3PO_MIN_SIZE("hibernate.c3p0.min_size"),                   //init
        C3PO_MAX_SIZE("hibernate.c3p0.max_size"),                   //init
        C3PO_TIMEOUT("hibernate.c3p0.timeout"),                     //init
        C3PO_MAX_STATEMENT("hibernate.c3p0.max_statements"),        //init
        SECOND_LEVEL_CACHE("cache.provider_class"),                 //init
        ECHO_SQL("show_sql"),                                       //init
        SESSION_CONTEXT("hibernate.current_session_context_class"); //init
        
        private final String propertyName;
        
        private Properties(String propName) {
            this.propertyName = propName;
        }
        
        public String getPropertyName() {
            return propertyName;
        }
    }
    
    private final static Logger logger = Logging.getLogger(SessionFactory.class.getName());
    
    private final static int C3PO_MIN_SIZE = 5;
    private final static int C3PO_MAX_SIZE = 5;
    private final static int C3PO_TIMEOUT = 1800;
    private final static int C3PO_MAX_STATEMENTS = 50;
    //No second-level cache
    private final static String SECOND_LEVEL_CACHE = "org.hibernate.cache.internal.NoCacheProvider";
    private final static boolean ECHO_SQL = false;
    private final static String SESSION_CONTEXT = "thread";
    private final static String UPDATE_SCHMEA = "update";
    
    private final Configuration config;
    
    SessionFactory() {
        config = new Configuration();
        initConfiguration();
        addEntities();
    }
    
    private void initConfiguration() {
        setProperty(Properties.C3PO_MIN_SIZE, C3PO_MIN_SIZE);
        setProperty(Properties.C3PO_MAX_SIZE, C3PO_MAX_SIZE);
        setProperty(Properties.C3PO_TIMEOUT, C3PO_TIMEOUT);
        setProperty(Properties.C3PO_MAX_STATEMENT, C3PO_MAX_STATEMENTS);
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
        config.setProperty(propName, value);
        logger.info("Hibernate property set: %s => %s", propName, value);
    }
    
    private void setProperty(Properties property, boolean value) {
        String str = value? "true" : "false";
        setProperty(property, str);
    }
    
    void setDatabase(PersistenceDatabase database) {
        try {
            setDriver(database);
            setConnectionUrl(database);
            setDialect(database);
        } catch (IllegalArgumentException ex) {
            logger.error(ex, "Unable to initialize configuration!");
        }
    }
    
    private void setDriver(PersistenceDatabase database) {
        String driver = database.getDriverClass();
        if(driver == null) {
            String msg = String.format("Driver for database '%s' is not set!", database);
            throw new IllegalArgumentException(msg);
        }
        setProperty(Properties.DRIVER, driver);
    }
    
    private void setConnectionUrl(PersistenceDatabase database) {
        String url = database.getConnectionUrl();
        if(url == null) {
            String msg = String.format("Connection url for database '%s' is not set!", database);
            throw new IllegalArgumentException(msg);
        }
        setProperty(Properties.URL, url);
    }
    
    private void setDialect(PersistenceDatabase database) {
        String dialect = database.getDialect();
        if(dialect != null)
            setProperty(Properties.DIALECT, dialect);
    }
    
    private void addEntities() {
        EntityFactory entityFactory = new EntityFactory();
        for(Class<?> clazz : entityFactory.getEntityClasses())
            config.addAnnotatedClass(clazz);
    }
}
