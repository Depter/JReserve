package org.jreserve.persistence.connection;

import org.hibernate.SessionFactory;
import org.jreserve.database.DatabaseUtil;
import org.jreserve.database.PersistenceDatabase;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class HibernateUtil {
    
    private final static Logger logger = Logging.getLogger(HibernateUtil.class.getName());
    
    private final static InstanceContent ic = new InstanceContent();
    private final static Lookup lookup = new AbstractLookup(ic);
    
    private static PersistenceDatabase database;
    private static HibernatePersistenceUnit pu;
    private static org.jreserve.persistence.Session session;
    
    static void startupLogin(PersistenceDatabase db) {
        SessionFactory sessionFactory = loginToDb(db);
        if(sessionFactory != null) {
            setSessionFactory(sessionFactory);
            setUsedDb(db);
        }
    }
    
    private static SessionFactory loginToDb(PersistenceDatabase db) {
        logger.info("Logging into database '%s'.", db.getShortName());
        LoginDialog dialog = new LoginDialog(db);
        dialog.setVisible(true);
        return dialog.isCancelled()? null : dialog.getSessionFactory();
    }
    
    private static void setSessionFactory(SessionFactory factory) {
        logger.debug("SessionFactory initialized.");
        pu = new HibernatePersistenceUnit(factory);
        ic.add(pu);
        createSession();
    }
    
    private static void createSession() {
        session = pu.getSession();
        session.beginTransaction();
        ic.add(session);
    }
    
    private static void setUsedDb(PersistenceDatabase db) {
        HibernateUtil.database = db;
        logger.info("Database '%s' is marked as used.", db.getShortName());
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
        return true;
    }
    
    private static void closeSessionFactory() {
        logger.info("CLosing SessionFactory...");
        closeSession();
        closePU();
    }
    
    private static void closeSession() {
        if(session == null) return;
        ic.remove(session);
        session.comitTransaction();
        session = null;
    }
    
    private static void closePU() {
        if(pu == null) return;
        pu.close();
        ic.remove(pu);
        pu = null;
    }
    
    private static void closePersistenceDatabase(boolean appClosing) {
        if(appClosing || database == null) return;
        logger.info("Database '%s' is marked as unused.", database.getShortName());
        DatabaseUtil.setUsed(database, false);
        HibernateUtil.database = null;
    }
    
    public static Lookup getLookup() {
        return lookup;
    }
}
