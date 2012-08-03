package org.jreserve.persistence.connection;

import org.hibernate.SessionFactory;
import org.jreserve.database.DatabaseUtil;
import org.jreserve.database.PersistenceDatabase;
import org.jreserve.logging.Logger;
import org.jreserve.logging.Logging;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class HibernateUtil {
    
    private final static Logger logger = Logging.getLogger(HibernateUtil.class.getName());
    
    private static PersistenceDatabase database;
    private static SessionFactory factory;
    
    static void startupLogin(PersistenceDatabase db) {
        setSessionFactory(loginToDb(db));
        if(factory != null)
            setUsedDb(db);
        else 
            DatabaseUtil.setUsed(db, false);
    }
    
    private static SessionFactory loginToDb(PersistenceDatabase db) {
        logger.info("Logging into database '%s'.", db.getShortName());
        LoginDialog dialog = new LoginDialog(db);
        dialog.setVisible(true);
        return dialog.isCancelled()? null : dialog.getSessionFactory();
    }
    
    private static void setSessionFactory(SessionFactory factory) {
        logger.debug("SessionFactory initialized.");
        HibernateUtil.factory = factory;
    }
    
    private static void setUsedDb(PersistenceDatabase db) {
        HibernateUtil.database = db;
        logger.info("Used database set to '%s'.", db.getShortName());
        if(!db.isUsed())
            DatabaseUtil.setUsed(db, true);
    }
    
    
    public static void login(PersistenceDatabase db) {
        //TODO close old db
        startupLogin(db);
    }    
    
    static void close() {
        logger.info("CLosing SessionFactory...");
        if(factory != null)
            factory.close();
    }
}
