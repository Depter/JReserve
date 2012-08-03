package org.jreserve.persistence.connection;

import org.hibernate.SessionFactory;
import org.jreserve.database.DatabaseUtil;
import org.openide.modules.ModuleInstall;
import org.jreserve.database.PersistenceDatabase;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class HibernateUtil extends ModuleInstall {

    private static PersistenceDatabase database;
    private static SessionFactory factory;

    @Override
    public void restored() {
        PersistenceDatabase db = DatabaseUtil.getUsedDatabase();
        login(db);
    }
    
    private static void startupLogin(PersistenceDatabase db) {
        Logind
    }
    
    static void login(PersistenceDatabase db) {
        //TODO close old db
        
    }
}
