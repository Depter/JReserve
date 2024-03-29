package org.jreserve.persistence.connection;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;
import org.jreserve.database.DatabaseUtil;
import org.jreserve.database.PersistenceDatabase;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class HibernateUtil {
    
    private final static Logger logger = Logger.getLogger(HibernateUtil.class.getName());
    
    private final static InstanceContent ic = new InstanceContent();
    private final static Lookup lookup = new AbstractLookup(ic);
    
    private static PersistenceDatabase database;
    private static SessionFactory sessionFactory;
    
    static void startupLogin(PersistenceDatabase db) {
        SessionFactory factory = loginToDb(db);
        if(factory != null) {
            setSessionFactory(factory);
            setUsedDb(db);
        } else {
            if(db.isUsed())
                DatabaseUtil.setUsed(db, false);
        }
    }
    
    private static SessionFactory loginToDb(PersistenceDatabase db) {
        logger.log(Level.INFO, "Logging into database \"{0}\".", db.getShortName());
        LoginDialog dialog = new LoginDialog(db);
        dialog.setVisible(true);
        return dialog.isCancelled()? null : dialog.getSessionFactory();
    }
    
    private static void setSessionFactory(SessionFactory factory) {
        logger.log(Level.FINE, "SessionFactory initialized.");
        sessionFactory = factory;
        ic.add(sessionFactory);
    }
    
    private static void setUsedDb(PersistenceDatabase db) {
        HibernateUtil.database = db;
        logger.log(Level.INFO, "Database \"{0}\" is marked as used.", db.getShortName());
        DatabaseUtil.setUsed(db, true);
    }
    
    
    public static void login(PersistenceDatabase db) {
        //TODO close old db
        startupLogin(db);
    }    
    
    public static boolean close(boolean appClosing) {
        if(!saveOpenEntities())
            return false;
        closeSessionFactory();
        closePersistenceDatabase(appClosing);
        return true;
    }
    
    private static boolean saveOpenEntities() {
        //TODO ask user
        return true;
    }
    
    private static void closeSessionFactory() {
        logger.info("CLosing SessionFactory...");
        if(sessionFactory != null) { 
            sessionFactory.close();
            ic.remove(sessionFactory);
            sessionFactory = null;
        }
    }
    
    private static void closePersistenceDatabase(boolean appClosing) {
        if(appClosing || database == null) return;
        logger.log(Level.INFO, "Database \"{0}\" is marked as unused.", database.getShortName());
        DatabaseUtil.setUsed(database, false);
        HibernateUtil.database = null;
    }
    
    public static Lookup getLookup() {
        return lookup;
    }
}
