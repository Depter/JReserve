package org.jreserve.persistence;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.cfg.Configuration;
import org.openide.util.Lookup;

/**
 * This class should be used to obtain {@link javax.persistence.EntityManager EntityManager}
 * instances in all modules.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class PersistenceProvider {
    
    private final static Logger logger = Logger.getLogger(PersistenceProvider.class.getName());
    
    private static EntityManagerFactory factory;
    
    public static EntityManager createEntityManager() {
        initFactory();
        return factory.createEntityManager();
    }
    
    private static void initFactory() {
        if(factory == null) {
            logger.log(Level.INFO, "Creating EntityManagerFactory.");
            loadEntityClasses();
        }
    }
    
    private static void loadEntityClasses() {
        Configuration cfg = new Configuration();
        Collection<? extends PersistenceEntityOwner> owners = 
                Lookup.getDefault().lookupAll(PersistenceEntityOwner.class);
        for(PersistenceEntityOwner owner : owners)
            loadEntityClasses(owner, cfg);
    }
    
    private static void loadEntityClasses(PersistenceEntityOwner owner, Configuration cfg) {
        for(Class c : owner.getEntities()) {
            cfg.addAnnotatedClass(c);
            logger.log(Level.INFO, "Adding entity '%s'", c);
        }
    }
}