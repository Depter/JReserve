package org.jreserve.project;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.jreserve.project.entities.ChangeLog;
import org.jreserve.project.entities.ClaimType;
import org.jreserve.project.entities.LoB;
import org.jreserve.project.entities.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class HibernateUtil {
    
    private final static Class<?>[] ENTITIES = {
        LoB.class,
        ClaimType.class,
        ChangeLog.class,
        Project.class
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
    }
}
