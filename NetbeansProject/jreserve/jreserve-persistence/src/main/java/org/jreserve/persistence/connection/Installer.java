package org.jreserve.persistence.connection;

import org.jreserve.database.DatabaseUtil;
import org.jreserve.database.PersistenceDatabase;
import org.openide.modules.ModuleInstall;
import org.openide.windows.WindowManager;

/**
 * Perform tasks related to startup and close.
 * 
 * <p>At startup:
 * <ul>
 *   <li>The Javassist classloader is {@link JavaassistClassLoader set}.</li>
 *   <li>if there is a databses, with used status, the 
 *       {@link LoginDialog LoginDialog} is shown to the user.</li>
 * </ul>
 * </p>
 * 
 * <p>After all modules are agreed to close, the Hibernate 
 * SessionFactory is closed, if there was any.</p>
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class Installer extends ModuleInstall {
    
    private final static boolean APP_CLOSING = true;
    
    @Override
    public void restored() {
        super.restored();
        initializeJavassistClassLoader();
        loginToOpenedDatabase();
    }
    
    private void initializeJavassistClassLoader() {
        JavaassistClassLoader.initialize();
    }
    
    private void loginToOpenedDatabase() {
        PersistenceDatabase database = DatabaseUtil.getUsedDatabase();
        if(database != null)
            loginFromAwtThread(database);
    }
    
    private void loginFromAwtThread(final PersistenceDatabase database) {
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                HibernateUtil.startupLogin(database);
            }
        });
    }

    @Override
    public void close() {
        super.close();
        HibernateUtil.close(APP_CLOSING);
    }
}
