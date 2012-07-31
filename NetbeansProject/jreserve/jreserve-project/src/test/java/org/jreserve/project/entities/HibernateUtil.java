package org.jreserve.project.entities;

import java.sql.DriverManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.jreserve.project.entities.input.ClaimData;
import org.jreserve.project.entities.input.ClaimType;
import org.jreserve.project.entities.input.DataType;
import org.jreserve.project.entities.input.LoB;
import org.jreserve.project.entities.project.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class HibernateUtil {
    
    private final static Class<?>[] ENTITIES = {
        DataType.class,
        LoB.class,
        ClaimType.class,
        ClaimData.class,
        ChangeLog.class,
        Project.class,
        Triangle.class,
        TriangleComment.class,
        TriangleCorrection.class,
        Vector.class,
        VectorComment.class,
        VectorCorrection.class
    };
    
    private static Logger logger = Logger.getLogger(HibernateUtil.class);
    
    private final static Configuration cfg = getConfiguration();
    private final static ServiceRegistry reg = getServiceRegistry(cfg);
    
    
    private static SessionFactory sessionFactory;
    
    
    private static Configuration getConfiguration() {
        Configuration config = new Configuration();
        config.configure();
        addEntities(config);
        return config;
    }
    
    private static void addEntities(Configuration cfg) {
        for(Class<?> entity : ENTITIES)
            cfg.addAnnotatedClass(entity);
    }
    
    private static ServiceRegistry getServiceRegistry(Configuration cfg) {
        ServiceRegistryBuilder builder = new ServiceRegistryBuilder();
        builder.applySettings(cfg.getProperties());
        return builder.buildServiceRegistry();    
    }
    
    public static void open() {
        try {
            sessionFactory = cfg.buildSessionFactory(reg);
        } catch (Throwable ex) {
            logger.error("SessionFactory creation failed!", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    public static void close() {
        sessionFactory.close();
        closeDb();
    }
    
    private static void closeDb() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            DriverManager.getConnection("jdbc:derby:memory:TestDb;drop=true");
        } catch (Exception ex) {}
    }
}
