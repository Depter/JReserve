package org.jreserve.project.entities;

import java.sql.Connection;
import java.sql.DriverManager;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.jreserve.project.entities.input.*;
import org.jreserve.project.entities.project.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class HibernateUtil {
    
    private final static Class<?>[] ENTITIES = {
        IdGenerator.class,
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
    private static final SessionFactory sessionFactory = buildFactory();
    
    private static SessionFactory buildFactory() {
        try {
            Configuration cfg = getConfiguration();
            ServiceRegistry reg = getServiceRegistry(cfg);
            SessionFactory factory = cfg.buildSessionFactory(reg);
            IdGenerator.generateIds(factory, ENTITIES);
            return cfg.buildSessionFactory(reg);
        } catch (Throwable ex) {
            logger.error("SessionFactory creation failed!", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    private static Configuration getConfiguration() {
        Configuration cfg = new Configuration();
        cfg.configure();
        addEntities(cfg);
        return cfg;
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
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
